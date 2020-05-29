import dataview.models.*;

public class PlanetaryClusteringV2 extends Workflow {
    private final String INPUT_FILE = "planetary_data_IN.csv";
    private final String OUTPUT_FILE = "planetary_data_OUT.csv";

    // constants
    public static final int K = 2;  // number of clusters
    public static final int P = 4; // how many partitions to divide data in to
    public static final int N = 3; // number of iterations to run for (may not converge)
    public static final int F = 7; // number of features in the data set

    public PlanetaryClusteringV2() {
        super("PlanetaryClustering", "Use K-Means to group similar planets");

        wins = new DATAVIEW_BigFile[1];
        wins[0] = new DATAVIEW_BigFile(INPUT_FILE);

        wouts = new DATAVIEW_BigFile[P + 1];
        for (int i = 0; i < P+1; i++) {
            wouts[i] = new DATAVIEW_BigFile(i + "TestOut");
        }
//        wouts = new DATAVIEW_BigFile[1];
//        wouts[0] = new DATAVIEW_BigFile(OUTPUT_FILE);
    }

    public void design() {
        // -----create tasks-----//
        Task featurePartitioner = addTask("FeaturePartitioner");
        Task[] normalizeData = addTasks("NormalizeData", F);
        Task initCentroids = addTask("InitCentroids");
        /* The set of AssignCluster tasks used directly after InitCentroids.
         * Just makes indexing of tasks for edges a lot easier. */
        //Task[] initialAssignClusters = addTasks("AssignClusters", P);
        /* Tasks below this line have yet to be implemented */
//        Task[] updateCentroids = addTasks("UpdateCentroids", N);
//        /* The set of AssignCluster tasks used in the iterations. */
//        Task[] iterAssignClusters = addTasks("AssignClusters", N * P);
//        Task clusteringResultWriter = addTask("ClusteringResultWriter");

        // -----connect tasks ----- //
        addEdge(0, featurePartitioner);  // input data
        // addEdge(clusteringResultWriter, 0);  // output data

        // from FeaturePartitioner to each instance of NormalizeData
        for (int f = 0;  f < F; f++) addEdge(featurePartitioner, f, normalizeData[f], 0);
        // from NormalizeData to InitCentroids
        for (int f = 0; f < F; f++) addEdge(normalizeData[f], 0, initCentroids, f);
        // from InitCentroids to the initial round of AssignClusters
//        for (int p = 0; p < P; p++) {
//            addEdge(initCentroids, p, initialAssignClusters[p], 0);  // partition matrix
//            addEdge(initCentroids, P, initialAssignClusters[p], 1);  // centroid matrix
//        }

        /* ABOVE THIS LINE: tasks that are confirmed working (though edges may be incorrect) */

        /* BELOW THIS LINE: the next thing being tested */
        for (int i = 0; i < wouts.length; i++) {
            addEdge(initCentroids, i, i);
        }

        /* BELOW THIS LINE: things that have yet to be implemented/tested */

//        // from the initial AssignClusters to the 0th UpdateCentroids
//        for (int p = 0; p < P; p++) addEdge(initialAssignClusters[p], 0, updateCentroids[0], p);
//
//        // edges between UpdateCentroids and AssignClusters for all iterations (except initial)
//        for (int n = 0; n < N; n++) {
//            // between UpdateCentroids and AssignClusters for each iteration
//            for (int p = 0; p < P; p++) {
//                // [0-> (P-1)]: specific individual partition
//                addEdge(updateCentroids[n], p, iterAssignClusters[n * P + p], 0);  // partition set
//                // [P]: index of cetroid list output port (constant)
//                addEdge(updateCentroids[n], P, iterAssignClusters[n * P + p], 1);  // centroid list
//            }
//        }
//        // from AssignClusters back to UpdateCentroids
//        for (int n = 0; n < N - 1; n++) {
//            for (int p = 0; p < P; p++) addEdge(iterAssignClusters[n * P + p], 0, updateCentroids[n + 1], p);
//        }
//
//        // from final iteration of AssignClusters to ClusteringResultWriter
//        // P * (N - 1): The first instance of AssignClusters in the last iteration of AssignClusters
//        for (int i = P * (N - 1); i < P * N; i++)
//            addEdge(iterAssignClusters[i], 0, clusteringResultWriter, i % P);
    }
}
