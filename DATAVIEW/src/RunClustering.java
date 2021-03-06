import dataview.models.*;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;

import java.io.*;
import java.nio.charset.Charset;

/**
 * ===============================================
 * Takes as inputs the cluster centroids and the data which we want to perform clustering on.
 *
 * The cluster centroids are determined in the previous task (PlanetClusterTraining), but because
 * we can not pass actual objects between tasks, we must take the string representation and use
 * that to reconstruct the model here. We do this by "retraining" the model, but give it only
 * the centroids as training data. Thus, the model is effectively reconstructed.
 *
 * Both the training and test sets must first be put in to ARFF before they can be used
 * by the SimpleKMeans model.
 *
 * Finally, we output the sample ID with its corresponding cluster number. For convenience,
 * we also append the data associated with that point.
 * ===============================================
 * Input(s): Centroids from PlanetClusterTraining Task and Test data from TestDataPartitioner Task
 * Output(s): Centroid assignment for each data entry within the Test data.
 * ===============================================
 */
public class RunClustering extends Task {
    public RunClustering() {
        super("Clusterer", "Runs K-means clustering on a pre-trained model.");
        // Setup ports
        ins = new InputPort[2];
        outs = new OutputPort[1];
        // Assign Ports
        ins[0] = new InputPort("Centroids", Port.DATAVIEW_Table,
                "The cluster centroids from the trained K-means model.");
        ins[1] = new InputPort("Partition", Port.DATAVIEW_Table,
                "The parition of test data to be clustered in this instance.");
        outs[0] = new OutputPort("Results", Port.DATAVIEW_Table,
                "Table show clustering results for each test data point ID");
    }

    public void run() {
        // Create string to read in centroids and write them into ARFF format for SimpleKMeans algorithm (Weka)
        StringBuilder centroidsStr = new StringBuilder();
        // Create structure of ARFF file format and append it
        centroidsStr.append("@relation centroidsStr\n" +
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
                "@attribute pctg_oither numeric\n" +
                "@data\n"
                );
        // Get the centroids from the first input port
        DATAVIEW_Table rawCentroids = (DATAVIEW_Table) ins[0].read();
        // Append each centroid read in into the centroidsStr StringBuilder above, replacing colons with commas
        centroidsStr.append(rawCentroids.toString().replace(':', ','));
        InputStream centroidInputStream = new ByteArrayInputStream(centroidsStr.toString().getBytes(Charset.forName("UTF-8")));
        BufferedReader centroidBufferedReader = new BufferedReader(new InputStreamReader(centroidInputStream));
        // Instantiate the instances for the centroids
        Instances centroids = null;
        try {
            // Set the centroids inside of Weka's Instances class
            centroids = new Instances(centroidBufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test data (to perform clustering on)
        StringBuilder testDataString = new StringBuilder();
        testDataString.append("@relation testData\n" +
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
                "@attribute pctg_oither numeric\n" +
                "@data\n"
        );
        // Get the second input which is the Test data
        DATAVIEW_Table rawTestData = (DATAVIEW_Table) ins[1].read();
        // Get the starting ID of the current data list
        int startId = Integer.parseInt(rawTestData.get(0, 0));
        // Get the ending ID of the current data list
        int endId = Integer.parseInt(rawTestData.get(rawTestData.getNumOfRows() - 1, 0));
        // Trim the IDs before feeding in to the model
        StringBuilder idTrimmedTestData = new StringBuilder();
        for (int row = 0; row < rawTestData.getNumOfRows(); row++) {
            // Constructs the row, which will be appended to the whole table
            StringBuilder rowBuilder = new StringBuilder();
            // Start at col = 1 because col = 0 is the ID column
            for (int col = 1; col < rawTestData.getNumOfColumns(); col++) {
                rowBuilder.append(rawTestData.get(row, col));
                if (col == rawTestData.getNumOfColumns() - 1) { // append commas to all but last row
                    rowBuilder.append("\n");
                } else {
                    rowBuilder.append(",");
                }
            }
            idTrimmedTestData.append(rowBuilder);
        }
        testDataString.append(idTrimmedTestData.toString());
        InputStream testDataInputStream = new ByteArrayInputStream(testDataString.toString().getBytes(Charset.forName("UTF-8")));
        BufferedReader testDataBufferedReader = new BufferedReader(new InputStreamReader(testDataInputStream));
        Instances testData = null;
        try {
            testData = new Instances(testDataBufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        Because we can not actually pass the model between the tasks, we must rebuild the model. By passing only the centroids
        (which we already got from the PlanetClusterTraining task), we ultimately are able to quickly reconstruct our model.
        */
        // Declare the SimpleKMeans model
        SimpleKMeans model = new SimpleKMeans();
        try {
            // Set the number of clusters
            model.setNumClusters(PlanetaryClustering.numClusters);
            // Keep data order preserved so we can process on them later
            model.setPreserveInstancesOrder(true);
            // Construct the model given the centroids
            model.buildClusterer(centroids);
        } catch (Exception e) {
            System.err.println("RunClustering: Number of clusters is negative");
            e.printStackTrace();
        }

        // Perform clustering and write results
        int currentId = startId;  // want to keep ID labels with clustering results
        StringBuilder clusterResults = new StringBuilder();
        for (Instance dataPoint: testData) {
            StringBuilder rowResult = new StringBuilder();
            rowResult.append(currentId + ",");
            int clusterNo = -1;
            try {
                clusterNo = model.clusterInstance(dataPoint);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rowResult.append(clusterNo + ",");
            // Now just append all of the data (for user convenience)
            double[] attributeVals = dataPoint.toDoubleArray();
            for (int col = 0; col < attributeVals.length; col++) {
                rowResult.append(attributeVals[col]);
                if (col == attributeVals.length - 1) {  // is the final column, newline
                    rowResult.append("\n");
                } else {
                    rowResult.append(",");
                }
            }
            clusterResults.append(rowResult);
            currentId++;
        }

        // Writing the result
        outs[0].write(clusterResults);
    }
}
