import dataview.models.DATAVIEW_BigFile;
import dataview.models.Task;
import dataview.models.Workflow;

public class TestClusterPartitionerW extends Workflow {
    public TestClusterPartitionerW() {
        super("adsf", "asd");
        wins = new DATAVIEW_BigFile[3];
        wins[0] = new DATAVIEW_BigFile("partition_test1.csv");
        wins[1] = new DATAVIEW_BigFile("partition_test2.csv");
        wins[2] = new DATAVIEW_BigFile("partition_test3.csv");

        wouts = new DATAVIEW_BigFile[8]; // 4 clusters
        wouts[0] = new DATAVIEW_BigFile("1_test");
        wouts[1] = new DATAVIEW_BigFile("1_centroid");
        wouts[2] = new DATAVIEW_BigFile("2_test");
        wouts[3] = new DATAVIEW_BigFile("2_centroid");
        wouts[4] = new DATAVIEW_BigFile("3_test");
        wouts[5] = new DATAVIEW_BigFile("3_centroid");
        wouts[6] = new DATAVIEW_BigFile("4_test");
        wouts[7] = new DATAVIEW_BigFile("4_centroid");
    }

    public void design() {
        Task t = addTask("TestClusterPartitionerT");

        // inputs
        addEdge(0, t, 0);
        addEdge(1, t, 1);
        addEdge(2, t, 2);

        // outputs
        addEdge(t, 0, 0);
        addEdge(t, 1, 1);
        addEdge(t, 2, 2);
        addEdge(t, 3, 3);
        addEdge(t, 4, 4);
        addEdge(t, 5, 5);
        addEdge(t, 6, 6);
        addEdge(t, 7, 7);
    }
}
