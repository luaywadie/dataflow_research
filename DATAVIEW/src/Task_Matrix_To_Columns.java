import dataview.models.*;

/**
 * This class is a Task that is responsible for taking a DATAVIEW_MathMatrix object and parsing it in to its
 * individual column vectors, output as DATAVIEW_MathVectors
 */
public class Task_Matrix_To_Columns extends Task {
    public DATAVIEW_MathMatrix matrix;
    // THIS MUST BE CHANGED TO FIT THE SIZE OF THE MATRIX BEING INPUT
    static final int numOfColumns = 5;
    // number of rows will be determined once file has been converted from Big_File to Math_Matrix
    int numOfRows = 0;

    // declare all I/O ports of the task
    public Task_Matrix_To_Columns() {
        super("Matrix to column vector splitter", "This task takes a matrix and splits it in to column vectors");

        // define input ports
        ins = new InputPort[1];
        ins[0] = new InputPort("in0", Port.DATAVIEW_MathMatrix, "Matrix to be split");
        // define output ports
        outs = new OutputPort[numOfColumns];
        for (int i = 0; i < numOfColumns; i++) {
            outs[i] = new OutputPort("out" + i, Port.DATAVIEW_MathVector, "Column " + (i+1));
        }
    }

    public void run() {
        System.out.println("RUN MAT TO COL");
        // step 1: read data from input ports;
        matrix = (DATAVIEW_MathMatrix) ins[0].read();
        numOfRows = matrix.getNumOfRows();

        // step 2: transform the matrix to individual column vectors, row by row
        for (int col = 0; col < numOfColumns; col++) {
            double[] values = new double[numOfRows];
            for (int row = 0; row < numOfRows; row++) {
                values[row] = matrix.get(row, col);
            }
            //System.out.println(new DATAVIEW_MathVector(values));
            outs[col].write(new DATAVIEW_MathVector(values));
            System.out.println("OUTS COL " + col + "; " + outs[col].location);
        }
    }

}
