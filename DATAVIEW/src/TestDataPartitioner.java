import dataview.models.*;

/**
 * ===============================================
 * This Task distributes the data within the Test CSV to RunClustering equally.
 * ===============================================
 * Input(s): Testing CSV data of entries that need to be classified / assigned
 * Output(s): Equal distribution of data to each RunClustering Task for processing and assigning.
 * ===============================================
 */
public class TestDataPartitioner extends Task {
    public TestDataPartitioner() {
        super("TestDataPartitioner", "Partitions the data set to be processed on multiple VMs in parallel.");
        // Setup Ports
        ins = new InputPort[1];
        outs = new OutputPort[PlanetaryClustering.numClusteringTasks];

        // Assign Ports
        ins[0] = new InputPort("Test Data Set", Port.DATAVIEW_Table, "The test data (data we want to cluster).");
        for (int i = 0; i < PlanetaryClustering.numClusteringTasks; i++) {
            outs[i] = new OutputPort("Partition " + i, Port.DATAVIEW_Table, "A partition of the training data set");
        }
    }

    public void run() {
        // Read in the Test Data CSV file
        DATAVIEW_Table data = (DATAVIEW_Table) ins[0].read();
        // Get the number of data rows
        int numSamples = data.getNumOfRows();
        // Get the number of data columns
        int numFeatures = data.getNumOfColumns();

        System.out.println("TestDataPartitioner: reading in data set with "+
                numSamples + " samples and " + numFeatures + " features");  // -1 for header row
        // Check to see if assigned RunClustering Task count is greater than data samples,
        // if so we can't allocate an equal amount of data between each Task.
        if (PlanetaryClustering.numClusteringTasks > numSamples) {
            String errorMsg = String.format("Number of test samples (%d) is less than " +
                    "number of clustering instances (%d).", numSamples, PlanetaryClustering.numClusteringTasks);
            throw new IllegalArgumentException(errorMsg);
        }
        // Calculate the number of data samples to be distributed between the Tasks (RunClustering)
        int rowsPerPartition = numSamples / PlanetaryClustering.numClusteringTasks;
        // If no remainder, then the number of samples is equal in each cluster;
        // Otherwise, we have a remainder so we need to add one to not leave any data points out
        if (numSamples % PlanetaryClustering.numClusteringTasks != 0) {rowsPerPartition++;}

        // Create partitions one at a time
        for (int partition = 0; partition < PlanetaryClustering.numClusteringTasks; partition++) {
            // Get the starting row
            int startRow = partition * rowsPerPartition;
            // Get the ending row
            int endRow = (partition + 1) * rowsPerPartition;
            // TODO TEST: make sure that this works correctly both when the partition is full and not full
            // Distribute the remaining samples if their less than the current endRow (Out of bound handling)
            if (endRow > numSamples) endRow = numSamples;
            // Create a new string to store our data range (startRow < = > EndRow)
            StringBuilder thisPartitionTable = new StringBuilder();
            // Loop starting from the current startRow up until the last row for this designated partition
            for (int row = startRow; row < endRow; row++) {
                // Append to the string builder previously created, separating the data by colons for DATAVIEW_Table formatting.
                thisPartitionTable.append(String.join(":", data.getRow(row)) + "\n");
            }
            // Set the output port for each specific RunClustering Task instantiated.
            outs[partition].write(thisPartitionTable);
        }
    }
}
