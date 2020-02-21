import dataview.models.*;

/**
 * Input from: InitializeNBody_Task, ComputeAcceleration_Task
 * Input to: ComputeCollisions_Task
 * Parallel with: ComputeVelocity_Task
 */

public class ComputeVelocity_Task extends Task {
    public ComputeVelocity_Task() {
        super("Compute Velocity", "Compute the velocity of one of the bodies");

        ins = new InputPort[2];
        ins[0] = new InputPort("Velocity", Port.DATAVIEW_MathVector, "The velocity of the body (calculated at the previous time step).");
        ins[1] = new InputPort("Acceleration", Port.DATAVIEW_MathVector, "The acceleration of the body (calculated this time step).");

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Velocity", Port.DATAVIEW_MathVector, "The resulting velocity of the body (AT this time step");
    }

    @Override
    public void run() {
        /* SETUP */
        DATAVIEW_MathVector rawVelocity = (DATAVIEW_MathVector) ins[0].read();
        Vector3D initialVelocity = new Vector3D(rawVelocity.get(0), rawVelocity.get(1), rawVelocity.get(2));

        DATAVIEW_MathVector rawAcceleration = (DATAVIEW_MathVector) ins[1].read();
        Vector3D acceleration = new Vector3D(rawAcceleration.get(0), rawAcceleration.get(1), rawAcceleration.get(2));

        /* ACTUAL CALCULATION */
        Vector3D resultVelocity = initialVelocity.plus(acceleration);

        outs[0].write(resultVelocity);
    }
}
