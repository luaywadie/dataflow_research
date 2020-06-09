import dataview.models.*;

/**
 * Partitions a matrix in to {@link KMeansClustering#P} partitons that are approximately equal in size.
 * While {@link DATAVIEW_MathMatrix#partitionByRow(int)} already does this, it is
 * is written as a separate task to enforce the separation of concerns within the workflow.
 *
 * Inputs: 1 {@link DATAVIEW_MathMatrix}s to be partitioned.
 * Outputs: {@link KMeansClustering#P} approximately equal sized {@link DATAVIEW_MathMatrix}s.
 */
public class RowPartitioner extends Task {
    public RowPartitioner() {
        super("Row Partitioner", "Partitions a matrix in to other matrices, splitting by row");

        ins = new InputPort[1];
        ins[0] = new InputPort("Input Matrix", Port.DATAVIEW_MathMatrix, "The matrix to be partitioned");

        outs = new OutputPort[KMeansClustering.P];
        for (int p = 0; p < KMeansClustering.P; p++) {
            outs[p] = new OutputPort("Partition " + (p + 1), Port.DATAVIEW_MathMatrix, "One partition of the entire data set");
        }
    }

    @Override
    public void run() {
        DATAVIEW_MathMatrix fullMatrix = (DATAVIEW_MathMatrix) ins[0].read();

        DATAVIEW_MathMatrix[] partitions = fullMatrix.partitionByRow(KMeansClustering.P);

        for (int p = 0; p < KMeansClustering.P; p++) {
            outs[p].write(partitions[p]);
        }
    }
}
