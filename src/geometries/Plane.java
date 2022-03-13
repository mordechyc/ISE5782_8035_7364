package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry {

    final private Point q0;
    final private Vector normal;

    /**
     * constractor
     *
     * @param vertex vertex
     * @param vertex1 vertex1
     * @param vertex2 vertex2
     */
    public Plane(Point vertex, Point vertex1, Point vertex2) {
        this.q0 = vertex;
        try {
            Vector U = vertex1.subtract(vertex);
            Vector V = vertex2.subtract(vertex);

            // if UxV = (0,0,0) this Plane not create because all 3 point on the same line
            Vector N = U.crossProduct(V);


            this.normal = N.normalize();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("can not create a plane with all 3 point on the same line");
        }
    }

    /**
     * constarctor
     *
     * @param p p
     * @param v v
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
        return normal;
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
