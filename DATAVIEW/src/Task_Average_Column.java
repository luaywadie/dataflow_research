import dataview.models.*;

public class Task_Average_Column extends Task {
    public static int instanceCount = 0;

    // declare all I/O ports of the task
    public Task_Average_Column() {
        super("Average column vector", "This task averages a column vector.");
        instanceCount++;
        // define input ports
        InputPort ins[] = new InputPort[1];
        ins[0] = new InputPort("in0", Port.DATAVIEW_MathVector, "Vector to be averaged");

        // define output ports
        OutputPort outs[] = new OutputPort[1];
        outs[0] = new OutputPort("out0", Port.DATAVIEW_double, "Result of averaging vector");
    }

    // this method performs the actual actions of the tasks
    @Override
    public void run() {
        System.out.println("RUN AVG COL");
        // step 1: read data from input ports
        DATAVIEW_MathVector inputVector = (DATAVIEW_MathVector) ins[0].read();

        // step 2: perform computation using data read from step 1
        // sum up all of the values in the vector
        double sum = 0;
        for(int i = 0; i < inputVector.length(); i++) {
            System.out.println("____________________________" + inputVector.get(i));
            sum += inputVector.get(i);
        }
        double average = sum / inputVector.length();
        // step 3: write data to output port
        outs[0].write(average);
    }
}
