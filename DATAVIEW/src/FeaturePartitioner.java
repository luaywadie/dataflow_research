import dataview.models.*;

import java.util.ArrayList;

/**
 * ===============================================
 * This task input our train csv data and output each column into NormalizeData task.
 * ===============================================
 * Input(s): Training CSV Data to input into the model
 * Output(s): Each column to be normalized in NormalizeData
 * ===============================================
*/

public class FeaturePartitioner extends Task {

    public static int numbOfFeatures = PlanetaryClusteringV2.F;

    public FeaturePartitioner() {
        super("FeaturePartitioner",
                "Task will parition each column by itself and output each one to its NormalizeData accordingly.");

        // Setup ports
        ins = new InputPort[1];
        outs = new OutputPort[numbOfFeatures];

        // Assign ports
        ins[0] = new InputPort("data", Port.DATAVIEW_MathMatrix, "This is our input data");
        for (int i = 0; i < numbOfFeatures; i++) {
            outs[i] = new OutputPort("dataColumn", Port.DATAVIEW_MathMatrix, "A feature / column from the data inputted");
        }
    }

    @Override
    public void run() {
        // Variables
        DATAVIEW_MathMatrix source = (DATAVIEW_MathMatrix)ins[0].read();
        // Temp-Storage
        ArrayList<DATAVIEW_MathVector> columns = new ArrayList<>();

        // Get each column separately, parse it into a DATAVIEW_MathMatrix object
        for (int column = 0; column < source.getNumOfColumns(); column++) {
            DATAVIEW_MathVector current_column = new DATAVIEW_MathVector(source.getNumOfRows());
            for (int row = 0; row < source.getNumOfRows(); row++) {
                current_column.set(row, source.get(row,column));
            }
            columns.add(current_column);
        }

        // Output each column inside columns to its corresponding output port
        for (int i = 0; i < columns.size(); i++) {
            outs[i].write(columns.get(i));
        }
    }
}
