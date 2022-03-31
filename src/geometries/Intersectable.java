package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

public interface Intersectable {

    /**
     * Find all the points where the given ray intersects the given sphere
     *
     * @param ray The ray to test for intersections.
     * @return A list of points.
     */
    public List<Point> findIntersections(Ray ray);
}
