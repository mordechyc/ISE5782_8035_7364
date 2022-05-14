package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.io.DataInput;
import java.util.List;

public abstract class Intersectable {
    /**
     * Helper class
     * In order to get color for each geometry shape individually
     * within a scene, we want to calculate color
     * at a certain point for the geometry
     */
    public static class GeoPoint{
        /**
         * the geometry that we find the color of a certain point
         */
        public Geometry geometry;
        /**
         * the point on the geometry that we get the color from
         */
        public Point point;
        /**
         * the normal at the point on the geometry
         */
        public Vector normal;
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeoPoint geoPoint = (GeoPoint) o;
            return geometry.equals(geoPoint.geometry) && point.equals(geoPoint.point);
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }

        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.normal = this.geometry.getNormal(this.point);
        }
    }
    /**
     * Find all the points where the given ray intersects the given sphere
     *
     * @param ray The ray to test for intersections.
     * @return A list of points.
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null
                : geoList.stream().map(gp -> gp.point).toList();
    }

    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersectionsHelper(ray,Double.POSITIVE_INFINITY);
    }
    public final List<GeoPoint> findGeoIntersections(Ray ray,double maxDistance) {
        return findGeoIntersectionsHelper(ray,maxDistance);
    }

    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance);

}
