import dataview.models.Task;
import dataview.models.Workflow;

public class PlanetCluster extends Workflow {
    public PlanetCluster() {
        super("Planet Cluster", "Clusters different planetary bodies based on various" +
                " measurements.");
    }

    public void design() {
        // tasks
        Task trainTestSplit = addTask("TrainTestSplit");
        Task[] addHeaders = addTasks("AddHeader", 2);
        //Task performClustering =

        // edges
    }

    @Override
    public void run() {

    }
}
