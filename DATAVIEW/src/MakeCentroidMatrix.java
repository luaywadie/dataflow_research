import dataview.models.*;

public class MakeCentroidMatrix extends Task {
    public MakeCentroidMatrix() {
        super("Make Centroid Matrix", "Creates a matrix that consists of centroids.");

        ins = new InputPort[KMeansClustering.K];
        for (int k = 0; k < KMeansClustering.K; k++) {
            ins[k] = new InputPort("Cluster Centroid " + (k + 1), Port.DATAVIEW_MathVector, "The centroid of cluster " + (k + 1));
        }

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Centroid Matrix", Port.DATAVIEW_MathMatrix, "Matrix whose rows are cluster centroids.");
    }

    @Override
    public void run() {
        DATAVIEW_MathVector[] centroids = new DATAVIEW_MathVector[ins.length];
        for (int c = 0; c < centroids.length; c++) {
            centroids[c] = (DATAVIEW_MathVector) ins[c].read();
        }

        DATAVIEW_MathMatrix centroidMatrix = DATAVIEW_MathMatrix.fromRowVectors(centroids);
        outs[0].write(centroidMatrix);
    }
}
