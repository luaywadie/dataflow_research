import dataview.models.*;

public class AllClusterSSE extends Task {
    public AllClusterSSE() {
        super("AllClusterSSE", "Sums up the square error of all clusters.");

        ins = new InputPort[KMeansClustering.K];
        for (int k = 0; k < ins.length; k++) {
            ins[k] = new InputPort("Cluster " + (k + 1) + " SSE", Port.DATAVIEW_double, "The SSE from cluster " + (k + 1));
        }

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Total SSE", Port.DATAVIEW_HashMap, "The SSE of all clusters individuall and summed.");
    }

    @Override
    public void run() {
        DATAVIEW_HashMap sseMap = new DATAVIEW_HashMap();
        double[] clusterSSEs = new double[ins.length];

        // gather SSEs of individual clusters
        for (int k = 0; k < clusterSSEs.length; k++) {
            clusterSSEs[k] = (double) ins[k].read();
            sseMap.put(Integer.toString(k + 2), Double.toString(clusterSSEs[k]));
        }

        // now sum the error from all clusters and add that to the HashMap
        DATAVIEW_MathVector clusterSSEVector = new DATAVIEW_MathVector(clusterSSEs);
        sseMap.put("Total", Double.toString(clusterSSEVector.sum()));

        outs[0].write(sseMap);
    }
}
