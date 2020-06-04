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
        Task featurePartitioner = addTask("FeaturePartitioner");
        Task[] normalizeData = addTasks("NormalizeData", F);
        Task initCentroids = addTask("InitCentroids");
        /* The set of AssignCluster tasks used directly after InitCentroids.
         * Just makes indexing of tasks for edges a lot easier. */
        Task[] initialAssignClusters = addTasks("AssignClusters", P);
        /* Tasks below this line have yet to be implemented */
        Task[] updateCentroids = addTasks("UpdateCentroids", N);
        /* The set of AssignCluster tasks used in the iterations. */
        Task[] iterAssignClusters = addTasks("AssignClusters", N * P);
        Task clusterPartitioner = addTask("ClusterPartitioner");
        Task[] oneClusterSSE = addTasks("OneClusterSSE", K);
        Task allClusterSSE = addTask("AllClusterSSE");
        Task clusteringResultWriter = addTask("ClusteringResultWriter");

        // ------ IO edges ------ //
        // input
        addEdge(0, featurePartitioner, 0);
        // clustering assignments
        addEdge(clusteringResultWriter, 0);
        // SSE (for all clusters and total)
        addEdge(allClusterSSE, 1);

        // ------ Inter-Task Edges ------ //
        // from FeaturePartitioner to each instance of NormalizeData
        for (int f = 0;  f < F; f++) addEdge(featurePartitioner, f, normalizeData[f], 0);
        // from NormalizeData to InitCentroids
        for (int f = 0; f < F; f++) addEdge(normalizeData[f], 0, initCentroids, f);
//        // from InitCentroids to the initial round of AssignClusters
        for (int p = 0; p < P; p++) {
            addEdge(initCentroids, p, initialAssignClusters[p], 0);  // partition matrix
            addEdge(initCentroids, P, initialAssignClusters[p], 1);  // centroid matrix
        }

        // from the initial AssignClusters to the 0th UpdateCentroids
        for (int p = 0; p < P; p++) addEdge(initialAssignClusters[p], 0, updateCentroids[0], p);

        // edges between UpdateCentroids and AssignClusters for all iterations (except initial)
        for (int n = 0; n < N; n++) {
            // between UpdateCentroids and AssignClusters for each iteration
            for (int p = 0; p < P; p++) {
                // [0-> (P-1)]: specific individual partition
                addEdge(updateCentroids[n], p, iterAssignClusters[n * P + p], 0);  // partition set
                // [P]: index of cetroid list output port (constant)
                addEdge(updateCentroids[n], P, iterAssignClusters[n * P + p], 1);  // centroid list
            }
        }
        // from AssignClusters back to UpdateCentroids
        for (int n = 0; n < N - 1; n++) {
            for (int p = 0; p < P; p++) {
                addEdge(iterAssignClusters[n * P + p], 0, updateCentroids[n + 1], p);
            }
        }

        // from final iteration of AssignClusters to ClusterPartitioner
        // P * (N - 1): The first instance of AssignClusters in the final iteration of AssignClusters
        for (int p = P * (N - 1); p < P * N; p++)
            addEdge(iterAssignClusters[p], 0, clusterPartitioner, p % P);
        // from ClusterPartitioner to each instance of OneClusterSSE plus ClusteringResultWriter
        for (int k = 0; k < K; k++) {
            addEdge(clusterPartitioner, 2 * k, clusteringResultWriter, k);  // data points
            addEdge(clusterPartitioner, 2 * k, oneClusterSSE[k], 0); // data points
            addEdge(clusterPartitioner, 2 * k + 1, oneClusterSSE[k], 1);  // centroids
        }

        // OneClusterSSE to AllClusterSSE
        for (int k = 0; k < K; k++) {
            addEdge(oneClusterSSE[k], 0, allClusterSSE, 2 * k);  // SSE value
            addEdge(oneClusterSSE[k], 1, allClusterSSE, 2 * k + 1);  // cluster cardinality
        }
    }
}
