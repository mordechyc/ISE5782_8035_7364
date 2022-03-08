package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Tube implements Geometry {

    protected Ray axisRay;
    protected double radius;

    /**
     * A constructor
     *
     * @param axisRay //Ray that goes through the height of tube
     * @param radius /Radius of tube
     */
    public Tube(Ray axisRay, double radius) {
        this.axisRay = axisRay;
        if (radius <= 0)
            throw new IllegalArgumentException("radius must be bigger then zero");
        this.radius = radius;
    }


    /**
     * Returns the axis ray
     *
     * @return The axisRay is being returned.
     */
    public Ray getAxisRay() {
        return axisRay;
    }

    /**
     * Returns the radius of the circle
     *
     * @return The radius of the circle.
     */

    public double getRadius() {
        return radius;
    }

    /**
     * get normal of point on tube
     * @param p Point on tube
     * @return Normal in point
     */
    @Override
    public Vector getNormal(Point p) {
        return null;
    }

    /**
     * The function toString() is a method that returns a string representation of the object.
     *
     * @return The string representation of the object.
     */
    @Override
    public String toString() {
        return "Tube{" +
                "axisRay=" + axisRay +
                ", radius=" + radius +
                '}';
    }
}
