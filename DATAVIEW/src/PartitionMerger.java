import dataview.models.*;


public class PartitionMerger extends Task {
    public PartitionMerger() {
        super("PartitionMerger", "Combines partitioned matrix data into a single matrix");

        // Setup Ports
        ins = new InputPort[KMeansClustering.P];
        outs = new OutputPort[2];

        // Assign Ports
        for (int i = 0; i < ins.length; i++) {
            ins[i] = new InputPort("Single Partition Matrix", Port.DATAVIEW_MathMatrix, "A single partition from the previous task");
        }

        outs[0] = new OutputPort("Combined Partition Matrix wo/Assignments", Port.DATAVIEW_MathMatrix, "A complete partition matrix containing all of the input partitions merged WITHOUT cluster assignment column.");
        outs[1] = new OutputPort("Combined Partition Matrix w/assignments", Port.DATAVIEW_MathMatrix, "A complete partition matrix containing all of the input partitions merged WITH cluster assignment column.");
    }

    @Override
    public void run() {
        // Read in the partitions
        DATAVIEW_MathMatrix[] partitions = new DATAVIEW_MathMatrix[ins.length];

        // Loop over the partitions
        for (int i = 0; i < ins.length; i++) {
            partitions[i] = (DATAVIEW_MathMatrix)ins[i].read();
        }

        // Create new MathMatrix to hold all partitions
        DATAVIEW_MathMatrix combinedPartitions = DATAVIEW_MathMatrix.concatenate(partitions);

        // Write out the combined partition
        // without cluster assignments (to be fed to next iteration)
        outs[0].write(DATAVIEW_MathMatrix.dropColumn(combinedPartitions.getNumOfColumns() - 1, combinedPartitions));
        // with cluster assignments (to be fed to the ClusterPartitioner task)
        outs[1].write(combinedPartitions);
    }
}
