import dataview.models.*;
import java.lang.Math;
import java.util.ArrayList;

/**
 * ===============================================
 * This task will assign each data point to its closest cluster / centroid.
 * ===============================================
 * Input(s): [0] => Partition[i] from initCentroids | [1] => Centroids from initCentroids.
 * Output(s): Data points with an added last column for their assigned cluster number.
 * ===============================================
 */

public class AssignClusters extends Task {

    public AssignClusters() {
        super("AssignClusters","Assign cluster numbers to data points closest to it.");

        // Setup ports
        ins = new InputPort[2];
        outs = new OutputPort[1];

        // Assign ports
        ins[0] = new InputPort("Partition", Port.DATAVIEW_MathMatrix, "Partition from initCentroids");
        ins[1] = new InputPort("Centroids", Port.DATAVIEW_MathMatrix, "Centroids from initCentroids");
        outs[0] = new OutputPort("Assigned Data Points", Port.DATAVIEW_MathMatrix, "Data points with assigned cluster");
    }

    @Override
    public void run() {
        // Read in data
        DATAVIEW_MathMatrix partition = (DATAVIEW_MathMatrix) ins[0].read();
        DATAVIEW_MathMatrix centroids = (DATAVIEW_MathMatrix) ins[1].read();
        ArrayList<Integer> assigned_centroids = new ArrayList<>();

        // Loop over each data point in partition
        for (int row = 0; row < partition.getNumOfRows(); row++) {
            // Temp variables
            int closestCentroid = 1;
            double currentTotal = 0.0;
            // Loop over every centroid
            for ( int centroid = 0; centroid < centroids.getNumOfRows(); centroid++) {
                // Temp variables
                double total = 0.0;
                // Loop over every column
                for (int column = 0; column < centroids.getNumOfColumns(); column++) {
                    total += Math.pow(partition.get(row, column) - (centroids.get(centroid, column)), 2);
                }
                total = Math.sqrt(total);
                // Check if first iter-run, assign current euclidean distance
                if (centroid == 0) {
                    currentTotal = total;
                } else {
                    // Else check if new value is smaller than before which indicates its closer to this centroid.
                    if (currentTotal > total) {
                        currentTotal = total;
                        closestCentroid = centroid + 1;
                    }
                }
            }
            assigned_centroids.add(closestCentroid);
        }

        // Final partition to merge everything together
        DATAVIEW_MathMatrix final_partition = new DATAVIEW_MathMatrix(partition.getNumOfRows(),partition.getNumOfColumns() + 1);
        for (int row = 0; row < partition.getNumOfRows(); row++) {
            for (int column = 0; column < partition.getNumOfColumns(); column++) {
                final_partition.set(row, column, partition.get(row, column));
            }
            final_partition.set(row,partition.getNumOfColumns(), assigned_centroids.get(row));
        }

        // Final output to write
        outs[0].write(final_partition);
    }
}
