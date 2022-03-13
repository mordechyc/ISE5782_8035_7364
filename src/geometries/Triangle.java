package geometries;

import primitives.Point;
import primitives.Vector;

public class Triangle extends Polygon {

    /**
     * constractor
     *
     * @param p1 p1
     * @param p2 p2
     * @param p3 p3
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public Vector getNormal(Point point) {
        return super.getNormal(point);
    }

    /**
     * The function toString() is a method that returns a string representation of the triangle
     *
     * @return The string representation of the triangle.
     */
    @Override
    public String toString() {
        return "Triangle{" +
                "vertices=" + vertices +
                ", plane=" + plane +
                '}';
    }

}
