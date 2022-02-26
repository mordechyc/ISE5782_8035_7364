package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry {

    private Point q0;
    private Vector normal;

    public Plane(Point vertex, Point vertex1, Point vertex2) {
        this.q0 = vertex;
        this.normal = null;
    }

    public Plane(Point p, Vector v) {
        this.q0 = p;
        this.normal = v.normalize();
    }

    public Point getQ0() {
        return q0;
    }

    public Vector getNormal() {
        return normal.normalize();
    }

    @Override
    public Vector getNormal(Point p) {
        return null;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "q0=" + q0 +
                ", normal=" + normal +
                '}';
    }
}
