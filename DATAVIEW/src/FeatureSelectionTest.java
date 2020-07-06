import dataview.models.DATAVIEW_BigFile;
import dataview.models.Task;
import dataview.models.Workflow;

public class FeatureSelectionTest extends Workflow {
    public FeatureSelectionTest() {
        super("CFG", "fsd");

        wins = new DATAVIEW_BigFile[2];
        wins[0] = new DATAVIEW_BigFile("fct_DATASET.csv");
        wins[1] = new DATAVIEW_BigFile("fct_DROP.csv");

        wouts = new DATAVIEW_BigFile[1];
        wouts[0] = new DATAVIEW_BigFile("fct_FILTERED");
    }

    public void design() {
        Task task = addTask("FeatureSelection");
        addEdge(0, task, 0);
        addEdge(1, task, 1);

        addEdge(task, 0);
    }
}
