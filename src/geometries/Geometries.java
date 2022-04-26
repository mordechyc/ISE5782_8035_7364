package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable {

    private List<Intersectable> geometries;

    /**
     * Find the intersections of the ray with all the intersectables in the scene
     *
     * @param ray The ray to test for intersections.
     * @return A list of points.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> result = null;

        for (Intersectable item : geometries) {
            List<GeoPoint> itemPoints = item.findGeoIntersectionsHelper(ray);
            if (itemPoints != null) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(itemPoints);
            }
        }

        return result;
    }

    /**
     *     This is the default constructor for the class.
     *     It creates a new list of intersectables.
     */
    public Geometries() {
        this.geometries = new LinkedList<>();
    }
    /**
     * Geometries
     * @param geometries intersectables
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }
    /**
     * add
     * @param geometries intersectables
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(Arrays.asList(geometries));
    }
}
