package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube {

    private double height;

    public Cylinder(Ray axisRay, double radius) {
        super(axisRay, radius);
    }

    public double getHeight() {
        return height;
    }

    @Override
    public Vector getNormal(Point p) {
        return super.getNormal(p);
    }

    @Override
    public String toString() {
        return "Cylinder{" +
                "height=" + height +
                super.toString() +
                '}';
    }
}
