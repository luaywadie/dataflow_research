import dataview.models.*;

public class BodyReconstruct_Task extends Task {
    public BodyReconstruct_Task() {
        super("Body Reconstruction", "Recombine position, velocity, acceleration, and mass in to 1 vector");
        ins = new InputPort[4];
        ins[0] = new InputPort("Position", Port.DATAVIEW_MathVector, "The position vector");
        ins[1] = new InputPort("Velocity", Port.DATAVIEW_MathVector, "The velocity vector");
        ins[2] = new InputPort("Acceleration", Port.DATAVIEW_MathVector, "The acceleration vector");
        ins[3] = new InputPort("Mass", Port.DATAVIEW_double, "The mass of the body");

        outs = new OutputPort[1];
        outs[0] = new OutputPort("Body Single-Vector", Port.DATAVIEW_MathVector, "The single vector representing position, velocity, acceleration and mass.");
    }

    @Override
    public void run() {
        DATAVIEW_MathVector rawPosition = (DATAVIEW_MathVector) ins[0].read();
        double positionX = rawPosition.get(0);
        double positionY = rawPosition.get(1);
        double positionZ = rawPosition.get(2);
        Vector3D position = new Vector3D(positionX, positionY, positionZ);

        DATAVIEW_MathVector rawVelocity = (DATAVIEW_MathVector) ins[1].read();
        double velocityX = rawVelocity.get(0);
        double velocityY = rawVelocity.get(1);
        double velocityZ = rawVelocity.get(2);
        Vector3D velocity = new Vector3D(velocityX, velocityY, velocityZ);

        DATAVIEW_MathVector rawAcceleration = (DATAVIEW_MathVector) ins[2].read();
        double accelerationX = rawAcceleration.get(0);
        double accelerationY = rawAcceleration.get(1);
        double accelerationZ = rawAcceleration.get(2);
        Vector3D acceleration = new Vector3D(accelerationX, accelerationY, accelerationZ);

        double mass = (double) ins[3].read();

        outs[0].write(new Body(position, velocity, acceleration, mass));
    }
}
