package geometries;

import primitives.Point;
import primitives.Vector;

public class Sphere implements Geometry {

    private final Point center;
    private final double radius;

    /**
     * constractor
     *
     * @param center center
     * @param radius radius
     */
    public Sphere(Point center, double radius) {
        this.center = center;
        if (radius <= 0)
            throw new IllegalArgumentException("radius must be bigger then zero");
        this.radius = radius;
    }

    /**
     * Returns the center of the circle
     *
     * @return The center of the circle.
     */
    public Point getCenter() {
        return center;
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
     * Returns the normal vector
     *
     * @param point point
     * @return normal
     */
    @Override
    public Vector getNormal(Point point) {
        //normal = normalize(point - center)
        Vector o_p = point.subtract(center);
        return o_p.normalize();
    }

    /**
     * The toString() method is a method that returns a string representation of the object
     *
     * @return The string representation of the object.
     */
    @Override
    public String toString() {
        return "Sphere{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }
}
