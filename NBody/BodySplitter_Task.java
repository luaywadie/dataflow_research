import dataview.models.*;

/**
 * This task reads the incoming list of data from either the initialization task, or the output of calculations of the
 * previous time step, and breaks it up in to the appropriate vectors for acceleration, velocity, and position
 * calculations.
 */
public class BodySplitter_Task extends Task {

    public BodySplitter_Task() {
        ins = new InputPort[1];
        ins[0] = new InputPort("Raw vector", Port.DATAVIEW_MathVector, "Port that takes the raw list of data which represent the " +
                "vectors of one body");
        outs = new OutputPort[4]; // output vector for position, velocity and acceleration, and the mass of the body
        outs[0] = new OutputPort("Position Vector", Port.DATAVIEW_MathVector, "The position vector of the body");
        outs[1] = new OutputPort("Velocity Vector", Port.DATAVIEW_MathVector, "The velocity vector of the body");
        outs[2] = new OutputPort("Acceleration Vector", Port.DATAVIEW_MathVector, "The velocity vector of the body");
        outs[3] = new OutputPort("Mass", Port.DATAVIEW_double, "The mass of the body");
    }

    @Override
    public void run() {
        double x, y, z;
        // check the Body.toString() method for the position of each component of each vector
        DATAVIEW_MathVector rawVectorList = (DATAVIEW_MathVector) ins[0].read();

        // create the position vector
        x = rawVectorList.get(0);
        y = rawVectorList.get(1);
        z = rawVectorList.get(2);
        Vector3D position = new Vector3D(x, y, z);
        System.out.println("POSITION: " + position.toString());

        // create the velocity vector
        x = rawVectorList.get(3);
        y = rawVectorList.get(4);
        z = rawVectorList.get(5);
        Vector3D velocity = new Vector3D(x, y ,z);
        System.out.println("VELOCITY: " + velocity.toString());

        // create the acceleration vector
        x = rawVectorList.get(6);
        y = rawVectorList.get(7);
        z = rawVectorList.get(8);
        Vector3D acceleration = new Vector3D(x, y, z);
        System.out.println("ACCELERATION: " + acceleration.toString());

        Double mass = rawVectorList.get(9);
        System.out.println("MASS: " + mass + "\n");

        outs[0].write(position);
        outs[1].write(velocity);
        outs[2].write(acceleration);
        outs[3].write(mass);
    }
}
