import dataview.models.DATAVIEW_BigFile;
import dataview.models.Task;
import dataview.models.Workflow;


public class KBody_Workflow extends Workflow {
    public final String POSITION_INPUT = "positions.input";  // positions are inputs and outputs
    public final String POSITION_OUTPUT = "positions.output";
    public final String VELOCITY_INPUT = "velocities.input"; // velocities are inputs and outputs
    public final String VELOCITY_OUTPUT = "velocities.output";
    public final String MASS_INPUT = "masses.input";  // masses are only input, not input
    public final String ACCELERATION_OUTPUT = "accelerations.output"; // accelerations are only output, not input

    static int k = 3;  // number of gravitational bodies
    static double gc = 0.05;  // gravitational constant
    static int timeSteps = 20;  // not currently implemented (no mechanism to iterate workflows)

    public KBody_Workflow() {
        super("K-Body Problem", "Computes the gravitational interaction of K massive bodies.");
        wins = new Object[3];
        wins[0] = new DATAVIEW_BigFile(POSITION_INPUT);
        wins[1] = new DATAVIEW_BigFile(VELOCITY_INPUT);
        wins[2] = new DATAVIEW_BigFile(MASS_INPUT);

        wouts = new Object[9];
        for (int i = 0; i < wouts.length; i++) {
            wouts[i] = new DATAVIEW_BigFile("INIT_TEST_" + i + ".test");
        }
//        wouts = new Object[3];
    }

    public void design() {
        /* instantiate the tasks */
        // initialization
        Task task_initialize = addTask("KBody_Initialize_Task");
        // acceleration
//        Task[] tasks_acceleration= addTasks("KBody_Acceleration_Task", k);
//        // velocity
//        Task[] tasks_velocity = addTasks("KBody_Velocity_Task", k);
//        // position
//        Task[] tasks_position = addTasks("KBody_Position_Task", k);
//        // collisions
//        Task[] task_collisions = addTasks("KBody_Collisions_Task", k);
//        // results
//        Task[] task_results = addTasks("KBody_Results_Task", k);

        /* create edges between tasks */
        // input files -> KBody_Initialize_Task
        addEdge(0, task_initialize, 0);
        addEdge(1, task_initialize, 1);
        addEdge(2, task_initialize, 2);

        // KBody_Initialize_Task -> TEST OUTS
        for (int body = 0; body < k; body ++) {
            addEdge(task_initialize, 3 * body, 3 * body);
            addEdge(task_initialize, 3 * body + 1, 3 * body + 1);
            addEdge(task_initialize, 3 * body + 2, 3 * body + 2);
        }

    }
}
