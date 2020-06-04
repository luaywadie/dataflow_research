import dataview.models.*;

/**
 * ===============================================
 * This Task will finalize and combine the results of all clusters from ClusterPartitioner Task.
 * ===============================================
 * Input(s): Data points with assigned cluster number.
 * Output(s): Final combined data set of all the partitioned data points.
 * ===============================================
 */
public class ClusteringResultWriter extends Task {
	public ClusteringResultWriter() {
		super("ClusteringResultWriter",
		"This task will output to a file the test data entries and their predicted centroids (Clusters)");
			
		// Setup Ports
		ins = new InputPort[KMeansClustering.K];
		outs = new OutputPort[1];
		
		// Assign Ports
		for (int i = 0; i < KMeansClustering.K; i++) {
			ins[i] = new InputPort("datapoints", Port.DATAVIEW_MathMatrix, "Data points with assigned cluster");
		}
		outs[0] = new OutputPort("completeDataset", Port.DATAVIEW_MathMatrix, "Entire data set merged into one");
	}
	
	public void run() {
		StringBuilder combinedData = new StringBuilder();
		// Merge the data together
		int rowId = 1;
		for (int i = 0; i < KMeansClustering.K; i++) {
			DATAVIEW_MathMatrix thisCluster = (DATAVIEW_MathMatrix) ins[i].read();
			int thisNumRows = thisCluster.getNumOfRows();
			for (int row = 0 ; row < thisNumRows; row++) {
				// doing on multiple lines because it's too long for all in one line
				// final column i scluster no. as a double, so just trim the ".0" to show as int
				String rowString = thisCluster.getRow(row).toString();
				String trimmedRowString = rowString.substring(0, rowString.length() - 2);
				combinedData.append(rowId++ + ", " + trimmedRowString + "\n");
			}
		}

		System.out.println(combinedData);
		// Write out
		outs[0].write(combinedData);
    }
}
