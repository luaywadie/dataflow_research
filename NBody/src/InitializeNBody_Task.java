
import dataview.models.*;

/**
 * Input from: Text files Input to: ComputeAcceleration_Task,
 * Compute_Position_Task, ComputeVelocity_Task Parallel with:
 * ComputeVelocity_Task
 */

public class InitializeNBody_Task extends Task {

	public InitializeNBody_Task() {
		super("Initialize N-Body", "Reads the input and creates the array of bodies");

		// input ports
		ins = new InputPort[3];
		ins[0] = new InputPort("positions", Port.DATAVIEW_MathMatrix, "The initial positions of the N-bodies");
		ins[1] = new InputPort("velocities", Port.DATAVIEW_MathMatrix, "The initial velocities of the N-bodies");
		ins[2] = new InputPort("masses", Port.DATAVIEW_MathVector, "The masses of the N-bodies (CONSTANT)");
		// output ports
		outs = new OutputPort[NBodyWorkflow.N * 3];
		for (int i = 0; i < NBodyWorkflow.N; i++) {
			outs[3 * i + 0] = new OutputPort("Position", Port.DATAVIEW_MathVector, "Position of body " + i);
			outs[3 * i + 1] = new OutputPort("Velocity", Port.DATAVIEW_MathVector, "Velocity of body " + i);
			outs[3 * i + 2] = new OutputPort("Mass", Port.DATAVIEW_MathVector, "Mass of body " + i);
		}
	}

	@Override
	public void run() {
		DATAVIEW_MathMatrix initialPositions = (DATAVIEW_MathMatrix) ins[0].read();
		DATAVIEW_MathMatrix initialVelocities = (DATAVIEW_MathMatrix) ins[1].read();
		DATAVIEW_MathVector masses = (DATAVIEW_MathVector) ins[2].read();

		// body represents a row in the input matrix (and each row represents a body)
		for (int body = 0; body < NBodyWorkflow.N; body++) {
			//System.err.println("BODY " + body);
			// x, y, z values are given all in a row in that order
			Vector3D position = new Vector3D(initialPositions.get(body, 0), initialPositions.get(body, 1),
					initialPositions.get(body, 2));
			Vector3D velocity = new Vector3D(initialVelocities.get(body, 0), initialVelocities.get(body, 1),
					initialVelocities.get(body, 2));
			double mass = masses.get(body);

			outs[3 * body + 0].write(position);
			// System.out.println("POSITION OF BODY " + body + ": " + position);
			outs[3 * body + 1].write(velocity);
			// System.out.println("VELOCITY OF BODY " + body + ": " + velocity);
			outs[3 * body + 2].write(mass);
			// System.out.println("MASS OF BODY " + body + ": " + mass + "\n");
		}
	}
}