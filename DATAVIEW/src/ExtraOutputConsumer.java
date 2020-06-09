import dataview.models.InputPort;
import dataview.models.OutputPort;
import dataview.models.Port;
import dataview.models.Task;

public class ExtraOutputConsumer extends Task {
    public ExtraOutputConsumer() {
        super("Extra Input Consumer", "Allows connection of edges when an output of a Task instance is not needed.");

        ins = new InputPort[1];
        ins[0] = new InputPort("Input", Port.DATAVIEW_String, "Input to discard");

        outs = new OutputPort[0];  // doesn't actually output anything
    }

    @Override
    public void run() {
        // do nothing
    }
}
