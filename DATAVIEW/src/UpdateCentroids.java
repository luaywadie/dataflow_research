import dataview.models.*;

/**
 * ===============================================
 * This task input our train csv data and output each column into NormalizeData task.
 * ===============================================
 * Input(s): Training Data to input into the model
 * Output(s): Each column to be normalized in NormalizeData
 * ===============================================
 */

public class UpdateCentroids extends Task {
    public UpdateCentroids() {
        super("UpdateCentroids",
                "This task will calculate the average value for each feature and set it as the new centroid");
        // Setup ports
        ins = new InputPort[KMeansClustering.P];  // inputs are partitions WITH assigned cluster as final column
        outs = new OutputPort[KMeansClustering.P + 1];  // output P partitions, and also a matrix containing cluster centroids
        // Assign Ports
        for (int i = 0; i < KMeansClustering.P; i++) {
            ins[i] = new InputPort("partition", Port.DATAVIEW_MathMatrix, "This is our input partition (data) at index i");
        }
        for (int i = 0; i < KMeansClustering.P; i++) {
            outs[i] = new OutputPort("partition", Port.DATAVIEW_MathMatrix, "This is our output partition (data) at index i");
        }
        outs[KMeansClustering.P] = new OutputPort("centroids", Port.DATAVIEW_MathMatrix, "Newly calculated centroids");
    }

    @Override
    public void run() {
        // put all paritions in to array so they can be concatenated
        DATAVIEW_MathMatrix[] partitionsWithCluster = new DATAVIEW_MathMatrix[KMeansClustering.P];
        for (int p = 0; p < ins.length; p++) {
            partitionsWithCluster[p] = ((DATAVIEW_MathMatrix) ins[p].read());
        }
        DATAVIEW_MathMatrix combinedPartitions = DATAVIEW_MathMatrix.concatenate(partitionsWithCluster);
        // Create the new centroids and the partitions list WITHOUT the cluster assigned column (because it is not relevant after centroids are updated)
        DATAVIEW_MathMatrix newCentroids = new DATAVIEW_MathMatrix(KMeansClustering.K, KMeansClustering.F);
        DATAVIEW_MathMatrix partitionsNoCluster = new DATAVIEW_MathMatrix(combinedPartitions.getNumOfRows(), KMeansClustering.F);
        // Get the count of data points within each centroid
        DATAVIEW_MathVector centroidDataCount = new DATAVIEW_MathVector(KMeansClustering.K);

        // Loop over every partition and append the centroid row it was assigned to
        for (int row = 0; row < combinedPartitions.getNumOfRows(); row++) {
            // grab the cluster number and decrement by 1 to index in to newCentroids matrix
            int clusterIndex = (int) combinedPartitions.getRow(row).get(combinedPartitions.getNumOfColumns() - 1) - 1;
            // Increment the index of centroidDataCount corresponding to the centroid
            double oldValue = centroidDataCount.get(clusterIndex);
            centroidDataCount.set(clusterIndex, oldValue + 1.0);
            // sum values for each column; will be averaged after all values have been summed
            // can't use DATAVIEW_MathMatrix.addRow() because we need to drop the final column (which is just the cluster number)
            for (int column = 0; column < combinedPartitions.getNumOfColumns() - 1; column++) {
                // hold previous value temporarily so that we can update it
                double previousValue = newCentroids.getRow(clusterIndex).get(column);
                newCentroids.set(clusterIndex, column, previousValue + combinedPartitions.getRow(row).get(column));
                partitionsNoCluster.set(row, column, combinedPartitions.get(row, column));
            }
        }

        // Divide the centroids by total number of data points to get the average for every feature
        newCentroids.div(combinedPartitions.getNumOfRows());
        for (int i = 0; i < newCentroids.getNumOfRows(); i++) {
            newCentroids.getRow(i).divide(centroidDataCount.get(i));
        }

        // Write outs
        // Partitions to write out =>
        DATAVIEW_MathMatrix[] partitions = partitionsNoCluster.partitionByRow(KMeansClustering.P);
        for (int p = 0; p < KMeansClustering.P; p++) {
            outs[p].write(partitions[p]);
        }

        // Write out centroids
        outs[outs.length - 1].write(newCentroids);
    }
}
