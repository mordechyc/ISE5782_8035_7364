package primitives;

import java.util.List;
import java.util.Objects;

import geometries.Intersectable.GeoPoint;

public class Ray {

    private final Point p0;
    private final Vector dir;
    private static final double DELTA = 0.1;

    /**
     * A constructor
     *
     * @param p0  p0
     * @param dir dir
     */
    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalize();
    }

    /**
     * constructor that takes in point, direction and additional vector for cases of reflection
     *
     * @param point     point
     * @param direction direction
     * @param n         n
     */
    public Ray(Point point, Vector direction, Vector n) {

        double delta = n.dotProduct(direction) >= 0d ? DELTA : -DELTA;
        p0 = point.add(n.scale(delta));
        dir = direction.normalize();
    }

    /**
     * This function returns the value of the private variable p0.
     *
     * @return The point p0.
     */
    public Point getP0() {
        return p0;
    }

    /**
     * This function returns the direction of the player.
     *
     * @return The direction of the ray.
     */
    public Vector getDir() {
        return dir;
    }

    /**
     * @param t scalar for multiplay
     *          p the point on the ray
     * @return P vector scale t
     */
    public Point getPoint(double t) {
        Vector tv = null;
        try {
            tv = dir.scale(t);
        } catch (Exception e) {
            return p0;
        }
        Point P = p0.add(tv);
        return P;
    }


    /**
     * compares ray with another object to see if equal
     *
     * @param o o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return Objects.equals(p0, ray.p0) && Objects.equals(dir, ray.dir);
    }

    /**
     * It returns a string representation of the ray.
     *
     * @return The string representation of the ray.
     */
    @Override
    public String toString() {
        return "Ray{" +
                "p0=" + p0 +
                ", dir=" + dir +
                '}';
    }

    /**
     * //find which intersection point is closest to ray
     *
     * @param points a list of points
     * @return A point
     */
    public Point findClosestPoint(List<Point> points) {
        if (points == null || points.isEmpty())
            return null;
        else return findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;

        /*
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;*/
    }

    /**
     * Finds the closest intersection point to ray's origin
     *
     * @param pointList - list of all intersections
     * @return closest point
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> pointList) {
        if (pointList == null) {
            return null;
        }

        GeoPoint closesPoint = null;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < pointList.size(); i++) {
            if (p0.distance(pointList.get(i).point) < minDistance) {
                minDistance = p0.distance(pointList.get(i).point);
                closesPoint = pointList.get(i);
            }
        }
        return closesPoint;
    }
}
