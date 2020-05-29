import dataview.models.*;

/**
 * Performs unit normalization of a single column of data.
 * Instances: PlanetaryClusteringV2.F
 * Input [0] – DATAVIEW_MathVector: A column vector on which to perform normalization.
 *     From: FeaturePartitioner
 * Output [0] – DATAVIEW_MathVector: A column vector that has been normalized.
 *     To: InitCentroids
 */

public class NormalizeData extends Task {
    public NormalizeData() {
        super("Unit Normalization", "Perform unit normalization on a single feature");

        ins = new InputPort[1];
        ins[0] = new InputPort("Vector", Port.DATAVIEW_MathVector, "A single feature from the data set");

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Normalized Vector", Port.DATAVIEW_MathVector, "Normalized vector");
    }

    @Override
    public void run() {
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        DATAVIEW_MathVector feature = (DATAVIEW_MathVector) ins[0].read();

        // figure out min/max values
        for (int i = 0; i < feature.length(); i++) {
            if (feature.get(i) > max) max = feature.get(i);
            if (feature.get(i) < min) min = feature.get(i);
        }

        double[] normalizedFeature = new double[feature.length()];
        for (int i = 0; i < normalizedFeature.length; i++)
            /* NOTE: this normalization calculation would be good for partial function application;
            could return function that already has max & min plugged in, just waiting for the
            value to be normalized */
            normalizedFeature[i] = (feature.get(i) - min) / (max - min);

        outs[0].write(new DATAVIEW_MathVector(normalizedFeature));
    }
}
