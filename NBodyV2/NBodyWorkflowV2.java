import dataview.models.DATAVIEW_BigFile;
import dataview.models.Task;
import dataview.models.Workflow;

/**
 * Adapted from N-Body problem code found at
 * https://rosettacode.org/wiki/N-body_problem#Java
 * 
 * 
 */

public class NBodyWorkflowV2 extends Workflow {
	public final String POSITION_INPUT = "positions.input";
	public final String VELOCITY_INPUT = "velocities.input";
	public final String MASS_INPUT = "masses.input";
	public final String POSITION_OUTPUT = "positions.output";
	public final String VELOCITY_OUTPUT = "velocities.output";
	public final String ACCELERATION_OUTPUT = "accelerations.output";

	/*
	 * PE: Constant Folding Constants for the workflow; could also put these as
	 * input files, but this would not be ideal for partial evaluation.
	 */
	public static final double gc = 0.05; // gravitational constant
	public static final int N = 3; // number of bodies
	public static final int timeSteps = 2; // time to advance each iteration

	public NBodyWorkflowV2() {
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
		/**
		 * TASKS
		 * 
		 * @Note: Tasks that are only used once (like initializeTaskNBody_Task and
		 *        NBodyResultWriter_Task) are assigned to Task variables. Tasks that are
		 *        used in every time step require a new task instance in every time
		 *        step. These tasks instance are held in a 2D Task array with dimensions
		 *        timeSteps X Number of Bodies. This allows for easily specifying a task
		 *        instance in any time step when assigning edges.
		 * 
		 * @Note: Since there is only one ComputeCollisions_Task instance in each time
		 *        step, these task instances are held in a 1D array.
		 * 
		 * @Note: A time step can be thought of as a workflow iteration
		 */

		// 0: Initialize Task
		Task initializeTask = addTask("InitializeNBody_Task");

		// 1: Compute Acceleration Tasks with Time Steps
		Task[][] computeAccelerationTasks = new Task[timeSteps][N];
		for (int timeStep = 0; timeStep < timeSteps; ++timeStep) {
			computeAccelerationTasks[timeStep] = addTasks("ComputeAcceleration_Task", N);
		}

		// 2: Compute Velocity Tasks with Time Steps
		Task[][] computeVelocityTasks = new Task[timeSteps][N];
		for (int timeStep = 0; timeStep < timeSteps; ++timeStep) {
			computeVelocityTasks[timeStep] = addTasks("ComputeVelocity_Task", N);
		}

		// 3: Compute Position Tasks with Time Steps
		Task[][] computePositionTasks = new Task[timeSteps][N];
		for (int timeStep = 0; timeStep < timeSteps; ++timeStep) {
			computePositionTasks[timeStep] = addTasks("ComputePosition_Task", N);
		}

		// 4: Compute Collisions Tasks with Time Steps
		Task[] computeCollisionTasks = addTasks("ComputeCollisions_Task", timeSteps);

		// 5: Write Results Task
		Task writeResultsTask = addTask("NBodyResultWriter_Task");

		/**
		 * EDGES
		 */

		// Position Input File to InitializeTask
		addEdge(0, initializeTask, 0);
		// Velocity Input File to InitializeTask
		addEdge(1, initializeTask, 1);
		// Mass Input File to InitializeTask
		addEdge(2, initializeTask, 2);

		/**
		 * The body of this loop creates edges between all the tasks in the workflow
		 * 
		 * @Note: A time step can be thought of as a workflow iteration
		 */
		for (int timeStep = 0; timeStep < timeSteps; ++timeStep) {
			/**
			 * The body of this if statement executes on the first time step. All task
			 * inputs (excluding computeCollision) come from the intializeTask in the first
			 * time step. Subsequent task inputs come from tasks in the previous time step
			 */
			if (timeStep == 0) {
				/**
				 * InitializeTask to ComputeAcceleration_Tasks (at time step 0). A
				 * ComputeAcceleration_Tasks instance exists for each body at each time step.
				 * 
				 * @Note: All ComputeAcceperation_Tasks at each time step must be given edges
				 *        before any other task in that timeStep is given edges because the
				 *        output of computeAcceleration_Tasks are used as inputs for different
				 *        tasks within the same time step.
				 * 
				 */
				for (int taskInstance = 0; taskInstance < N; taskInstance++) {
					int currentInputPort = 1;
					for (int body = 0; body < N; body++) {

						if (body == taskInstance) {
							addEdge(initializeTask, 3 * body, computeAccelerationTasks[timeStep][taskInstance], 0);

						} else {
							addEdge(initializeTask, 3 * body, computeAccelerationTasks[timeStep][taskInstance],
									currentInputPort++);
							addEdge(initializeTask, 3 * body + 2, computeAccelerationTasks[timeStep][taskInstance],
									currentInputPort++);
						}
					}

					/**
					 * The body of this loop creates edges for the remaining tasks that exist at
					 * time step 0. These tasks include: ComputePosition_Tasks (One instance for
					 * each body) ComputeVelocity_Tasks (One instance for each body)
					 * ComputeCollision_Task (Only one instance) *
					 */
					for (int body = 0; body < N; body++) {

						// InitializeTask to ComputePosiiton_Tasks
						// InitializeTask to ComputePosiiton_Tasks
						// ComputeAcceleration_Tasks to ComputePosiiton_Tasks
						addEdge(initializeTask, 3 * body, computePositionTasks[timeStep][body], 0); // Position
						addEdge(initializeTask, 3 * body + 1, computePositionTasks[timeStep][body], 1); // Velocities
						addEdge(computeAccelerationTasks[timeStep][body], 0, computePositionTasks[timeStep][body], 2); // Acceleration

						// InitializeTask to ComputeVelocity_Tasks
						// ComputeAcceleration_Tasks to ComputeVelocity_Tasks
						addEdge(initializeTask, 3 * body + 1, computeVelocityTasks[timeStep][body], 0); // Velocity
						addEdge(computeAccelerationTasks[timeStep][body], 0, computeVelocityTasks[timeStep][body], 1); // Acceleration

						// ComputePosition_Task to ComputeCollision_Task
						// ComputeVelocity_Task to ComputeCollision_Task
						addEdge(computePositionTasks[timeStep][body], 0, computeCollisionTasks[timeStep], 2 * body); // Position
						addEdge(computeVelocityTasks[timeStep][body], 0, computeCollisionTasks[timeStep], 2 * body + 1); // Velocity
					}
				}
			}

			/**
			 * This else block executes on every time step after time step 0. The body of
			 * this else block creates edges for all the tasks in the current time step.
			 * Most of these edges come from tasks in the previous time step.
			 */

			else {

				/**
				 * ComputeAcceleration_Tasks. A ComputeAcceleration_Tasks instance exists for
				 * each body at each time step.
				 * 
				 * @Note: All ComputeAcceperation_Tasks at each time step must be given edges
				 *        before any other task in that timeStep is given edges because the
				 *        output of computeAcceleration_Tasks are used as inputs for different
				 *        tasks within the same time step.
				 * 
				 */
				for (int taskInstance = 0; taskInstance < N; taskInstance++) {
					int currentInputPort = 1;
					for (int body = 0; body < N; body++) {
						/**
						 * This if statement ensures input port 0 for each task instance in the current
						 * iteration receives the position of the body for which acceleration is being
						 * computed
						 */
						if (body == taskInstance) {
							addEdge(computePositionTasks[timeStep-1][body], 0,
									computeAccelerationTasks[timeStep][taskInstance], 0); // Position

						} else {
							addEdge(computePositionTasks[timeStep-1][body], 0,
									computeAccelerationTasks[timeStep][taskInstance], currentInputPort++); // Position
							addEdge(initializeTask, 3 * body + 2, computeAccelerationTasks[timeStep][taskInstance],
									currentInputPort++); // Mass (always comes from initialize)
						}
					}
				}
				/**
				 * The body of this loop creates edges for the remaining tasks that exist at in
				 * the current time step. These tasks include: ComputePosition_Tasks (One
				 * instance for each body) ComputeVelocity_Tasks (One instance for each body)
				 * ComputeCollision_Task (Only one instance).
				 */
				for (int body = 0; body < N; body++) {
					// ComputeCollision_Task (from previous time step) to ComputePosiiton_Tasks
					// ComputeVelocity_Task (from previous time step) to ComputePosiiton_Tasks
					// ComputeAcceleration_Task to ComputePosiiton_Tasks
					addEdge(computePositionTasks[timeStep - 1][body], 0, computePositionTasks[timeStep][body], 0); // Position
					addEdge(computeCollisionTasks[timeStep - 1], body, computePositionTasks[timeStep][body], 1); // Velocity
					addEdge(computeAccelerationTasks[timeStep][body], 0, computePositionTasks[timeStep][body], 2); // Acceleration

					// ComputeVelocity_Task (from previous time step) to ComputeVelocity_Tasks
					// ComputeAcceleration_Task to ComputeVelocity_Tasks
					addEdge(computeCollisionTasks[timeStep - 1], body, computeVelocityTasks[timeStep][body], 0); // Velocity
					addEdge(computeAccelerationTasks[timeStep][body], 0, computeVelocityTasks[timeStep][body], 1);// Acceleration

					// ComputePosition_Task to ComputeCollision_Task
					// ComputeVelocity_Task to ComputeCollision_Task
					addEdge(computePositionTasks[timeStep][body], 0, computeCollisionTasks[timeStep], 2 * body); // Position
					addEdge(computeVelocityTasks[timeStep][body], 0, computeCollisionTasks[timeStep], 2 * body + 1); // Velocity
				}

			}
		}

		// NBodyResultWriter_Task
		for (int body = 0; body < N; body++) {
			// Final ComputePosition_Task to NBodyResultWriter_Task
			// Final ComputeCollision_Task to NBodyResultWriter_Task
			// Final ComputeAcceleration_Task to NBodyResultWriter_Task
			addEdge(computePositionTasks[timeSteps - 1][body], 0, writeResultsTask, 3 * body); // Position
			addEdge(computeCollisionTasks[timeSteps - 1], body, writeResultsTask, 3 * body + 1); // Velocity
			addEdge(computeAccelerationTasks[timeSteps - 1][body], 0, writeResultsTask, 3 * body + 2); // Acceleration

		}

		// Write to output files
		addEdge(writeResultsTask, 0, 0);
		addEdge(writeResultsTask, 1, 1);
		addEdge(writeResultsTask, 2, 2);

	}

}
