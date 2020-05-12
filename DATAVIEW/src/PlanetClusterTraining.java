import dataview.models.InputPort;
import dataview.models.OutputPort;
import dataview.models.Port;
import dataview.models.Task;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;

public class PlanetClusterTraining extends Task {

  public PlanetClusterTraining() {
    super("PlanetClusterTraining",
    "This task will execute SimpleKMeans from Weka onto our data");
    // Setup ports
    ins = new InputPort[1];
    outs = new OutputPort[1];
    // Assign ports
    ins[0] = new InputPort("data", Port.DATAVIEW_String, "This is our data input");
    outs[0] = new OutputPort("centroids", Port.DATAVIEW_String, "This is the return value from SimpleKMeans Clustering");
  }

  @Override
  public void run() {
	// Variables
	int numClusters = PlanetaryClustering.numClusters;

	// Instantiate CSV Input
	CSVLoader loader = new CSVLoader();

	try {
		// Load the source via input location (File Directory Path)
		loader.setSource(new File((String)ins[0].location));
		// Set headers to null for processing
		loader.setNoHeaderRowPresent(false);
		// Get instances from the data (Instances are data entry, 1 Instance = 1 Data Entry)
		Instances data = loader.getDataSet();
		// Instantiate SimpleKMeans
		SimpleKMeans kmeans = new SimpleKMeans();
		// Set random seed to static (Choose any #)
		kmeans.setSeed(42);
		// Keep data entry orders unchanged
		kmeans.setPreserveInstancesOrder(true);
		// Set number of clusters
		// TODO Automate best # of clusters to use per data set
		kmeans.setNumClusters(numClusters);
		// Construct the clusters
		kmeans.buildClusterer(data);
		// Get centroids from clusterer
		Instances centroids = kmeans.getClusterCentroids();
		// Create StringBuilder
		StringBuilder sb = new StringBuilder();
		// Loop over all centroids and append them into StringBuilder
		int i;
		for (i = 0; i < numClusters; i++) {
			sb.append(centroids.get(i) + "\n");
		}

		// Set output port to StringBuilder
		outs[0].write(sb.toString().replace(',', ':'));  // ':' is used by DATAVIEW_Table

	} catch (Exception e) {
		e.printStackTrace();
	}
  }
}
	