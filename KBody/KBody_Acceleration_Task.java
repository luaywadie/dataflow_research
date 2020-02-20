import dataview.models.InputPort;
import dataview.models.Port;
import dataview.models.Task;

public class KBody_Acceleration_Task extends Task {
    public KBody_Acceleration_Task() {
        ins = new InputPort[2 * (KBody_Workflow.k - 1) + 1];
        ins[0] = new InputPort("This Position", Port.DATAVIEW_MathVector, "The position of the body being calculated.");

    }
}
