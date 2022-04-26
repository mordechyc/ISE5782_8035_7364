package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;


public class Sphere extends Geometry {

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
    public Vector getNormal(Point point) {
        //normal = normalize(point - center)
        Vector o_p = point.subtract(center);
        return o_p.normalize();
    }

    /**
     * Find the intersection points of a ray with a sphere
     *
     * @param ray The ray to check for intersections with.
     * @return A list of points.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> intersections = new LinkedList<>();

        //Save ray starting point and direction
        Point p0 = ray.getP0();
        Vector v = ray.getDir();

        double tm = 0;
        double d = 0; //d = distance
        //if ray does not start in center of sphere
        if (!p0.equals(center)) {
            //u is the vector from base of ray to center of sphere
            Vector u = center.subtract(p0);

            //find dot product of u and ray direction
            tm = u.dotProduct(v);

            //calculate distance
            d = alignZero(Math.sqrt(u.lengthSquared() - tm * tm));
        }

        //if distance os larger than radius of sphere clearly no intersections exist
        if (d >= radius) {
            return null;
        }

        double th = alignZero(Math.sqrt(radius * radius - d * d));

        double t1 = tm - th;
        double t2 = tm + th;

        if (t1 > 0 && t2 > 0) {
            Point p1 = p0.add(v.scale(t1));
            Point p2 = p0.add(v.scale(t2));
            intersections.add(new GeoPoint(this,p1));
            intersections.add(new GeoPoint(this,p2));

            return intersections;
        }
        if (t1 > 0) {
            Point p1 = p0.add(v.scale(t1));
            intersections.add(new GeoPoint(this,p1));
            return  intersections;
        }
        if (t2 > 0) {
            Point p2 = p0.add(v.scale(t2));
            intersections.add(new GeoPoint(this,p2));
            return  intersections;
        }
        return null;
    }

    /**
     * The toString() method is a method that returns a string representation of the object
     *
     * @return The string representation of the object.
     */
    public String toString() {
        return "Sphere{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }
}
