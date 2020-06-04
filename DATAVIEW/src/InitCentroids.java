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

        // ins are the columns coming from NormalizeData
        ins = new InputPort[KMeansClustering.F];  // takes one input per (normalized) feature
        for (int f = 0; f < KMeansClustering.F; f++) {
            ins[f] = new InputPort("Feature " + f, Port.DATAVIEW_MathVector, "One of the normalized features");
        }

        // outs are the P partitions, plus a matrix of the centroids
        outs = new OutputPort[KMeansClustering.P + 1]; // +1 for centroids
        for (int p = 0; p < KMeansClustering.P; p++) {
            outs[p] = new OutputPort("Partition " + p, Port.DATAVIEW_MathMatrix, "A single partition of the data set");
        }
        outs[outs.length - 1] = new OutputPort("Centroids", Port.DATAVIEW_MathMatrix, "Matrix where rows are centroids.");
    }

    @Override
    public void run() {
        DATAVIEW_MathVector[] features = new DATAVIEW_MathVector[KMeansClustering.F];
        for (int f = 0; f < KMeansClustering.F; f++) {
            features[f] = (DATAVIEW_MathVector) ins[f].read();
        }

        // combine features from normalizer in to a single matrix
        DATAVIEW_MathMatrix data = new DATAVIEW_MathMatrix(features);

        // partition the data set
        DATAVIEW_MathMatrix[] partitions = data.partitionByRow(KMeansClustering.P);
        for (int p = 0; p < KMeansClustering.P; p++) {
            outs[p].write(partitions[p]);
        }

        // define initial centroids; K centroids with F dimensions
        double[][] centroids = new double[KMeansClustering.K][KMeansClustering.F];
        for (int k = 0; k < KMeansClustering.K; k++) {
            int randomRow = (int)(Math.random() * data.getNumOfRows());
            for (int f = 0; f < KMeansClustering.F; f++) {
                centroids[k][f] = data.get(randomRow,f);
            }
        }
        DATAVIEW_MathMatrix centroidMatrix = new DATAVIEW_MathMatrix(centroids);
        outs[outs.length - 1].write(centroidMatrix);
    }
}
