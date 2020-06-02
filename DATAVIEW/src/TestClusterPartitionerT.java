import dataview.models.*;

public class TestClusterPartitionerT extends Task {
    public TestClusterPartitionerT() {
        super("fasdfad", "asdf");

        ins = new InputPort[3];
        ins[0] = new InputPort("fasd", Port.DATAVIEW_MathMatrix, "faskd");
        ins[1] = new InputPort("fasd", Port.DATAVIEW_MathMatrix, "faskd");
        ins[2] = new InputPort("fasd", Port.DATAVIEW_MathMatrix, "faskd");

        outs = new OutputPort[4];
        outs[0] = new OutputPort("asdf", Port.DATAVIEW_MathMatrix, "dfa");
        outs[1] = new OutputPort("asdf", Port.DATAVIEW_MathMatrix, "dfa");
        outs[2] = new OutputPort("asdf", Port.DATAVIEW_MathMatrix, "dfa");
        outs[3] = new OutputPort("asdf", Port.DATAVIEW_MathMatrix, "dfa");
    }

    @Override
    public void run() {
        DATAVIEW_MathMatrix m1 = (DATAVIEW_MathMatrix) ins[0].read();
        DATAVIEW_MathMatrix m2 = (DATAVIEW_MathMatrix) ins[1].read();
        DATAVIEW_MathMatrix m3 = (DATAVIEW_MathMatrix) ins[2].read();
    }
}
