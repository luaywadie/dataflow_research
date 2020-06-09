import dataview.models.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ClusterPartitioner extends Task {
    public ClusterPartitioner() {
        super("Cluster Partitioner", "Separates data set by assigned cluster number");

        ins = new InputPort[1];
        ins[0] = new InputPort("Dataset", Port.DATAVIEW_MathMatrix,"All dataset entries");

        outs = new OutputPort[KMeansClustering.K];
        for (int k = 0; k < KMeansClustering.K; k++) {
            // +1 to k because cluster labels start from 1, not 0
            outs[k] = new OutputPort("Cluster " + (k + 1), Port.DATAVIEW_MathMatrix,"All samples in Cluster no. " + (k + 1));
        }
    }

    @Override
    public void run() {
        // read in data
        DATAVIEW_MathMatrix fullMatrix = (DATAVIEW_MathMatrix)ins[0].read();

        /* Figure out which rows need to be copied to which cluster partition by keep track of row indices for each cluster number */
        // map the cluster number to the row indices in which they appear in the fullMatrix
        HashMap<Integer, ArrayList<Integer>> clusterIndices = new HashMap<>(KMeansClustering.K, 1.0f); // key: cluster no.; value: list of row indices that are in that cluster
        // initialize the HashMap
        int initCapacity = (int) .5 * (fullMatrix.getNumOfRows() / KMeansClustering.K);
        for (int cluster = 1; cluster <= KMeansClustering.K; cluster++) {
            clusterIndices.put(cluster, new ArrayList<>(initCapacity));
        }
        int clusterColIndex = fullMatrix.getNumOfColumns() - 1; // the index for the column containing the cluster label
        // populate the HashMap array lists with the row numbers that belong to each cluster
        for (int row = 0; row < fullMatrix.getNumOfRows(); row++) {
            int clusterNo = (int) fullMatrix.get(row, clusterColIndex);  // the cluster assigned to the row as an int
            clusterIndices.get(clusterNo).add(row);  // add the row index to the array list for that cluster
        }

        // create a DATAVIEW_MathMatrix for each cluster; find its centroid
        for (int k = 1; k <= KMeansClustering.K; k++) {
            ArrayList<Integer> rowsToCopy = clusterIndices.get(k);  // list of rows that belong to cluster k
            int numRows = rowsToCopy.size();
            DATAVIEW_MathMatrix thisCluster = null;  // the MathMatrix that holds all of the data rows that are in cluster k (w/cluster no. as final column; will be removed when writing)
            if (numRows == 0) {  // the cluster has no points assigned to it; write dummy row to avoid errors
                // k - 1 because k starts at 1 here, rather than 0
                outs[k - 1].write("");
            } else {
                thisCluster = new DATAVIEW_MathMatrix(numRows, fullMatrix.getNumOfColumns());
                for (int i = 0; i < numRows; i++) {
                    thisCluster.setRow(i, fullMatrix.getRow(rowsToCopy.get(i))); // copy the rows from the fullMatrix to the partition
                }
                // write all points in the entire cluster WITH the cluster assignment column
                // k - 1 because k starts at 1 here, rather than 0
                outs[k - 1].write(thisCluster);
            }
        }
    }
}
