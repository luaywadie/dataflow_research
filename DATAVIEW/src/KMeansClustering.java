import dataview.models.DATAVIEW_BigFile;
import dataview.models.Task;
import dataview.models.Workflow;

public class KMeansClustering extends Workflow {
    private final String UNIQUE_ID = "0101_";  // used to easily find output data for a specific run of the workflow
    private final String INPUT_FILE = "seeds_dataset.csv";
    private final String CLUSTER_ASSIGNMENTS_OUTPUT = UNIQUE_ID + "KMeans_Assignments";
    private final String CLUSTER_SSE_OUTPUT = UNIQUE_ID + "KMeans_SSEs";

    // constants
    public static final int K = 3;  // number of clusters
    public static final int P = 4; // how many partitions to divide data in to
    public static final int N = 4; // number of iterations to run for (may not converge)
    public static final int F = 7; // number of features in the data set (excluding ID)

    public KMeansClustering() {
        super("PlanetaryClustering", "Use K-Means to group similar planets");
        wins = new DATAVIEW_BigFile[1];
        wins[0] = new DATAVIEW_BigFile(INPUT_FILE);

        wouts = new DATAVIEW_BigFile[2];
        // original data set but with cluster assignment as the last column
        wouts[0] = new DATAVIEW_BigFile(CLUSTER_ASSIGNMENTS_OUTPUT);
        // SSE of each cluster individually, as well as the sum of all of the errors
        wouts[1] = new DATAVIEW_BigFile(CLUSTER_SSE_OUTPUT);

    }

    public void design() {
        // -----create tasks-----//
        // tasks that set us up for iteration
        Task featurePartitioner = addTask("FeaturePartitioner");
        Task[] normalizeData = addTasks("NormalizeData", F);
        Task columnsToMatrix = addTask("ColumnsToMatrix");
        Task initialRowPartitioner = addTask("RowPartitioner");
        Task initCentroids = addTask("InitCentroids");
        /* The set of AssignCluster tasks used directly after InitCentroids.
         * Just makes indexing of tasks for edges a lot easier. */
        Task[] initialAssignClusters = addTasks("AssignClusters", P);

        // tasks that are part of the iteration
        Task[] iterPartitionMerger = addTasks("PartitionMerger", N);
        Task[] iterClusterPartitioner = addTasks("ClusterPartitioner", N);
        Task[] iterCalculateCentroid = addTasks("CalculateCentroid", N * K);
        Task[] iterRowPartitioner = addTasks("RowPartitioner", N);
        Task[] iterMakeCentroidMatrix = addTasks("MakeCentroidMatrix", N);
        Task[] iterAssignClusters = addTasks("AssignClusters", N * P);

        // tasks that follow iteration
        Task partitionMerger = addTask("PartitionMerger");
        /* The set of AssignCluster tasks used in the iterations. */
        Task clusterPartitioner = addTask("ClusterPartitioner");
        Task[] calculateCentroid = addTasks("CalculateCentroid", K);
        Task[] oneClusterSSE = addTasks("OneClusterSSE", K);
        Task allClusterSSE = addTask("AllClusterSSE");
        Task clusteringResultWriter = addTask("ClusteringResultWriter");
        Task extraOutputConsumer = addTask("ExtraOutputConsumer");

        // ------ IO edges ------ //
        // input
        addEdge(0, featurePartitioner, 0);
        // clustering assignments
        addEdge(clusteringResultWriter, 0);
        // SSE (for all clusters and total)
        addEdge(allClusterSSE, 1);

        // ------ Inter-Task Edges ------ //
        // first set of non-iterative tasks (come BEFORE iteration)
        // from FeaturePartitioner to each instance of NormalizeData
        for (int f = 0;  f < F; f++) {
            addEdge(featurePartitioner, f, normalizeData[f], 0);  // a non-normalized column vector
        }
        // from NormalizeData to ColumnsToMatrix
        for (int f = 0; f < F; f++) {
            addEdge(normalizeData[f], 0, columnsToMatrix, f); // a unit-norm column vector
        }
        // from ColumnsToMatrix to RowPartitioner and InitCentroids
        addEdge(columnsToMatrix, 0, initialRowPartitioner, 0); // full data set matrix
        addEdge(columnsToMatrix, 0, initCentroids, 0);  // full data set matrix
        // from RowPartitioner and InitCentroids to AssignClusters
        for (int p = 0; p < P; p++) {
            addEdge(initialRowPartitioner, p, initialAssignClusters[p], 0);  // a partition of the full data set
            addEdge(initCentroids, 0, initialAssignClusters[p], 1);  // centroid matrix
        }  //

        // edges from the first set of non-iterative tasks to the first iteration of iterable tasks
        // from AssignClusters to the 1st instance of PartitonMerger
        for (int p = 0; p < P; p++) {
            addEdge(initialAssignClusters[p], 0, iterPartitionMerger[0], p);  // partitions of data set with assigned cluster
        }

        // edges within iteration
        for (int n = 0; n < N; n++) {
            // from PartitionMerger to ClusterPartitioner and RowPartitioner
            // this edge carries the full data set WITHOUT the cluster assignment column
            addEdge(iterPartitionMerger[n], 0, iterRowPartitioner[n], 0);
            // this edge carries the full data set WITH the cluster assignment column
            addEdge(iterPartitionMerger[n], 1, iterClusterPartitioner[n], 0);

            // from ClusterPartitioner to CalculateCentroid
            for (int k = 0; k < K; k++) {
                addEdge(iterClusterPartitioner[n], k, iterCalculateCentroid[n * K + k], 0);  // a matrix containing samples of a single cluster
            }

            // from CalculateCentroid to MakeCentroidMatrix
            for (int k = 0; k < K; k++) {
                addEdge(iterCalculateCentroid[n * K + k], 0, iterMakeCentroidMatrix[n], k);  // the centroid of a point
            }

            // from RowPartitioner and MakeCentroidMatrix to AssignClusters
            for (int p = 0; p < P; p++) {
                addEdge(iterRowPartitioner[n], p, iterAssignClusters[n * P + p], 0); // a partition of the entire data set
                addEdge(iterMakeCentroidMatrix[n], 0, iterAssignClusters[n * P + p], 1); // matrix containing all centroids
            }

            // from AssignClusters back to PartitionMerger (which is the beginining of the next iteration
            if (n != N - 1) {  // final iteration goes to non-iterative tasks, otherwise connect to next iteration
                for (int p = 0; p < P; p++) {
                   addEdge(iterAssignClusters[n * P + p], 0, iterPartitionMerger[n + 1], p); // partition matrix with cluster assignments
                }
            }
        }

        // edges from the final instance of iterative tasks to the second set of non-iteartive tasks
        // from AssignClusters to PartitionMerger
        for (int p = 0; p < P; p++) {
            addEdge(iterAssignClusters[(N - 1) * P + p], 0, partitionMerger, p);  // partition matrix with cluster assignments
        }

        // edges following the iterative portion of the workflow
        // edge from PartitonMerger to ClusterPartitioner
        addEdge(partitionMerger, 0, extraOutputConsumer, 0); // don't need this output outside of iteration
        addEdge(partitionMerger, 1, clusterPartitioner, 0); // matrix of ALL data points

        // edges from ClusterPartitioner to ClusteringResultWriter, CalculateCentroid, and OneClusterSSE
        for (int k = 0; k < K; k++) {
            addEdge(clusterPartitioner, k, clusteringResultWriter, k);
            addEdge(clusterPartitioner, k, oneClusterSSE[k], 0);
            addEdge(clusterPartitioner, k, calculateCentroid[k], 0);
        }

        // edges from CalculateCentroid to OneClusterSSE
        for (int k = 0; k < K; k++) {
            addEdge(calculateCentroid[k], 0, oneClusterSSE[k], 1);
        }

        // from OneClusterSSE to AllClusterSSE
        for (int k = 0; k < K; k++) {
            addEdge(oneClusterSSE[k], 0, allClusterSSE, 2 * k);  // SSE of cluster
            addEdge(oneClusterSSE[k], 1, allClusterSSE, 2 * k + 1);  // cardinality of cluster
        }
    }
}
