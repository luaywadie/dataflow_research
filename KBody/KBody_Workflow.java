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

        wouts = new Object[3];
        wouts[0] = new DATAVIEW_BigFile("TEST_ACCEL_0.test");
        wouts[1] = new DATAVIEW_BigFile("TEST_ACCEL_1.test");
        wouts[2] = new DATAVIEW_BigFile("TEST_ACCEL_2.test");
    }

    public void design() {
        /* instantiate the tasks */
        // initialization
        Task task_initialize = addTask("KBody_Initialize_Task");
        // acceleration
        Task[] tasks_acceleration= addTasks("KBody_Acceleration_Task", k);
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


        /* TEST EDGES */
        // KBody_Initialize_Task -> KBody_Acceleration_Task
        int positionIndex = -1;
        int massIndex = - 1;
        for (int body = 0; body < k; body ++) {
            for (int taskInstance = 0; taskInstance < k; taskInstance++) {
                if (body == taskInstance) { // 0th input to instance i of Acceleration is always position of body i
                    addEdge(task_initialize, 3 * body, tasks_acceleration[taskInstance], 0);
                    positionIndex = body * 2 - 1;
                    massIndex = positionIndex + 1;
                } else {
                    addEdge(task_initialize, 3 * body, tasks_acceleration[taskInstance], positionIndex);
                    addEdge(task_initialize, 3 * body + 2, tasks_acceleration[taskInstance], massIndex);
                }
            }
        }


        // KBody_Acceleration_Task -> TEST OUT
        addEdge(tasks_acceleration[0], 0,0);
        addEdge(tasks_acceleration[1], 0,1);
        addEdge(tasks_acceleration[2], 0,2);
    }
}
