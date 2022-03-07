package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry {

    final private Point q0;
    final private Vector normal;

    /**
     * constractor
     * @param vertex
     * @param vertex1
     * @param vertex2
     */
    public Plane(Point vertex, Point vertex1, Point vertex2) {
        this.q0 = vertex;
        this.normal = null;
    }

    /**
     * constarctor
     * @param p
     * @param v
     */
    public Plane(Point p, Vector v) {
        this.q0 = p;
        this.normal = v.normalize();
    }

    /**
     * Returns Q0
     *
     * @return The method returns a Point object that is a copy of the q0 field.
     */
    public Point getQ0() {
        return q0;
    }

    /**
     * Returns the normal vector of the plane
     *
     * @return The normal vector of the plane.
     */
    public Vector getNormal() {
        return normal;
    }

    /**
     * Returns the normal vector at the given point
     *
     * @param p The point to get the normal at.
     * @return Nothing.
     */
    @Override
    public Vector getNormal(Point p) {
        return null;
    }

    /**
     * The function returns a string representation of the Plane object
     *
     * @return The string representation of the Plane object.
     */
    @Override
    public String toString() {
        return "Plane{" +
                "q0=" + q0 +
                ", normal=" + normal +
                '}';
    }
}
