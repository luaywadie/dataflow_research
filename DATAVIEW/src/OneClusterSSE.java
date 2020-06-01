import dataview.models.*;

public class OneClusterSSE extends Task {
    public OneClusterSSE() {
        super("OneClusterSSE", "Calculates the SSE for a single cluster.");

        ins = new InputPort[2];
        ins[0] = new InputPort("Partition", Port.DATAVIEW_MathMatrix, "All data points in the cluster.");
        ins[1] = new InputPort("Centoid", Port.DATAVIEW_MathVector, "The centroid for the cluster");

        outs = new OutputPort[1];
        outs[0] = new OutputPort("SSE", Port.DATAVIEW_double, "The SSE of the cluster.");
    }

    @Override
    public void run() {
        DATAVIEW_MathMatrix data = (DATAVIEW_MathMatrix) ins[0].read();
        DATAVIEW_MathVector centroid = (DATAVIEW_MathVector) ins[1].read();

        double sse = 0;
        for (int row = 0; row < data.getNumOfRows(); row++) {
            sse += euclideanDist(data.getRow(row), centroid);
        }
        outs[0].write(sse);
    }

    private double euclideanDist(DATAVIEW_MathVector x, DATAVIEW_MathVector y) {
        // make sure the vectors are the same length
        if (x.length() != y.length()) {
            throw new IllegalArgumentException("Vectors should be the same length. Received " + x.length() + " and " +y.length());
        }

        double accumulator = 0;
        for (int i = 0; i < x.length(); i++) {
            accumulator += Math.pow((x.get(i) - y.get(i)), 2);
        }
        return accumulator;
    }
}
