import dataview.models.*;

/**
 * Input from:  ComputeAcceleration_Task, ComputePosition_Task, ComputeCollisions_Task
 * Outputs: Lists of the results
 * Parallel with: Nothing
 */
public class NBodyResultWriter_Task extends Task {
    public NBodyResultWriter_Task() {
        super("Result Writer", "Writes the results of the workflow, either for the next iteration, or for viewing of results.");
        ins = new InputPort[NBodyWorkflow.N * 3];
        for (int i = 0; i < NBodyWorkflow.N; i++) {
            ins[3*i] = new InputPort("Position " + i, Port.DATAVIEW_MathVector, "The output position vector of body " + i + ".");
            ins[3*i + 1] = new InputPort("Velocity " + i, Port.DATAVIEW_MathVector, "The output velocity vector of body " + i + ".");
            ins[3*i + 2] = new InputPort("Acceleration " + i, Port.DATAVIEW_MathVector, "The output acceleration vector of body " + i + ".");
            // don't include mass, because this will stay constant
        }

        outs = new OutputPort[3];
        outs[0] = new OutputPort("Position", Port.DATAVIEW_MathMatrix, "The result positions.");
        outs[1] = new OutputPort("Velocity", Port.DATAVIEW_MathMatrix, "The result velocities.");
        outs[2] = new OutputPort("Acceleration", Port.DATAVIEW_MathMatrix, "The result accelerations.");
    }

    @Override
    public void run() {
        double[][] positions = new double[NBodyWorkflow.N][3];
        double[][] velocities = new double[NBodyWorkflow.N][3];
        double[][] accelerations = new double[NBodyWorkflow.N][3];

        for (int row = 0; row < NBodyWorkflow.N; row++) {
            // write each row of of the resulting position matrix
            DATAVIEW_MathVector rawPosition = (DATAVIEW_MathVector) ins[row*3].read();
            positions[row][0] = rawPosition.get(0);
            positions[row][1] = rawPosition.get(1);
            positions[row][2] = rawPosition.get(2);

            // write each row of the resulting velocity matrix
            DATAVIEW_MathVector rawVelocity = (DATAVIEW_MathVector) ins[row*3 + 1].read();
            velocities[row][0] = rawVelocity.get(0);
            velocities[row][1] = rawVelocity.get(1);
            velocities[row][2] = rawVelocity.get(2);

            // write each row of the resulting acceleration matrix
            DATAVIEW_MathVector rawAcceleration = (DATAVIEW_MathVector) ins[row*3 + 2].read();
            accelerations[row][0] = rawAcceleration.get(0);
            accelerations[row][1] = rawAcceleration.get(1);
            accelerations[row][2] = rawAcceleration.get(2);
        }

        DATAVIEW_MathMatrix position = new DATAVIEW_MathMatrix(positions);
        DATAVIEW_MathMatrix velocity = new DATAVIEW_MathMatrix(velocities);
        DATAVIEW_MathMatrix acceleration = new DATAVIEW_MathMatrix(accelerations);

        outs[0].write(position);
        outs[1].write(velocity);
        outs[2].write(acceleration);

    }
}
