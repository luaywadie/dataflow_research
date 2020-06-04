import dataview.models.*;

public class AllClusterSSE extends Task {
    public AllClusterSSE() {
        super("AllClusterSSE", "Sums up the square error of all clusters.");

        ins = new InputPort[KMeansClustering.K * 2];  // 2 per cluster – SSE and cardinality
        for (int k = 0; k < KMeansClustering.K; k++) {
            ins[2 * k] = new InputPort("Cluster " + (k + 1) + " SSE", Port.DATAVIEW_double, "The SSE from cluster " + (k + 1));
            ins[2 * k + 1] = new InputPort("Cluster " + (k + 1) + " Cardinality", Port.DATAVIEW_int, "The cardinality of cluster " + (k + 1));
        }

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Total SSE", Port.DATAVIEW_String, "SSE and related data");
    }

    @Override
    public void run() {
        JSONObject sseData = new JSONObject();
        double[] clusterSSEs = new double[KMeansClustering.K];
        int totalCardinality = 0;

        // gather relevant info from each cluster
        for (int k = 0; k < KMeansClustering.K; k++) {
            clusterSSEs[k] = (double) ins[2 * k].read();  // the SSE of cluster k + 1
            int thisClusterCardinality = (int) ins[2 * k + 1].read();  // the cardinality of cluster k + 1
            totalCardinality += thisClusterCardinality;

            // construct the inner object which is associated to cluster k + 1
            JSONObject innerObject = new JSONObject();
            // the raw SSE for the cluster
            innerObject.put("sse", new JSONValue(Double.toString(clusterSSEs[k])));
            // the cardinality of the set
            innerObject.put("cardinality", new JSONValue(Integer.toString(thisClusterCardinality)));
            // the mean error a point in this cluster contributes
            if (thisClusterCardinality == 0) {  // 0 points in cluster, so SSE of 0 would be misleading
                innerObject.put("mean_sse", new JSONValue("none"));
            } else {
                innerObject.put("mean_sse", new JSONValue(Double.toString(clusterSSEs[k] / thisClusterCardinality)));
            }
            sseData.put(Integer.toString(k + 1), new JSONValue(innerObject));
        }
        // sum of error over all clusters
        double totalSSE = new DATAVIEW_MathVector(clusterSSEs).sum();

        // finish constructing the output by adding the totals
        JSONObject innerObject = new JSONObject();
        innerObject.put("sse", new JSONValue(Double.toString(totalSSE)));
        innerObject.put("cardinality", new JSONValue(Integer.toString(totalCardinality)));
        innerObject.put("mean_sse", new JSONValue(Double.toString(totalSSE/totalCardinality)));
        sseData.put("total", new JSONValue(innerObject));

        outs[0].write(sseData.toString());
    }
}
