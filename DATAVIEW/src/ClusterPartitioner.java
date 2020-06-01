import dataview.models.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ClusterPartitioner extends Task {
    public ClusterPartitioner() {
        super("Cluster Partitioner", "Separates data set by assigned cluster number");

        ins = new InputPort[PlanetaryClusteringV2.P];
        for (int p = 0; p < ins.length; p++) {
            ins[p] = new InputPort("Partition " + p, Port.DATAVIEW_MathMatrix, "One partition of the data set");
        }

        outs = new OutputPort[PlanetaryClusteringV2.K * 2];
        for (int k = 0; k < PlanetaryClusteringV2.K; k++) {
            // +1 to k because cluster labels start from 1, not 0
            outs[2 * k] = new OutputPort("Cluster " + (k + 1), Port.DATAVIEW_MathMatrix,"All samples in Cluster no. " + (k + 1));
            outs[2 * k + 1] = new OutputPort("Centroid " + (k + 1), Port.DATAVIEW_MathVector, "The centroid of cluster no. " + (k + 1));
        }
    }

    @Override
    public void run() {
        // read in data
        DATAVIEW_MathMatrix[] incomingPartitions = new DATAVIEW_MathMatrix[ins.length];
        for (int p = 0; p < incomingPartitions.length; p++) {
            incomingPartitions[p] = (DATAVIEW_MathMatrix) ins[p].read();
        }

        // combine all matrices in to one
        DATAVIEW_MathMatrix fullMatrix = DATAVIEW_MathMatrix.concatenate(incomingPartitions);

        /* Figure out which rows need to be copied to which cluster partition by keep track of row indices for each cluster number */
        // map the cluster number to the row indices in which they appear in the fullMatrix
        HashMap<Integer, ArrayList<Integer>> clusterIndices = new HashMap<>(PlanetaryClusteringV2.K, 1.0f); // key: cluster no.; value: list of row indices that are in that cluster
        // initialize the HashMap
        int initCapacity = (int) .5 * (fullMatrix.getNumOfRows() / PlanetaryClusteringV2.K);
        for (int cluster = 1; cluster <= PlanetaryClusteringV2.K; cluster++) {
            clusterIndices.put(cluster, new ArrayList<>(initCapacity));
        }
        int clusterCol = fullMatrix.getNumOfColumns() - 1; // the index for the column containing the cluster label
        for (int row = 0; row < fullMatrix.getNumOfRows(); row++) {
            int clusterNo = (int) fullMatrix.get(row, clusterCol);  // the cluster assigned to the row as an int
            clusterIndices.get(clusterNo).add(row);  // add the row index to the array list for that cluster
        }

        // create a DATAVIEW_MathMatrix for each cluster, find its centroid and write the results
        for (int k = 1; k <= PlanetaryClusteringV2.K; k++) {
            ArrayList<Integer> rowsToCopy = clusterIndices.get(k);
            int numRows = rowsToCopy.size();
            DATAVIEW_MathMatrix clusterPartition = new DATAVIEW_MathMatrix(numRows, fullMatrix.getNumOfColumns());
            for (int row = 0; row < numRows; row++) {
                clusterPartition.setRow(row, fullMatrix.getRow(rowsToCopy.get(row)));  // retrieve the index row to be copied from the stored ArrayList
            }
            // removing the cluster number column, which is no longer required and inhibits vector operations
            clusterPartition = DATAVIEW_MathMatrix.dropColumn(clusterPartition.getNumOfColumns() - 1, clusterPartition);
            // write all points in the entire cluster
            outs[2 * (k - 1)].write(clusterPartition);  // k - 1 since clusters are not 0 indexed
            // pass on the centroid of the cluster
            outs[2 * k - 1].write(calculateCentroid(clusterPartition));
        }
    }

    /**
     *
     * @param clusterData A DATAVIEW_MathMatrix whose points ALL belong to a single cluster; final column in cluster number
     * @return
     */
    // TODO: TEST THIS
    private DATAVIEW_MathVector calculateCentroid(DATAVIEW_MathMatrix clusterData) {
        DATAVIEW_MathVector centroid = new DATAVIEW_MathVector(PlanetaryClusteringV2.F);

        for (int col = 0; col < PlanetaryClusteringV2.F; col++) {
            centroid.set(col, clusterData.getCol(col).sum());  // sum
            centroid.divide(col, clusterData.getNumOfRows());  // divide to average
        }
        return centroid;
    }
}
