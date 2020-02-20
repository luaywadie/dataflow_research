import dataview.models.DATAVIEW_MathMatrix;
import dataview.models.DATAVIEW_MathVector;
import dataview.models.InputPort;
import dataview.models.OutputPort;
import dataview.models.Port;
import dataview.models.Task;

public class KBody_Velocity_Task extends Task {
	public KBody_Velocity_Task() {
		super("Compute Velocity","Compute the Velocity of a single body");

		// Inputs Ports
		ins = new InputPort[2];
		ins[0] = new InputPort("Velocity", Port.DATAVIEW_MathMatrix,
				"The velocity of body i");
		ins[1] = new InputPort("Acceleration",Port.DATAVIEW_MathMatrix,
				"The acceleration of body i");
		// Output Ports
		outs = new OutputPort[1];
		outs[0] = new OutputPort("Velocity",Port.DATAVIEW_MathMatrix,
				"Velocity of this Body");

	}

	@Override
	public void run() {
		// Read inputs from ins
		DATAVIEW_MathVector	inputVelocity = (DATAVIEW_MathVector) ins[0].read();
		DATAVIEW_MathVector inputAcceleration = (DATAVIEW_MathVector) ins[1].read();

		// Assign vectors to ins
		Vector3D vectorVelocity = new Vector3D(inputVelocity.get(0), inputVelocity.get(1),
												inputVelocity.get(2));
		Vector3D vectorAcceleration = new Vector3D(inputAcceleration.get(0),
				inputAcceleration.get(1), inputAcceleration.get(2));
		// Calculate the new Velocity
		Vector3D calculatedVelocity = vectorVelocity.plus(vectorAcceleration);

		// Assign it to out
		outs[0].write(calculatedVelocity);
	}
}
