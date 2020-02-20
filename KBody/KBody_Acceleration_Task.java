import dataview.models.*;

public class KBody_Acceleration_Task extends Task {
    public KBody_Acceleration_Task() {
        super("KBody Acceleration", "");

        // inputs
        ins = new InputPort[2 * (KBody_Workflow.k - 1) + 1];
        // Position of body i: from KBody_Initialize_Task
        ins[0] = new InputPort("This Position", Port.DATAVIEW_MathVector, "The position of the body being calculated.");
        for (int otherBody = 0; otherBody < KBody_Workflow.k - 1; otherBody++) {
            // Position of body j != i: from KBody_Initialize_Task
            ins[1 + (2 * otherBody)] = new InputPort("Other Body " + otherBody, Port.DATAVIEW_MathVector,
                    "Position of one of the gravitational bodies for which we are NOT calculating acceleration.");
            // Mass of body j != i: from KBody_Initialize_Task
            ins[2 + (2 * otherBody)] = new InputPort("Other Body " + otherBody + " Mass", Port.DATAVIEW_MathVector,
                    "Mass of one of the gravitational bodies for which we are NOT calculating acceleration.");
        }

        // outputs
        outs = new OutputPort[1];
        // Acceleration of Body i: to Position of body i, Velocity of body i, and Results
        outs[0] = new OutputPort("Velocity of Body i", Port.DATAVIEW_MathVector,
                "The resulting velocity of the gravitational.");
    }

    public void run() {
        // read the inputs
        DATAVIEW_MathVector rawThisPosition = (DATAVIEW_MathVector)ins[0].read();
        Vector3D thisPosition = new Vector3D(rawThisPosition.get(0), rawThisPosition.get(1), rawThisPosition.get(2));
        Vector3D[] otherPositions = new Vector3D[KBody_Workflow.k - 1];
        double[] otherMasses = new double[KBody_Workflow.k - 1];
        for (int otherBody = 0; otherBody < KBody_Workflow.k - 1; otherBody++) {
            DATAVIEW_MathVector rawOtherPosition = (DATAVIEW_MathVector) ins[1 + (2 * otherBody)].read();
            otherPositions[otherBody] = new Vector3D(rawOtherPosition.get(0), rawOtherPosition.get(1), rawOtherPosition.get(2));
            otherMasses[otherBody] = (double) ins[2 + (2 * otherBody)].read();
        }

        // perform the actual calculation
        Vector3D resultAccel = Vector3D.makeOrigin();
        for (int otherBody = 0; otherBody < KBody_Workflow.k - 1; otherBody++) {
            double temp = (KBody_Workflow.gc * otherMasses[otherBody]) / Math.pow(thisPosition.minus(otherPositions[otherBody]).mod(), 3);
            resultAccel = resultAccel.plus(otherPositions[otherBody].minus(thisPosition).times(temp));
        }

        // write the result
        outs[0].write(resultAccel);
    }
}