import dataview.models.Workflow;

public class RoughKMeansClustering extends Workflow {
    public static final int K = 3;  // number of clusters
    public static final int F = 7;  // number of features
    public static final double THRESHOLD = 0.1;  // TODO: how to determine good value for this?
    public RoughKMeansClustering() {
        // TODO: the proper constructor
        super("fasd", "fad");
    }
}
