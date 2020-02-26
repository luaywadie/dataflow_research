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
    public static final int timeSteps = 3; // time to advance each iteration

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
        Task[] computeAccelerations = addTasks("Acc", N * timeSteps);

        // 2: velocities
        Task[] computeVelocities = addTasks("Vel", N * timeSteps);

        // 3: positions
        Task[] computePositions = addTasks("Pos", N * timeSteps);

        // 4: collisions
        Task[] computeCollisions = addTasks("Coll", timeSteps);

        // 5: write results
        Task writeResults = addTask("NBodyResultWriter_Task");

        /* EDGES */

        // workflow inputs
        addEdge(0, initialize); // position input file
        addEdge(1, initialize, 1); // velocity input file
        addEdge(2, initialize, 2); // mass input file

        // InitializeNBody_Task TO time step 0 Acc
        int positionIndex = -1; // the input port in which to feed body's position
        int massIndex = -1; // the input port in which to feed body's mass
        for (int body = 0; body < N; body++) {
            for (int taskInstance = 0; taskInstance < N; taskInstance++) {
                if (body == taskInstance) { // task corresponds to body
                    addEdge(initialize, 3 * body, computeAccelerations[taskInstance], 0); // position
                    positionIndex = 2 * body + 1;
                    massIndex = positionIndex + 1;
                } else { // task does not correspond to the body

                    addEdge(initialize, 3 * body, // position is an offset of 0
                            computeAccelerations[taskInstance], positionIndex);
                    addEdge(initialize, 3 * body + 2, // mass is an offset of 2
                            computeAccelerations[taskInstance], massIndex);

                }
            }

            // InitializeNBody_Task TO time step 0 Compute_Position_Task
            addEdge(initialize, 3 * body, computePositions[body], 0); // position
            System.out.printf("\t\u001b[33mFor body %d,: connecting OUT %d, %d to IN %d, %d\n\u001b[0m", body, 3 * body, 3 * body + 1, positionIndex, massIndex);
            addEdge(initialize, 3 * body + 1, computePositions[body], 1); // velocity

            // InitializeNBody_Task TO time step 0 Compute_Velocity_Task
            addEdge(initialize, 3 * body + 1, computeVelocities[body], 0); // velocity
        }

        /* intra-time-step edges (within one time step) */
        for (int timeStep = 0; timeStep < timeSteps; timeStep++) { // add edges FROM timeStep-1 time step, to timeStep
            for (int body = 0; body < N; body++) {
                int currentTaskIndex = N * timeStep + body;

                /* intra-time-step edges */
                // Acc TO Pos
                addEdge(computeAccelerations[currentTaskIndex], 0, computePositions[currentTaskIndex], 2); // acceleration

                // Acc TO Vel
                addEdge(computeAccelerations[currentTaskIndex], 0, computeVelocities[currentTaskIndex], 1); // acceleration

                // Pos TO Coll
                addEdge(computePositions[currentTaskIndex], 0, computeCollisions[timeStep], 2 * body); // position

                // Vel TO Coll
                addEdge(computeVelocities[currentTaskIndex], 0, computeCollisions[timeStep], 2 * body + 1); // velocity
            }
        }

        /* inter-time step edges (between two time steps) */
        for (int timeStep = 1; timeStep < timeSteps; timeStep++) { // add edges FROM timeStep-1 time step, to timeStep
            for (int body = 0; body < N; body++) {
                int previousTaskIndex = N * (timeStep - 1) + body;
                int currentTaskIndex = N * timeStep + body;
                // from Acceleration to Acceleration (propagates masses)
                for (int out = 1; out < NBodyWorkflow.N; out++) {
                    addEdge(computeAccelerations[previousTaskIndex], out, computeAccelerations[currentTaskIndex], out * 2);
                }

                // from Position to Acceleration (gives position of all bodies to acceleration similar to how initialize does it)
                int positionInputIndex = -1;
                for (int i = 0; i < N; i++) { // i represents the acceleration task instance for body i relative to this time step
                    if (i == body) {  // position goes to input 0
                        addEdge(computePositions[previousTaskIndex], 0, computeAccelerations[currentTaskIndex], 0);
                        positionIndex += 2 * body + 1;
                    } else {
                        addEdge(computePositions[previousTaskIndex], 0, computeAccelerations[currentTaskIndex], positionInputIndex);
                    }
                }

                // from Position to Position (position of current body)
                addEdge(computePositions[previousTaskIndex], 0, computePositions[currentTaskIndex], 0);

                // from Collision to Velocity (gives velocity at previous time step for a single body to its next time step task)
                addEdge(computeCollisions[timeStep - 1], body, computePositions[currentTaskIndex], 1);
                addEdge(computeCollisions[timeStep - 1], body, computeVelocities[currentTaskIndex], 0);
            }
        }

        // add edges to the final task (result writer) from the final outputs from the last time step
        for (int body = 0; body < N; body++) {
            // position
            addEdge(computePositions[(timeSteps - 1) * N + body], 0, writeResults, 3 * body);
            // velocity
            addEdge(computeCollisions[timeSteps - 1], body, writeResults, 3 * body + 1);
            // acceleration
            addEdge(computeAccelerations[(timeSteps - 1) * N + body], 0, writeResults, 3 * body + 2);
        }

        // TO wouts
        addEdge(writeResults, 0, 0);
        addEdge(writeResults, 1, 1);
        addEdge(writeResults, 2, 2);

    }
}
