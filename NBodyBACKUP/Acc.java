
import dataview.models.*;

/**
 * This task handles the computation of the acceleration of 1 of the N bodies. There will need to be 1 instance of this
 * task for each time step AND body in the simulation (e.g. 5 bodies and 20 time step –> 5*20 = 100 instances).
 * Input from: InitializeNBody_Task
 * Input to: Pos, Vel, NBodyResultWriter_Task
 * Parallel with: Nothing

 */

public class Acc extends Task {

    public Acc() {
        super("Compute Acceleration", "Computes the accelerations of an individual body at the current time step.");
        // need position of all bodies, and mass of all bodies except the one for which we are calculating acceleration
        ins = new InputPort[2 * (NBodyWorkflow.N) - 1]; //Changed *********************************
        //ins[0] = new InputPort("Index", Port.DATAVIEW_int, "Index i of the body; 0 <= i < N");
        ins[0] = new InputPort("Position", Port.DATAVIEW_MathVector, "Position of the body for which we are calculating acceleration.");
        for (int i = 1; i < NBodyWorkflow.N; i++) {
            // start at 1 because index 0 is reserved for the position of the body for which we are calculating acceleration
            ins[2 * i - 1] = new InputPort("Position", Port.DATAVIEW_MathVector, "Position of one of the bodies for which we are NOT calculating acceleration.");
            ins[2 * i] = new InputPort("Mass", Port.DATAVIEW_double, "Mass of one of the bodies for which we are NOT calculating acceleration.");
        }

        // output acceleration of the body, as well as the masses of all OTHER bodies (to propagate them forward in time)
        outs = new OutputPort[NBodyWorkflow.N];
        outs[0] = new OutputPort("Acceleration", Port.DATAVIEW_MathVector, "The result of calculating this body's acceleration.");
        for (int i = 1; i < NBodyWorkflow.N; i++) {
            outs[i] = new OutputPort("Mass", Port.DATAVIEW_double, "Mass of one of the bodies for which we are NOT calculating acceleration");
        }
    }

    @Override
    public void run() {
        /* SETUP */

        // read the inputs
        DATAVIEW_MathVector rawThisPosition = (DATAVIEW_MathVector)ins[0].read();
        Vector3D thisPosition = new Vector3D(rawThisPosition.get(0), rawThisPosition.get(1), rawThisPosition.get(2));
        Vector3D[] otherPositions = new Vector3D[NBodyWorkflow.N - 1];
        double[] otherMasses = new double[NBodyWorkflow.N - 1];
        for (int otherBody = 0; otherBody < NBodyWorkflow.N - 1; otherBody++) {
            DATAVIEW_MathVector rawOtherPosition = (DATAVIEW_MathVector) ins[1 + (2 * otherBody)].read();
            otherPositions[otherBody] = new Vector3D(rawOtherPosition.get(0), rawOtherPosition.get(1), rawOtherPosition.get(2));
            otherMasses[otherBody] = (double) ins[2 + (2 * otherBody)].read();
        }

        /* ACTUAL CALCULATION */
        Vector3D resultAccel = Vector3D.makeOrigin();
        for (int otherBody = 0; otherBody < NBodyWorkflow.N - 1; otherBody++) {
            double temp = (NBodyWorkflow.gc * otherMasses[otherBody]) / Math.pow(thisPosition.minus(otherPositions[otherBody]).mod(), 3);
            resultAccel = resultAccel.plus(otherPositions[otherBody].minus(thisPosition).times(temp));
        }

        // Write output of this task
        outs[0].write(resultAccel);
        for (int i = 1; i < NBodyWorkflow.N; i++) {
            outs[i].write(otherMasses[i - 1]);
        }
    }
}
