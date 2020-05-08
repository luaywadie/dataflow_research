import dataview.models.Task;
import dataview.models.Workflow;

public class PlanetaryClustering extends Workflow {
    // the path to the file which contains the training data
    public final String TRAIN_INPUT = "planetary_data_train.csv";
    // the path to the file with test data (to be clustered)
    public final String TEST_INPUT = "planetary_data_test.csv";
    // the cluster labels for the training data
    public final String CLUSTERING_OUTPUT = "planetary_data_output.csv";

    // the number of clusters for the model to have
    public final int K = 10;
    // the number of individual tasks used to classify new training data
    public final int M = 10;

    PlanetaryClustering() {
        super("Planetary Clustering", "Performs K-Means clustering on planet data");
    }

    public void design() {
        // create tasks
        Task testPartitioner


        // add edges between them
    }
}