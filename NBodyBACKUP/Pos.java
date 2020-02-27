import dataview.models.*;

import java.io.File;
import java.io.IOException;

/**
 * Input from: InitializeNBody_Task, Acc
 * Input to: Coll, NBodyResultWriter_Task
 * Parallel with: Vel
 */

public class Pos extends Task {
    public Pos() {
        super("Compute Position", "Compute the position of one of the bodies");

        ins = new InputPort[3];
        ins[0] = new InputPort("Position", Port.DATAVIEW_MathVector, "The current position of the body (calculated at the previous time step).");
        ins[1] = new InputPort("Velocity", Port.DATAVIEW_MathVector, "The velocity of the body (calculated at the previous time step).");
        ins[2] = new InputPort("Acceleration", Port.DATAVIEW_MathVector, "The acceleration of the body (calculated this time step)");

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Position", Port.DATAVIEW_MathVector, "The resulting position of the body.");
    }

    @Override
    public void run() {
        try{
            File f = new File(outs[0].getFileName());
            f.createNewFile();
        } catch (IOException e) {
            System.exit(0);
        }

        /* SETUP */
        DATAVIEW_MathVector rawPosition = (DATAVIEW_MathVector) ins[0].read();
        Vector3D position = new Vector3D(rawPosition.get(0), rawPosition.get(1), rawPosition.get(2));

        // velocity of the body
        DATAVIEW_MathVector rawVelocity = (DATAVIEW_MathVector) ins[1].read();
        Vector3D velocity = new Vector3D(rawVelocity.get(0), rawVelocity.get(1), rawVelocity.get(2));

        // acceleration of the body
        DATAVIEW_MathVector rawAcceleration = (DATAVIEW_MathVector) ins[2].read();
        Vector3D acceleration = new Vector3D(rawAcceleration.get(0), rawAcceleration.get(1), rawAcceleration.get(2));

        /* ACTUAL CALCULATION */
        position = position.plus(velocity).plus(acceleration.times(0.5));
        outs[0].write(position);
    }
}
