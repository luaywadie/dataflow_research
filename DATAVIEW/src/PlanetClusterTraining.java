import dataview.models.InputPort;
import dataview.models.OutputPort;
import dataview.models.Port;
import dataview.models.Task;
import dataview.models.Dataview;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class PlanetClusterTraining extends Task {

  public PlanetClusterTraining() {
    super("PlanetClusterTraining",
    "This task will execute SimpleKMeans from Weka onto our data");
    // Setup ports
    ins = new InputPort[1];
    outs = new OutputPort[1];
    // Assign ports
    ins[0] = new InputPort("data", Port.DATAVIEW_Table, "This is our data input");
    outs[0] = new OutputPort("centroids", Port.DATAVIEW_Table, "This is the return value from SimpleKMeans Clustering");
  }

  @Override
  public void run() {
	  Dataview.debugger.logErrorMessage("Error");
  }
}
