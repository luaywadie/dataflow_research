import dataview.models.*;

public class PlanetaryClusteringV2 extends Workflow {
    private final String INPUT_FILE = "planetary_data_IN.csv";
    private final String CLUSTER_SSE_FILE_PREFIX = "planetary_clusters_SSE_";
    private final String CLUSTER_RESULT_FILE_PREFIX = "planetary_clusters_result_";
    private final String TOTAL_SSE_FILE = "planetary_clusters_SSE_all";

    // constants
    public static final int K = 3;  // number of clusters
    public static final int P = 4; // how many partitions to divide data in to
    public static final int N = 4; // number of iterations to run for (may not converge)
    public static final int F = 7; // number of features in the data set

    public PlanetaryClusteringV2() {
        super("PlanetaryClustering", "Use K-Means to group similar planets");
        wins = new DATAVIEW_BigFile[1];
        wins[0] = new DATAVIEW_BigFile(INPUT_FILE);

        // output files for the each cluster's data points
        wouts = new DATAVIEW_BigFile[ 2 * K + 1];
        for (int k = 0; k < K; k++) {
            wouts[k] = new DATAVIEW_BigFile(CLUSTER_RESULT_FILE_PREFIX + k);
        }
        // output files for each individual cluster's SSE
        for (int k = K; k < 2 * K; k++) {
            wouts[k] = new DATAVIEW_BigFile(CLUSTER_SSE_FILE_PREFIX + (k % K));
        }
        // output file for ALL cluster's SSEs summed
        wouts[2 * K] = new DATAVIEW_BigFile(TOTAL_SSE_FILE);

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

        // ------ IO edges ------ //
        // input
        addEdge(0, featurePartitioner, 0);

        // output for each cluster's data points; wouts[0..K)
        for (int k = 0; k < K; k++) {
            addEdge(clusterPartitioner, 2 * k, k);
        }
        // outputs of each cluster's individual SSE; wouts[K..2 * K)
        for (int k = K; k < 2 * K; k++) {
            addEdge(oneClusterSSE[k % K], 0, k);
        }
        // output the total SSE amongst ALL clusters; wouts[2 * K]
        addEdge(allClusterSSE, 0, 2 * K);

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
        // from ClusterPartitioner to each instance of OneClusterSSE
        for (int k = 0; k < K; k++) {
            addEdge(clusterPartitioner, 2 * k, oneClusterSSE[k], 0); // data points
            addEdge(clusterPartitioner, 2 * k + 1, oneClusterSSE[k], 1);  // centroids
        }

        // OneClusterSSE to AllClusterSSE
        for (int k = 0; k < K; k++) {
            addEdge(oneClusterSSE[k], 0, allClusterSSE, k);
        }
    }
}
