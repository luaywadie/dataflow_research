import dataview.models.*;
import weka.clusterers.SimpleKMeans;

public class RunClustering extends Task {
    public RunClustering() {
        super("Clusterer", "Runs K-means clustering on a pre-trained model.");

        ins = new InputPort[2];
        ins[0] = new InputPort("Centroids", Port.DATAVIEW_Table,
                "The cluster centroids from the trained K-means model.");
        ins[1] = new InputPort("Partition", Port.DATAVIEW_Table,
                "The parition of test data to be clustered in this instance.");

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Results", Port.DATAVIEW_Table,
                "Table show clustering results for each test data point ID");
    }

    public void run() {
        // read in the centroids
        DATAVIEW_Table rawCentroids = (DATAVIEW_Table) ins[0].read();
        StringBuilder centroids = new StringBuilder(); // in ARFF format
        // header for ARFF format
        centroids.append("@relation dataset\n" +
                "@attribute mass numeric\n" +
                "@attribute diameter numeric\n" +
                "@attribute surface_temperature numeric\n" +
                "@attribute pctg_oxygen numeric\n" +
                "@attribute pctg_helium numeric\n" +
                "@attribute pctg_iron numeric\n" +
                "@attribute pctg_nickel numeric\n" +
                "@attribute pctg_silicon numeric\n" +
                "@attribute pctg_aluminum numeric\n" +
                "@attribute pctg_calcium numeric\n" +
                "@attribute pctg_sodium numeric\n" +
                "@attribute pctg_potassium numeric\n" +
                "@attribute pctg_magnesium numeric\n" +
                "@attribute pctg_other numeric\n" +
                "@data\n");
        centroids.append(rawCentroids.toString());

        SimpleKMeans model = new SimpleKMeans();
        try {
            model.setNumClusters(PlanetaryClustering.numClusters);
        } catch (Exception e) {
            System.err.println("RunClustering: Number of clusters in negative");
            e.printStackTrace();
        }


        // read in the test data
        DATAVIEW_Table rawTestData = (DATAVIEW_Table) ins[1].read();

        // perform clustering and

        // write result

    }
}
