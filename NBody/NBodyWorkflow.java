import dataview.models.DATAVIEW_BigFile;
import dataview.models.Task;
import dataview.models.Workflow;

/**
 * Adapted from N-Body problem code found at https://rosettacode.org/wiki/N-body_problem#Java
 */

public class NBodyWorkflow extends Workflow {
    public final String POSITION_INPUT = "positions.input";
    public final String VELOCITY_INPUT = "velocities.input";
    public final String MASS_INPUT = "masses.input";
    public final String POSITION_OUTPUT = "positions.output";
    public final String VELOCITY_OUTPUT = "velocities.output";
    public final String ACCELERATION_OUTPUT = "accelerations.output";

    /*
       PE: Constant Folding
       Constants for the workflow; could also put these as input files, but this would not be ideal for
       partial evaluation.
     */
    public static final double gc = 0.05; // gravitational constant
    public static final int N = 3; // number of bodies
    public static final int timeSteps = 20; // time to advance each iteration

    public NBodyWorkflow() {
        super("N-Body Simulation", "A simulation of N-bodies' gravitational interactions.");
        wins = new DATAVIEW_BigFile[3];
        wins[0] = new DATAVIEW_BigFile(POSITION_INPUT);
        wins[1] = new DATAVIEW_BigFile(VELOCITY_INPUT);
        wins[2] = new DATAVIEW_BigFile(MASS_INPUT);

        wouts = new DATAVIEW_BigFile[3];
        wouts[0] = new DATAVIEW_BigFile(POSITION_OUTPUT);
        wouts[1] = new DATAVIEW_BigFile(VELOCITY_OUTPUT);
        wouts[2] = new DATAVIEW_BigFile(ACCELERATION_OUTPUT);
    }

    public void design() {
        /* TASKS */
        // 0: initialize the bodies
        Task initialize = addTask("InitializeNBody_Task");

        // 1: accelerations
        Task[] computeAccelerations = addTasks("ComputeAcceleration_Task", N);

        // 2: velocities
        Task[] computeVelocities = addTasks("ComputeVelocity_Task", N);

        // 3: positions
        Task[] computePositions = addTasks("ComputePosition_Task", N);

        // 4: collisions
        Task resolveCollisions = addTask("ComputeCollisions_Task");

        // 5: write results
        Task writeResults = addTask("NBodyResultWriter_Task");

        /* EDGES */
        // input files to the initialization task
        // position
        addEdge(0, initialize, 0);
        // velocity
        addEdge(1, initialize, 1);
        // mass
        addEdge(2, initialize, 2);

        // TO ComputeAcceleration_Task
        for (int mainBody = 0; mainBody < NBodyWorkflow.N; mainBody++) {
            addEdge(initialize, mainBody*3, computeAccelerations[mainBody], 0); // position of THIS




            // get the masses and positions of other bodies
//            for (int otherBody = 0; otherBody < NBodyWorkflow.N; otherBody++) {
//                if (mainBody != otherBody) {
//                    addEdge(initialize, otherBody*3, computeAccelerations[mainBody], (mainBody+1)3 + 1); // position
//                                                           // inputPort index offset by 1 because input port 0 is for the main body position
//                    System.out.println("POSITION EDGE BETWEEN INITIALIZE PoRT # " + otherBody*3 + " AND COMPUTE PORT " + mainBody*3);
//                    addEdge(initialize, otherBody*3 + 2, computeAccelerations[mainBody], mainBody*3 + 3); // mass
//                        //inputPort index offset by 3 because 1 for port 0 being main body position, plus another 2 because mass is the 3rd relative port
//                }
//            }
        }

        // TO ComputeVelocity_Task
        for (int i = 0; i < N; i++) {
            addEdge(initialize, 3*i + 1, computeVelocities[i], 0); // velocity
            addEdge(computeAccelerations[i], 0, computeVelocities[i], 1); // acceleration
        }

        // TO ComputePosition_Task
        for (int i = 0; i < N; i++) {
            addEdge(initialize, 3 * i, computePositions[i], 0); // position
            addEdge(computeVelocities[i], 0, computePositions[i], 1); // velocity
            addEdge(computeAccelerations[i], 0, computePositions[i], 2); // acceleration
        }

        // TO ComputeCollisions_Task
        for (int i = 0; i < N; i++) {
            addEdge(computePositions[i], 0, resolveCollisions, i * 2); // position
            addEdge(computeVelocities[i], 0, resolveCollisions, i*2 + 1); // velocity
        }

        // TO NBodyResultWriter_Task
        for (int i = 0; i < N; i++) {
            addEdge(computePositions[i], 0, writeResults, i*3); // position
            addEdge(resolveCollisions, i, writeResults, i*3 + 1); // velocity
            addEdge(computeAccelerations[i], 0, writeResults, i*3 + 2);
        }

        // TO wouts
        addEdge(writeResults, 0, 0);
        addEdge(writeResults, 1, 1);
        addEdge(writeResults, 2, 2);

    }
}
