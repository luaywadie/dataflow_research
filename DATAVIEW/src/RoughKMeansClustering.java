import dataview.models.DATAVIEW_BigFile;
import dataview.models.Task;
import dataview.models.Workflow;

public class RoughKMeansClustering extends Workflow {
    private final String UNIQUE_ID = "0101_";  // used to easily find output data for a specific run of the workflow
    private final String INPUT_FILE = "seeds_dataset.csv";
    private final String CLUSTER_ASSIGNMENTS_OUTPUT = UNIQUE_ID + "RoughKMeans_Assignments";
    private final String CLUSTER_SET_DIFFERENCE = UNIQUE_ID + "RoughKMeans_Sets";
    private final String CLUSTER_SSE_OUTPUT = UNIQUE_ID + "RoughKMeans_SSEs";

    // constants
    public static final int K = 3;  // number of clusters
    public static final int P = 4; // how many partitions to divide data in to
    public static final int N = 4; // number of iterations to run for (may not converge)
    public static final int F = 7; // number of features in the data set (excluding ID)
    public static final double THRESHOLD = 0.1;  // TODO: how to determine good value for this?



    public RoughKMeansClustering() {
        super("Rough K-Means Clustering", "Use the rough K-Means algorithm to cluster data points.");
        wins = new DATAVIEW_BigFile[1];
        wins[0] = new DATAVIEW_BigFile(INPUT_FILE);

        // TODO: these wouts are outdated, update to reflect new workflow design
        wouts = new DATAVIEW_BigFile[2];
        // JSON representing each cluster and the points that are in its upper/lower bounds
        wouts[0] = new DATAVIEW_BigFile(CLUSTER_ASSIGNMENTS_OUTPUT);
        // JSON representing the set difference between upper/lower bounds for each cluster
        wouts[1] = new DATAVIEW_BigFile(CLUSTER_SET_DIFFERENCE);
        // SSE of each cluster individually, as well as the sum of all of the errors
        wouts[2] = new DATAVIEW_BigFile(CLUSTER_SSE_OUTPUT);
    }

    public void design() {
        // ---- create the tasks ---- //
        // tasks that prepare for the iterative portion
        Task featurePartitioner = addTask("FeaturePartitioner");
        Task[] normalizeData = addTasks("NormalizeData", F);
        Task columnsToMatrix = addTask("ColumnsToMatrix");
        Task initRowPartitioner = addTask("RowPartitioner");
        Task initCentroids = addTask("InitCentroids");

        // tasks that make up the iterative portion of the workflow
        Task[][] roughClusterAssignment = addTasks2D("RoughClusterAssignment", N, P);
        Task[] clusterMerger = addTasks("ClusterMerger", N);
        // N-1 instances because we do not need this task for the final iteration
        Task[] rowPartitioner = addTasks("rowPartitioner", N - 1);
        Task[][] setDifference = addTasks2D("SetDifference", N, K);
        Task[][] roughCalculateCentroid = addTasks2D("RoughCalculateCentroid", N, K);
        // N-1 instances because we do not need this task for the final iteration
        Task[] makeCentroidMatrix = addTasks("MakeCentroidMatrix", N - 1);

        // tasks that handle aggregating and writing the results of the workflow
        Task roughClusterWriter = addTask("RoughClusterWriter");
        Task[] roughClusterSSE = addTasks("RoughClusterSSE", K);
        Task allClusterSSE = addTask("AllClusterSSE");

        // ---- connect the tasks ---- //
        // input //
        addEdge(0, featurePartitioner);
        // output //
        addEdge(roughClusterWriter, 0);
        addEdge(roughClusterWriter, 1);
        addEdge(allClusterSSE, 2);

        // pre-iteration tasks //
        // FeaturePartitioner to NormalizeData
        for(int f = 0; f < F; f++) {
            addEdge(featurePartitioner, f, normalizeData[f], 0);
        }
        // FeaturePartitioner to ColumnsToMatrix
        addEdge(featurePartitioner, F, columnsToMatrix, F);
        // NormalizeData to ColumnsToMatrix
        for (int f = 0; f < F; f++) {
            addEdge(normalizeData[f], 0, columnsToMatrix, f);
        }
        // ColumnsToMatrix to RowPartitioner and InitCentroids
        addEdge(columnsToMatrix, 0, initRowPartitioner, 0);
        addEdge(columnsToMatrix, 0, initCentroids, 0);

        // interface between pre-iterative and iterative tasks (to first iteration) //
        // from RowPartitioner and InitCentroids to RoughClusterAssignment
        for (int p = 0; p < P; p++) {
            addEdge(initRowPartitioner, p, roughClusterAssignment[0][p], 0);
            addEdge(initCentroids, 0, roughClusterAssignment[0][p], 1);
        }

        // iterative tasks //
        for (int n = 0; n < N; n++) {
            // from RoughClusterAssignment to ClusterMerger
            for (int p = 0; p < P; p++) {
                // points in the partition that are in lower bounds of clusters
                addEdge(roughClusterAssignment[n][p], 0, clusterMerger[n], 2 * p);
                // points in the partition that are in upper bounds of clusters
                addEdge(roughClusterAssignment[n][p], 1, clusterMerger[n], 2 * p + 1);
            }
            // from ClusterMerger to SetDifference
            for (int k = 0; k < K; k++) {
                addEdge(clusterMerger[n], 2 * k, setDifference[n][k], 0);
                addEdge(clusterMerger[n], 2 * k + 1, setDifference[n][k], 1);
            }
            // from ClusterMerger to RowPartitioner (only if not the final iteration)
            // TODO: where to send this output after the final iteration??
            if (n != N - 1) {
                addEdge(clusterMerger[n], 2 * K, rowPartitioner[n], 0);
            }
            // from SetDifference to RoughCalculateCentroid
            for (int k = 0; k < K; k++) {
                addEdge(setDifference[n][k], 0, roughCalculateCentroid[n][k], 0);
                addEdge(setDifference[n][k], 1, roughCalculateCentroid[n][k], 1);
            }
            // from RoughCalculateCentroid to MakeCentroidMatrix
            // TODO: where to send this output after the final iteration??
            if (n != N - 1) {
                for (int k = 0; k < K; k++) {
                    addEdge(roughCalculateCentroid[n][k], 0, makeCentroidMatrix[n], k);
                }
            }
            // from MakeCentroidMatrix and RowPartitioner to RoughClusterAssignment (next iteration)
            if (n != N - 1) {
                for (int p = 0; p < P; p++) {
                    addEdge(rowPartitioner[n], p, roughCalculateCentroid[n + 1][p], 0);
                    addEdge(makeCentroidMatrix[n], 0, roughClusterAssignment[n + 1][p], 1);
                }
            }
        }

        // from final iteration to the tasks that write results //
        // from SetDifference to RoughClusterWriter/RoughClusterSSE and RoughCalculateCentroid to RoughClusterSSE
        for (int k = 0; k < K; k++) {
            addEdge(setDifference[N-1][k], 0, roughClusterWriter, 2 * k);
            addEdge(setDifference[N-1][k], 1, roughClusterWriter, 2 * k + 1);
            addEdge(setDifference[N-1][k], 0, roughClusterSSE[k], 0);
            addEdge(setDifference[N-1][k], 1, roughClusterSSE[k], 1);
            addEdge(roughCalculateCentroid[N-1][k], 0, roughClusterSSE[k], 2);
        }
        // from RoughClusterSSE to AllClusterSSE
        for (int k = 0; k < K; k++) {
            addEdge(roughClusterSSE[k], 0, allClusterSSE, k);
        }
    }

    /**
     * Creates a 2D array of homogeneous {@link Task}s.
     * @param taskType The string representing the name of the Class that implements the task.
     * @param rows The number of rows in the array.
     * @param cols The number of columns in the array.
     * @return A 2D array with {@code rows} * {@code cols} instances of {@code taskType}.
     */
    private Task[][] addTasks2D(String taskType, int rows, int cols) {
        Task[][] taskArray = new Task[rows][cols];
        for (int r = 0; r < rows; r++) {
            taskArray[r] = addTasks(taskType, cols);
        }
        return taskArray;
    }

}
