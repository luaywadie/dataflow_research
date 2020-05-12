import dataview.models.*;

/**
 * Partitions the test data set so that clustering on these points by the model may be done in parallel.
 */
public class TestDataPartitioner extends Task {
    public TestDataPartitioner() {
        super("TestDataPartitioner", "Partitions the data set to be processed on multiple VMs in parallel.");

        ins = new InputPort[1];
        ins[0] = new InputPort("Test Data Set", Port.DATAVIEW_Table, "The test data (data we want to cluster).");

        outs = new OutputPort[PlanetaryClustering.numClusteringTasks];
        for (int i = 0; i < PlanetaryClustering.numClusteringTasks; i++) {
            outs[i] = new OutputPort("Partition " + i, Port.DATAVIEW_Table, "A partition of the training data set");
        }
    }

    public void run() {
        DATAVIEW_Table data = (DATAVIEW_Table) ins[0].read();
        int numSamples = data.getNumOfRows();
        int numFeatures = data.getNumOfColumns();

        System.out.println("INPUT DATA:\n" + data.toString());

        System.out.println("TestDataPartitioner: reading in data set with "+
                numSamples + " samples and " + numFeatures + " features");

        // TODO TEST: try having the number of clustering tasks gt, eq, lt number of samples
        if (PlanetaryClustering.numClusteringTasks > numSamples) {
            String errorMsg = String.format("Number of training samples (%d) is less than " +
                    "number of clustering instances (%d).", numSamples, PlanetaryClustering.numClusteringTasks);
            throw new IllegalArgumentException(errorMsg);
        }

        int rowsPerPartition = numSamples / PlanetaryClustering.numClusteringTasks;
        // if no remainder, then the number of samples is equal in each cluster;
        // otherwise, we have a remainder so we need to add one to not leave any data points out
        if (numSamples % PlanetaryClustering.numClusteringTasks != 0) {rowsPerPartition++;}

        // create partitions one at a time
        for (int partition = 0; partition < PlanetaryClustering.numClusteringTasks; partition++) {
            int startRow = partition * rowsPerPartition;
            int endRow = (partition + 1) * rowsPerPartition;
            // TODO TEST: make sure that this works correctly both when the partition is full and not full
            if (endRow > numSamples) endRow = numSamples;  // if there are not enough samples to fill final partition

            StringBuilder thisPartitionTable = new StringBuilder();
            for (int row = startRow; row < endRow; row++) {
                thisPartitionTable.append(String.join(",", data.getRow(row)) + "\n");
            }
            outs[partition].write(thisPartitionTable);
        }
    }
}
