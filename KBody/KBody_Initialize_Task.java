import dataview.models.*;

/**
 * Input from: Text files
 * Input to: ComputeAcceleration_Task, Compute_Position_Task, ComputeVelocity_Task
 */
public class KBody_Initialize_Task extends Task {

    public KBody_Initialize_Task() {
        super("Initialize N-Body", "Reads the input files and diverts data accordingly.");
            // inputs
            ins = new InputPort[3];
            // Positions: from positions.input
            ins[0] = new InputPort("Positions", Port.DATAVIEW_MathMatrix,
                    "Matrix where rows correspond to one gravitational body and columns refer to the X, Y, Z coordinates of the body's position.");
            // Velocities: from velocities.input
            ins[1] = new InputPort("Velocities", Port.DATAVIEW_MathMatrix,
                    "Matrix where rows correspond to one gravitational body and columns refer to the X, Y, Z coordinates of the body's velocity.");
            // Masses: From masses.input
            ins[2] = new InputPort("Masses", Port.DATAVIEW_MathVector,
                    "Vector where each entry refers to the mass of one gravitational body.");

            // outputs
            outs = new OutputPort[3 * KBody_Workflow.k];
            for (int body = 0; body < KBody_Workflow.k; body++) {
                // Position of body i: to k instances of Acceleration
                outs[3 * body] = new OutputPort("Position " + body, Port.DATAVIEW_MathVector,
                        "A 3D vector representing the X, Y, Z coordinates of gravitational body " + body + ".");
                // Velocity of body i: to 1 instance of Velocity
                outs[3 * body + 1] = new OutputPort("Velocity " + body, Port.DATAVIEW_MathVector,
                        "A 3D vector represention the X, Y, Z coordinates of gravitational body " + body + ".");
                // Mass of body i: to k - 1 instances of Acceleration,
                outs[3 * body + 2] = new OutputPort("Mass " + body, Port.DATAVIEW_double,
                        "A single number corresponding to the mass of gravitational body " + body + ".");
            }
        }

    public void run() {

        // Step 1: Read the inputs
        DATAVIEW_MathMatrix rawPositions = (DATAVIEW_MathMatrix) ins[0].read();
        DATAVIEW_MathMatrix rawVelocities = (DATAVIEW_MathMatrix) ins[1].read();
        DATAVIEW_MathVector rawMasses = (DATAVIEW_MathVector) ins[2].read();

        // Step 2: Write the ouputs
//        Vector3D[] positions = new Vector3D[KBody_Workflow.k];
//        Vector3D[] velocities = new Vector3D[KBody_Workflow.k];
//        double[] masses = new double[KBody_Workflow.k];

        for (int body = 0; body < KBody_Workflow.k; body++) {
            // Use Vector3D because Vector3D.toString() gives us the proper representation of a vector for writing to a text file
            /*
            write the position of the ith gravitational body to:
                All instances of Acceleration
                1 instance of Position (ith instance)
             */
            outs[3 * body].write(new Vector3D(
                    rawPositions.get(body, 0), // X
                    rawPositions.get(body, 1), // Y
                    rawPositions.get(body, 2)  // Z
            ));
            /*
            write the velocity of the ith gravitational body to:
                1 instance of Velocity (ith instance)
                1 instance of Position (ith instance)
             */
            outs[3 * body + 1].write(new Vector3D(
                    rawVelocities.get(body, 0), // X
                    rawVelocities.get(body, 1), // Y
                    rawVelocities.get(body, 2)  // Z
            ));
            /*
            write the mass of the ith gravitational body to:
                All instances of Acceleration, except for the instance (ith) corresponding to calculation for the ith body
             */
            outs[3 * body + 2].write(rawMasses.get(body)); // just a scalar quantity
        }
    }
}
