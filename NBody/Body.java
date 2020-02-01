public class Body {
    Vector3D position;
    Vector3D velocity;
    Vector3D acceleration;
    double mass;
    static Vector3D origin = new Vector3D(0,0,0);

    public Body(Vector3D position, Vector3D velocity, Vector3D acceleration, double mass) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.mass = mass;
    }

    /* written with the OutputPort.write(Object o) method in mind
       position vector will be indices 0, 1, 2 (x, y, z)
       velocity vector will be indices 3, 4, 5 (x, y, z)
       acceleration vector will be indices 6, 7, 8 (x, y, z)
       mass will be index 9
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(position.toString() + ", ");
        sb.append(velocity.toString() + ", ");
        sb.append(acceleration.toString() + ", ");
        sb.append(mass);

        return sb.toString();
    }
}