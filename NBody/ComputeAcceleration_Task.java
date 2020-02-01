import dataview.models.*;

/**
 * This task handles the computation of the acceleration of 1 of the N bodies. There will need to be 1 instance of this
 * task for each time step AND body in the simulation (e.g. 5 bodies and 20 time step –> 5*20 = 100 instances)
 */

public class ComputeAcceleration_Task extends Task {

    public ComputeAcceleration_Task() {
        super("Compute Acceleration", "Computes the accelerations of an individual body at the" +
                " current time step.");

        // 2*N - 1 because we need mass and position of all bodies, except not mass of the body acceleration is being calculated for
        ins = new InputPort[2*NBodyWorkflow.N - 1];
        ins[0] = new InputPort("This position", Port.DATAVIEW_MathVector, "Position of this body");
        // gather position and mass of all OTHER bodies (which we are not currently calculating acceleration for)
        for (int i = 1; i < 2*NBodyWorkflow.N - 1; i += 2) {
            ins[i] = new InputPort("Position " + i, Port.DATAVIEW_MathVector, "The position of other body " + i);
            ins[i+1] = new InputPort("Mass " + i, Port.DATAVIEW_double, "The mass of other body " + i);
        }

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Acceleration", Port.DATAVIEW_MathVector, "The resulting acceleration of the body (AT this time step");
    }

    @Override
    public void run() {
        /* SETUP */
        DATAVIEW_MathVector rawThisPosition = (DATAVIEW_MathVector) ins[0].read();
        Vector3D thisPosition = new Vector3D(rawThisPosition.get(0), rawThisPosition.get(1), rawThisPosition.get(2));

        Vector3D[] otherPositions = new Vector3D[NBodyWorkflow.N - 1];
        double[] otherMasses = new double[NBodyWorkflow.N - 1];
        for (int i = 1; i < 2*NBodyWorkflow.N - 1; i += 2) {
            // j is the index of the otherPositions and otherMasses arrays
            int j = ((i+1)/2) - 1;

            DATAVIEW_MathVector rawPosition = (DATAVIEW_MathVector) ins[i].read();
            otherPositions[j] = new Vector3D(rawPosition.get(0), rawPosition.get(1), rawPosition.get(2));

            double mass = (double) ins[i+1].read();
            otherMasses[j] = mass;
        }

        /* ACTUAL CALCULATION */
        Vector3D resultAccel = Vector3D.makeOrigin();
        for (int otherBody = 0; otherBody < otherMasses.length; otherBody++) {
            double temp = (NBodyWorkflow.gc * otherMasses[otherBody]) /
                    Math.pow(thisPosition.minus(otherPositions[otherBody]).mod(), 3);
                    resultAccel = resultAccel.plus(otherPositions[otherBody].minus(thisPosition).times(temp));
        }

        outs[0].write(resultAccel);
    }
}
