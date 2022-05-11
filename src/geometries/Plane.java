package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Plane extends Geometry {

    final private Point q0;
    final private Vector normal;

    /**
     * constractor
     *
     * @param vertex vertex
     * @param vertex1 vertex1
     * @param vertex2 vertex2
     */
    public Plane(Point vertex, Point vertex1, Point vertex2) {
        this.q0 = vertex;
        Vector U = vertex1.subtract(vertex);
        Vector V = vertex2.subtract(vertex);

        // if UxV = (0,0,0) this Plane not create because all 3 point on the same line
        Vector N = U.crossProduct(V);
        this.normal = N.normalize();
    }

    /**
     * constarctor
     *
     * @param p p
     * @param v v
     */
    public Plane(Point p, Vector v) {
        this.q0 = p;
        this.normal = v.normalize();
    }

    /**
     * Returns Q0
     *
     * @return The method returns a Point object that is a copy of the q0 field.
     */
    public Point getQ0() {
        return q0;
    }

    /**
     * Returns the normal vector of the plane
     *
     * @return The normal vector of the plane.
     */
    public Vector getNormal() {
        return normal;
    }

    /**
     * Returns the normal vector at the given point
     *
     * @param p The point to get the normal at.
     * @return Nothing.
     */
    public Vector getNormal(Point p) {
        return normal;
    }

    /**
     * If the ray starts from a point on the plane, then no intersections exist. If the ray is parallel to the plane, then
     * no intersections exist. If the ray intersects the plane, then the intersection point is returned
     *
     * @param ray The ray that we're checking for intersections with the plane.
     * @return A list of points.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> intersections = new LinkedList<>();
        //save beginning point of ray
        Point p0 = ray.getP0();
        //if ray starts from point on plane then no intersections exist so we can return null with no further calculations
        if (q0.equals(p0)) {
            return null;
        }

        double denominatorDot = alignZero(normal.dotProduct(ray.getDir()));

        //if denominatorDot is 0 that means ray is parallel to plane (which means no intersections exist), so we return null
        if (isZero(denominatorDot)) {
            return null;
        }

        double numeratorDot = normal.dotProduct(q0.subtract(p0));

        double t = alignZero(numeratorDot / denominatorDot);

        if (t <= 0 ||alignZero(t-maxDistance) >0){
            return null;
        }

        Point P = ray.getPoint(t);
        if (P != null) {
            intersections.add(new GeoPoint(this,P));
            return intersections;
        }
        return null;
    }


    /**
     * The function returns a string representation of the Plane object
     *
     * @return The string representation of the Plane object.
     */
    public String toString() {
        return "Plane{" +
                "q0=" + q0 +
                ", normal=" + normal +
                '}';
    }
}
