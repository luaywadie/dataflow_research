import dataview.models.Task;

/**
 * Given PlanetaryClusteringV2.K centroids and a partition of a data set, places each data point in
 * a cluster (using EUCLIDEAN distance).
 * Instances: PlanetaryClusteringV2.N * PlanetaryClusteringV2.P (one for each partition for each iteration)
 * Input [0] – DATAVIEW_MathMatrix: A partition of the entire data set.
 *     From: Either InitCentroids (for initialization) OR UpdateCentroids (for iterations).
 * Input [1] – DATAVIEW_MathMatrix: The cluster centroids.
 *     From: Either InitCentroids (for initialization) OR UpdateCentroids (for iterations).
 * Output [0] – DATAVIEW_MathMatrix: A DATAVIEW_MathMatrix like Input [0], except that there is an additional
 *                                   column at the END of the matrix that gives the cluster the sample has been
 *                                   placed in.
 *    To: The next time step's instance of UpdateCentroids.
 */
public class AssignClusters extends Task {
    @Override
    public void run() {
        //
    }
}
