

/** provides 3D position vectors for the N-body problem **/
public class Vector3D {
    double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D plus(Vector3D rhs) {
        return new Vector3D(x + rhs.x, y + rhs.y, z + rhs.z);
    }

    public Vector3D minus(Vector3D rhs) {
        return new Vector3D(x - rhs.x, y - rhs.y, z - rhs.z);
    }

    public Vector3D times(double s) {
        return new Vector3D(s * x, s * y, s * z);
    }

    public double mod() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    // TODO: unused I think, so if it truly is, remove
    public static Vector3D makeOrigin() {
        return new Vector3D(0,0,0);
    }

    public String toString() {
        return x + ", " + y + ", " + z;
    }
}
