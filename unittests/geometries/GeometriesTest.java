package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Geometries Class
 *
 * @author Mordechy Cohen
 */
class GeometriesTest {

    /**
     * Test method for {@link geometries.Geometries#findIntersections(Ray)}.
     */
    @Test
    void findIntersections() {

        Geometries geos = new Geometries();

        // =============== Boundary Values Tests ==================

        //TC01: Empty collection
        assertNull(geos.findIntersections(new Ray(new Point(2, 2, 2), new Vector(1, 1, 1))));

        //TC02: No shape is intersected
        geos.add(new Sphere(new Point(2, 0, 0.5), 1d));
        geos.add(new Triangle(new Point(5, 0, 0), new Point(6, 4, 0), new Point(10, 0, 2)));
        geos.add(new Plane(new Point(11, 0, 0), new Point(11, 5, 0), new Point(11, 0, 5)));

        assertNull(geos.findIntersections(new Ray(new Point(3, 2, 2), new Vector(0, 1, 1))));

        //TC03: Only one object is intersected
        List<Point> result = geos.findIntersections(new Ray(new Point(10.5, 1, 1),
                new Vector(1, 0, 0)));
        assertEquals(1, result.size(), "Wrong number of points");

        //TC05: All shapes are intersected
        result = geos.findIntersections(new Ray(new Point(0, 0.9, 0.5),
                new Vector(1, 0, 0)));
        assertEquals(4, result.size(), "Wrong number of points");

        // ============ Equivalence Partitions Tests ==============

        //TC04: Some shapes - but not all - are intersected
        result = geos.findIntersections(new Ray(new Point(4, 1, 1),
                new Vector(1, 0, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
    }
}