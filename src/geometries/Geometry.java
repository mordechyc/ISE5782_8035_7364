package geometries;

import primitives.Point;
import primitives.Vector;

public interface Geometry extends Intersectable  {

    /**
     * Given a point, return the normal vector to the surface at that point
     *
     * @param p The point to get the normal for.
     * @return The normal vector to the surface at the point p.
     */
    public Vector getNormal(Point p);
}
