import dataview.models.Task;
import dataview.models.Port;
import dataview.models.InputPort;
import dataview.models.OutputPort;

/**
 * Combines all of the clustering results from the different partitions in to one output file.
 */
public class ClusteringResultWriter extends Task {
    
	public int numClusteringTasks = PlanetaryClustering.numClusteringTasks;
	
	public ClusteringResultWriter() {
		super("ClusteringResultWriter",
		"This task will output to a file the test data entries and their predicted centroids (Clusters)");
			
		// Setup Ports
		ins = new InputPort[numClusteringTasks];
		outs = new OutputPort[1];
		
		// Assign Ports
		for (int i = 0; i < numClusteringTasks; i++) { 
			ins[i] = new InputPort("Prediction from Partition " + i, Port.DATAVIEW_String, "Prediction from a single partition task");
		}
		outs[0] = new OutputPort("Prediction from all Partitions", Port.DATAVIEW_String, "Final prediction results");
	}
	
	public void run() {
    	// Variables
		int i;

		// Instantiate StringBuilder 
		StringBuilder sb = new StringBuilder();
    	
		// Generate CSV header
		sb.append("index,clusterNumber,mass,diameter,surface_temperature,pctg_oxygen,"
				+ "pctg_helium,pctg_iron,pctg_nickel,pctg_silicon,pctg_aluminum,pctg_calcium,"
				+ "pctg_sodium,pctg_potassium,pctg_magnesium,pctg_other\n");
		
		// Append the data read in from different partitions
		for (i = 0; i < numClusteringTasks; i++) {
			sb.append(ins[i].read());
		}
		
		// Output the CSV formatted predicted results
		outs[0].write(sb.toString());
    }
}
