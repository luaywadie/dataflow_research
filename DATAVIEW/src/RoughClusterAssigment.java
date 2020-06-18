import dataview.models.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Adapted from Interval Set Clustering of Web Users with Rough K-Means
 * by Pawan Lingras and Chad West
 * Journal of Intelligent Information Systems
 */
public class RoughClusterAssigment extends Task {
    public RoughClusterAssigment() {
        super("RoughClusterAssignment", "Assigns points to upper/lower bounds of clusters based on the concept of rough sets.");
        ins = new InputPort[2];
        ins[0] = new InputPort("Partition", Port.DATAVIEW_MathMatrix, "A partition of the entire data set to be clustered");
        ins[1] = new InputPort("Centroids", Port.DATAVIEW_MathMatrix, "Matrix containing all centroids");

        // output the upper and lower bound sets
        // map from cluster no. -> list of indices (relative to the entire data set, not partition) of points in the cluster
        outs = new OutputPort[2];
        outs[0] = new OutputPort("Lower Bound Set", Port.DATAVIEW_String, "JSON of cluster assignments");
        outs[1] = new OutputPort("Upper Bound Set", Port.DATAVIEW_String, "JSON of cluster assignments");
    }

    @Override
    public void run() {
        // the actual data set
        DATAVIEW_MathMatrix partition = (DATAVIEW_MathMatrix) ins[0].read();
        DATAVIEW_MathVector rowIDs = partition.getColumn(0); // get the IDs from original data set; make no assumption that IDs are in order
        partition = DATAVIEW_MathMatrix.dropColumn(0, partition); // don't want IDs for calculation
        // the centroids of the matrix
        DATAVIEW_MathMatrix centroids = (DATAVIEW_MathMatrix) ins[1].read();

        // set up the upper/lower bounds sets
        HashMap<Integer, ArrayList<DATAVIEW_MathVector>> lowerBounds = new HashMap<>(RoughKMeansClustering.K);
        HashMap<Integer, ArrayList<DATAVIEW_MathVector>> upperBounds = new HashMap<>(RoughKMeansClustering.K);
        for (int k = 0; k < RoughKMeansClustering.K; k++) {
            lowerBounds.put(k, new ArrayList<>());
            upperBounds.put(k, new ArrayList<>());
        }

        for (int i = 0; i < partition.getNumOfRows(); i++) { // go through all points in the cluster
            int rowId = (int) rowIDs.get(i);  // append to the current row AFTER we are done doing calculations with it
            DATAVIEW_MathVector row = partition.getRow(i);  // the data point we are considering
            // distance from the point to each centroid (clustDists[k] = distance to cluster k; 0 <= k < K)
            double[] clustDists = new double[centroids.getNumOfRows()];

            // find the centroid which the data point is closest to
            int minCluster = -1; // 0 <= minCluster < K
            double minDistance = Double.POSITIVE_INFINITY;
            for (int c = 0; c < centroids.getNumOfRows(); c++) {
                clustDists[c] = euclideanDistance(row, centroids.getRow(c));
                if (clustDists[c] < minDistance) {
                    minDistance = clustDists[c];
                    minCluster = c;
                }
            }

            // check to see if the data point is in upper bound of any clusters; if so, add the data point the upper bound
            ArrayList<DATAVIEW_MathVector> membershipSet = new ArrayList<>();  // corresponds to set T in the paper
            for (int c = 0; c < centroids.getNumOfRows(); c++) {
                if (c != minCluster) {
                    if (minDistance - clustDists[c] <= RoughKMeansClustering.THRESHOLD) {
                        membershipSet.get(c).add(row.insert(0, rowId));
                    }
                }
            }

            if (!membershipSet.isEmpty()) {
                // the the row is in the upper bounds of all clusters in the membershipSet
                for (int c = 0; c < membershipSet.size(); c++) {
                    // add the data point to the upper bound set for cluster c, with ID label attached
                    upperBounds.get(c).add(row.insert(0, rowId));
                }
            } else {
                // add the point with ID label attached to ONLY the LOWER bound set
                lowerBounds.get(minCluster).add(row.insert(0, rowId));
            }
            // point is added to upperBounds of minCluster regardless of whether it is in the lower bound set
            upperBounds.get(minCluster).add(row.insert(0, rowId));
        }

        // TODO (refactor): this JSON stuff should probably be its own method
        // get JSON for upper/lower bounds set for each cluster
        JSONObject lowerBoundsJSON = new JSONObject(); // cluster no. -> [data points assigned to lower bound of cluster]
        JSONObject upperBoundsJSON = new JSONObject(); // cluster no. -> [data points assigned to upper bound of cluster]
        // turn ArrayList of points in each clutser to JSONArray
        for (int k = 0; k < RoughKMeansClustering.K; k++) {  // each cluster (lower/upper bounds)
            // LOWER BOUND
            JSONObject lowerBoundMembers = new JSONObject();
            for (int i = 0; i < lowerBounds.get(k).size(); i++) {  // each point assigned to the cluster
                DATAVIEW_MathVector nextRow = lowerBounds.get(k).get(i);
                String rowId = Double.toString(nextRow.get(0));
                JSONArray dataPoint = new JSONArray();
                for (int col = 0; col <= nextRow.length(); col++) {  // each feature of the point INCLUDING row ID
                    dataPoint.add(new JSONValue(Double.toString(nextRow.get(col))));
                }
                lowerBoundMembers.put(rowId, new JSONValue(dataPoint));
            }
            lowerBoundsJSON.put(Integer.toString(k + 1), new JSONValue(lowerBoundMembers));

            // UPPER BOUND
            JSONObject upperBoundMembers = new JSONObject();
            for (int i = 0; i < upperBounds.get(k).size(); i++) {  // each point assigned to the cluster
                DATAVIEW_MathVector nextRow = upperBounds.get(k).get(i);
                String rowId = Double.toString(nextRow.get(0));
                JSONArray dataPoint = new JSONArray();
                for (int col = 0; col <= nextRow.length(); col++) {  // each feature of the point INCLUDING row ID
                    dataPoint.add(new JSONValue(Double.toString(nextRow.get(col))));
                }
                upperBoundMembers.put(rowId, new JSONValue(dataPoint));
            }
            upperBoundsJSON.put(Integer.toString(k + 1), new JSONValue(lowerBoundMembers));
        }

        outs[0].write(lowerBoundsJSON);
        outs[1].write(lowerBoundsJSON);
    }

    private double euclideanDistance(DATAVIEW_MathVector x, DATAVIEW_MathVector y) {
        // make sure the vectors are the same length
        if (x.length() != y.length()) {
            throw new IllegalArgumentException("Vectors should be the same length. Received " + x.length() + " and " +y.length());
        }

        double accumulator = 0;
        for (int i = 0; i < x.length(); i++) {
            accumulator += Math.pow((x.get(i) - y.get(i)), 2);
        }
        return Math.sqrt(accumulator);
    }
}