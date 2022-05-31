package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Tube extends Geometry {

    protected Ray axisRay;
    protected double radius;

    /**
     * A constructor
     *
     * @param axisRay //Ray that goes through the height of tube
     * @param radius  /Radius of tube
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
     *
     * @param point Point on tube
     * @return Normal in point
     */
    public Vector getNormal(Point point) {
        //t = v∙(p − p0)
        //o = p0 + t∙v
        //n = normalize(P - o)
        Vector p0_p = point.subtract(axisRay.getP0());
        Vector v = axisRay.getDir();
        double t = v.dotProduct(p0_p);

        //if point is on rim then dot product will return 0, in which case the normal is p0_p
        if (t == 0) {
            return p0_p.normalize();
        }

        v = v.scale(t);
        Point o = axisRay.getP0().add(v);
        Vector o_p = point.subtract(o);
        return o_p.normalize();
    }


    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        return null;
    }

    /**
     * The function toString() is a method that returns a string representation of the object.
     *
     * @return The string representation of the object.
     */
    public String toString() {
        return "Tube{" +
                "axisRay=" + axisRay +
                ", radius=" + radius +
                '}';
    }
}
