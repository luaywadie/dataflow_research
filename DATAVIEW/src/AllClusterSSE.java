import dataview.models.*;

public class AllClusterSSE extends Task {
    public AllClusterSSE() {
        super("AllClusterSSE", "Sums up the square error of all clusters.");

        ins = new InputPort[PlanetaryClusteringV2.K];
        for (int k = 0; k < ins.length; k++) {
            ins[k] = new InputPort("Cluster " + (k + 1) + " SSE", Port.DATAVIEW_double, "The SSE from cluster " + (k + 1));
        }

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Total SSE", Port.DATAVIEW_double, "The total SSE among all clusters");
    }

    @Override
    public void run() {
        double[] clusterSSEs = new double[ins.length];
        for (int k = 0; k < clusterSSEs.length; k++) {
            clusterSSEs[k] = (double) ins[k].read();
        }

        DATAVIEW_MathVector clusterSSEVector = new DATAVIEW_MathVector(clusterSSEs);
        outs[0].write(clusterSSEVector.sum());
    }
}
