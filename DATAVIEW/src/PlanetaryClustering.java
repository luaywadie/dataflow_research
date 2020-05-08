import dataview.models.*;

public class PlanetaryClustering extends Workflow {
    // the path to the file which contains the training data; wins[0]
    public final String TRAIN_INPUT = "planetary_data_train.csv";
    // the path to the file with test data (to be clustered); wins[1]
    public final String TEST_INPUT = "planetary_data_test.csv";
    // the cluster labels for the training data; wouts[0]
    public final String CLUSTERING_OUTPUT = "planetary_data_output.csv";

    // the number of clusters for the model to have
    public static final int numClusters = 5;
    // the number of individual tasks used to classify new training data
    public static final int numClusteringTasks = 3;

    PlanetaryClustering() {
        super("Planetary Clustering", "Performs numClusters-Means clustering on planet data");
        wins = new DATAVIEW_BigFile[2];
        wins[0] = new DATAVIEW_BigFile(TRAIN_INPUT);
        wins[1] = new DATAVIEW_BigFile(TEST_INPUT);

        wouts = new DATAVIEW_BigFile[1];
        wouts[0] = new DATAVIEW_BigFile(CLUSTERING_OUTPUT);
    }

    public void design() {
        // create tasks
        Task planetClusterTrainingTask = addTask("PlanetClusterTraining");
        Task testDataPartitionerTask = addTask("TestDataPartitioner");
        Task[] runClusteringTasks = addTasks("RunClustering", numClusteringTasks);
        Task clusteringResultWriterTask = addTask("ClusteringResultWriter");

        // input the training and test data to the workflow
        addEdge(0, planetClusterTrainingTask);
        addEdge(1, testDataPartitionerTask);
        
        // send cluster centroids to each task that runs clustering on the test data
        for (int i = 0; i < runClusteringTasks.length; i++) {
            addEdge(planetClusterTrainingTask, runClusteringTasks[i]);
        }

        // input from the test data partitioner InputPort 1 of the respective task to run clustering for that partition
        for (int partition = 0; partition < numClusteringTasks; partition++) {
            addEdge(testDataPartitionerTask, partition, runClusteringTasks[partition], 1);
        }

        // combine the results of distributed clustering of the test data
        for (int partition = 0; partition < numClusteringTasks; partition++) {
            addEdge(runClusteringTasks[partition], 0, clusteringResultWriterTask, partition);
        }

        // write the results
        addEdge(clusteringResultWriterTask, 0);
    }
}