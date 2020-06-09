import dataview.models.*;

/**
 * Combines normalized feature vector in to one matrix, partitions this matrix to be clustered on
 * parallel instances of AssignClusters, and randomly initializes the initial cluster centroids.
 * Instances: 1
 * Input [0 -> (KMeansClustering.F-1)] – DATAVIEW_MathVector: Normalized feature vectors.
 *     From: PlanetaryClusterV2.F instances of NormalizeData
 * Output [0 -> KMeansClustering.P - 1] – DATAVIEW_MathMatrix: Partitions of the normalized matrix.
 *     To: One partition goes to one instance of AssignClusters.
 * Output [KMeansClustering.P] – DATAVIEW_MathMatrix: KMeansClustering.K cluster centroids.
 *     To: Each instance of AssignClusters.
 */
public class InitCentroids extends Task {
    public InitCentroids() {
        super("K-Means Initializer", "Initializes the K-Means algorithm");
        ins = new InputPort[1];
        ins[0] = new InputPort("Dataset", Port.DATAVIEW_MathMatrix,"Full data set to initialize centroids");

        // outs are the P partitions, plus a matrix of the centroids
        outs = new OutputPort[1]; // randomly initialized centroid matrix
        outs[0] = new OutputPort("Centroids", Port.DATAVIEW_MathMatrix, "Matrix where rows are centroids.");
    }

    @Override
    public void run() {
        DATAVIEW_MathMatrix data = (DATAVIEW_MathMatrix)ins[0].read();

        // define initial centroids; K centroids with F dimensions
        double[][] centroids = new double[KMeansClustering.K][KMeansClustering.F];
        for (int k = 0; k < KMeansClustering.K; k++) {
            int randomRow = (int)(Math.random() * data.getNumOfRows());
            for (int f = 0; f < KMeansClustering.F; f++) {
                centroids[k][f] = data.get(randomRow,f);
            }
        }
        DATAVIEW_MathMatrix centroidMatrix = new DATAVIEW_MathMatrix(centroids);
        outs[0].write(centroidMatrix);
    }
}
