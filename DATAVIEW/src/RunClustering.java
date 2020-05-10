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
        // centroids
        DATAVIEW_Table rawCentroids = (DATAVIEW_Table) ins[0].read();
        InputStream centroidInputStream = new ByteArrayInputStream(rawCentroids.toString().getBytes(Charset.forName("UTF-8")));
        BufferedReader centroidBufferedReader = new BufferedReader(new InputStreamReader(centroidInputStream));
        Instances centroids = null;
        try {
            centroids = new Instances(centroidBufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // test data (to perfrom clustering on)
        DATAVIEW_Table rawTestData = (DATAVIEW_Table) ins[1].read();
        // get the start and end ID labels (for writing to output)
        int startId = Integer.parseInt(rawTestData.get(1, 0));
        int endId = Integer.parseInt(rawTestData.get(rawTestData.getNumOfRows() - 1, 0));
        // trim the IDs before feeding in to the model
        // start at row 1 because 0 is just header row
        StringBuilder idTrimmedTestData = new StringBuilder();
        for (int row = 1; row < rawTestData.getNumOfRows(); row++) {
            // constructs the row, which will be appended to the whole table
            StringBuilder rowBuilder = new StringBuilder();
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
        // retrieve the start and end ID's, but remove them before sending them through the model

        InputStream testDataInputStream = new ByteArrayInputStream(idTrimmedTestData.toString().getBytes(Charset.forName("UTF-8")));
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

        int currentId = startId;
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
