import dataview.models.*;

public class CalculateCentroid extends Task {
    public CalculateCentroid() {
        super("CalculateCentroid",
                "Given a cluster input from ClusterPartitioner, this will return the new centroid.");

        // Setup Ports
        ins = new InputPort[1];
        outs = new OutputPort[1];

        // Assign Ports
        ins[0] = new InputPort("Cluster Data", Port.DATAVIEW_MathMatrix, "Data that pertains to a single cluster");
        outs[0] = new OutputPort("New Cluster Centroid", Port.DATAVIEW_MathVector, "New centroid from the mean value of all data points within it.");
    }

    @Override
    public void run() {
        // Read in data
        DATAVIEW_MathMatrix clusterData = (DATAVIEW_MathMatrix)ins[0].read();
        // remove the cluster assignment column
        clusterData = DATAVIEW_MathMatrix.dropColumn(clusterData.getNumOfColumns() - 1, clusterData);
        DATAVIEW_MathVector calculatedCentroid = new DATAVIEW_MathVector(clusterData.getNumOfColumns());

        // Loop over clusterData and add it to the calculatedCentroid
        for (int row = 0; row < clusterData.getNumOfRows(); row++) {
            for (int column = 0; column < clusterData.getNumOfColumns(); column++) {
                calculatedCentroid.add(column, clusterData.get(row, column));
            }
        }

        // Divide by total amount of data to get the mean
        calculatedCentroid.divide(clusterData.getNumOfRows());

        // Write out the new centroid
        outs[0].write(calculatedCentroid);
    }
}
