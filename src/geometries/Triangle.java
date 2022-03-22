package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.alignZero;

import java.util.List;

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

    public Vector getNormal(Point point) {
        return super.getNormal(point);
    }
    /**
     * find intersections point with the triangle
     * v1 = p1 - p0 <br/>
     * v2 = p2 - p0 <br/>
     * v3 = p3 - p0 <br/>
     * n1 = normalize(v1xv2) <br/>
     * n2 = normalize(v2xv3) <br/>
     * n3 = normalize(v3xv1) <br/>
     * let v be the direction of the rey
     * if v*ni (i between 1 to 3) is have the sing (+/-)
     * there is intersection points with the triangle
     * @param ray ray that cross the triangle
     * @return list of intersection points that were found
     */
    @Override
    public List<Point> findIntsersections(Ray ray) {
        Point p0 = ray.getP0();
        Vector v = ray.getDir();

        var result = plane.findIntsersections(ray);

        // if there is no intersections with the plane is a fortiori (kal&homer)
        // that there is no intersections with the triangle
        if(result == null){
            return null;
        }

        Vector v1 = this.vertices.get(0).subtract(p0),
                v2 = this.vertices.get(1).subtract(p0),
                v3 = this.vertices.get(2).subtract(p0);

        Vector n1 = v1.crossProduct(v2).normalize(),
                n2 = v2.crossProduct(v3).normalize(),
                n3 = v3.crossProduct(v1).normalize();

        double x1 = alignZero(v.dotProduct(n1)),
                x2 = alignZero(v.dotProduct(n2)),
                x3 = alignZero(v.dotProduct(n3));

        boolean allNegative = x1 < 0 && x2 < 0 && x3 < 0;
        boolean allPositive = x1 > 0 && x2 > 0 && x3 > 0;

        if(allNegative || allPositive){
            return List.of(result.get(0)); // return the intersections with the plane that the triangle is on
        }
        return null;
    }

    /**
     * The function toString() is a method that returns a string representation of the triangle
     *
     * @return The string representation of the triangle.
     */
    public String toString() {
        return "Triangle{" +
                "vertices=" + vertices +
                ", plane=" + plane +
                '}';
    }

}
