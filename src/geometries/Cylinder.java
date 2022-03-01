package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube {

    private double height;

    /**
     * constaractor
     *
     * @param axisRay
     * @param radius
     */
    public Cylinder(Ray axisRay, double radius) {
        super(axisRay, radius);
    }

    /**
     * Returns the height of the Cylinder
     *
     * @return The height of the Cylinder.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the normal vector at the given point
     *
     * @param p The point to evaluate the normal at.
     * @return The normal vector of the plane.
     */
    @Override
    public Vector getNormal(Point p) {
        return super.getNormal(p);
    }

    /**
     * It overrides the toString() method of the superclass.
     *
     * @return The toString() method returns a string representation of the Cylinder object.
     */
    @Override
    public String toString() {
        return "Cylinder{" +
                "height=" + height +
                super.toString() +
                '}';
    }
}
