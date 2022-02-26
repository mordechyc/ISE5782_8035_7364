package geometries;

import primitives.Point;
import primitives.Vector;

public class Sphere implements Geometry{

    private Point center;
    private double radius;

    public Sphere(Point center, double radius) {
        this.center = center;
        if (radius<=0)
            throw new IllegalArgumentException("radius must be bigger then zero");
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public Vector getNormal(Point p) {
        return null;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }
}
