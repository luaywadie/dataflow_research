import dataview.models.*;

import java.io.File;
import java.io.IOException;

/**
 * Input from: Pos, Vel
 * Input to: NBodyResultWriter_Task
 * Parallel with: Nothing
 */

public class Coll extends Task {
    public Coll() {
        super("Collisions", "Compute the collisions between bodies");

        ins = new InputPort[NBodyWorkflow.N * 2];
        outs = new OutputPort[NBodyWorkflow.N];
        for (int i = 0; i < NBodyWorkflow.N; i++) {
            ins[i*2] = new InputPort("Position", Port.DATAVIEW_MathVector, "Position of a body.");
            ins[i*2 + 1] = new InputPort("Velocity" + i / 2, Port.DATAVIEW_MathVector, "Velocity of a body.");
            outs[i] = new OutputPort("Velocity", Port.DATAVIEW_MathVector, "Velocity of a body.");
        }
    }


    @Override
    public void run() {
        /* SETUP */
        Vector3D[] positions = new Vector3D[NBodyWorkflow.N];
        Vector3D[] velocities = new Vector3D[NBodyWorkflow.N];

        for (int i = 0; i < NBodyWorkflow.N; i++) {

            DATAVIEW_MathVector rawPosition = (DATAVIEW_MathVector) ins[2*i].read();
            // get x, y, z position components and make vector for it
            positions[i] = new Vector3D(rawPosition.get(0), rawPosition.get(1), rawPosition.get(2));

            DATAVIEW_MathVector rawVelocity = (DATAVIEW_MathVector) ins[2*i + 1].read();
            // get x, y, z velocity components and make vector for it
            velocities[i] = new Vector3D(rawVelocity.get(0), rawVelocity.get(1), rawVelocity.get(2));
        }

        /* ACTUAL CALCULATION */
        for (int i = 0; i < NBodyWorkflow.N; i++) {
            for (int j = i+1; j < NBodyWorkflow.N; j++) {
                // if the bodies are in the same position, they have collided
                if ((positions[i].x == positions[j].x) && (positions[i].y == positions[j].y) && (positions[i].z == positions[j].z)) {
                    Vector3D hold = velocities[i];
                    velocities[i] = velocities[j];
                    velocities[j] = hold;
                }
            }
        }

        for (int i = 0; i < NBodyWorkflow.N; i++) {
            try{
                File f = new File(outs[i].getFileName());
                f.createNewFile();
            } catch (IOException e) {
                System.exit(0);
            }
            outs[i].write(velocities[i]);
        }
    }
}
