import dataview.models.*;

/**
 * Create a matrix by appending column vectors.
 * While {@link DATAVIEW_MathMatrix#DATAVIEW_MathMatrix(DATAVIEW_MathVector[])} already does this, it is
 * is written as a separate task to enforce the separation of concerns within the workflow.
 *
 * Inputs: {@link KMeansClustering#F} {@link DATAVIEW_MathVector}s, which are to be the columns of the matrix.
 * Outputs: 1 {@link DATAVIEW_MathMatrix}.
 */
public class ColumnsToMatrix extends Task {
    public ColumnsToMatrix() {
        super("Columns to Matrix", "Given column vectors, form a matrix");

        ins = new InputPort[KMeansClustering.F];
        for (int f  = 0; f < KMeansClustering.F; f++) {
            ins[f] = new InputPort("Column Vector" + (f + 1), Port.DATAVIEW_MathVector, "A column vector");
        }

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Matrix", Port.DATAVIEW_MathMatrix, "The resulting matrix");
    }

    @Override
    public void run() {
        DATAVIEW_MathVector[] columns = new DATAVIEW_MathVector[ins.length];
        for (int f = 0; f < KMeansClustering.F; f++) {
            columns[f] = (DATAVIEW_MathVector) ins[f].read();
        }

        outs[0].write(new DATAVIEW_MathMatrix(columns));
    }
}
