import dataview.models.*;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;

import java.io.*;
import java.nio.charset.Charset;

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
        StringBuilder centroidsStr = new StringBuilder();
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
        DATAVIEW_Table rawCentroids = (DATAVIEW_Table) ins[0].read();
        centroidsStr.append(rawCentroids.toString().replace(':', ','));
        InputStream centroidInputStream = new ByteArrayInputStream(centroidsStr.toString().getBytes(Charset.forName("UTF-8")));
        BufferedReader centroidBufferedReader = new BufferedReader(new InputStreamReader(centroidInputStream));
        Instances centroids = null;
        try {
            centroids = new Instances(centroidBufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // test data (to perform clustering on)
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
        DATAVIEW_Table rawTestData = (DATAVIEW_Table) ins[1].read();
        // get the start and end ID labels (for writing to output)
        int startId = Integer.parseInt(rawTestData.get(0, 0));
        int endId = Integer.parseInt(rawTestData.get(rawTestData.getNumOfRows() - 1, 0));
        // trim the IDs before feeding in to the model
        StringBuilder idTrimmedTestData = new StringBuilder();
        for (int row = 0; row < rawTestData.getNumOfRows(); row++) {
            // constructs the row, which will be appended to the whole table
            StringBuilder rowBuilder = new StringBuilder();
            // start at col = 1 because col = 0 is the ID column
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
        SimpleKMeans model = new SimpleKMeans();
        try {
            model.setNumClusters(PlanetaryClustering.numClusters);
            model.setPreserveInstancesOrder(true);
            model.buildClusterer(centroids);
        } catch (Exception e) {
            System.err.println("RunClustering: Number of clusters is negative");
            e.printStackTrace();
        }

        // perform clustering and write results
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
            // now just append all of the data (for user convenience)
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

        // writing the result
        outs[0].write(clusterResults);
    }
}
