package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        Triangle pl = new Triangle(
                new Point(0, 0, 1),
                new Point(1, 0, 0),
                new Point(0, 1, 0));

        double sqrt3 = Math.sqrt(1d / 3);
        Vector result= pl.getNormal(new Point(0, 0, 1));
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3), result,"Bad normal to triangle");
    }
    Triangle t = new Triangle(
            new Point(2, 0, 0),
            new Point(0, 3, 0),
            new Point(0, 0, 0));

    // ============ Equivalence Partitions Tests ==============


    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(
                new Point(1,1,0),
                new Point(2,2,0),
                new Point(3,1,0));

        // ============ Equivalence Partitions Tests ==============
        //TC01: Inside triangle
        List<Point> result = triangle.findIntsersections(new Ray(new Point(2, 0.5, -1),
                new Vector(0, 1, 1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(result.get(0),new Point(2,1.5,0),"Ray crosses plane in wrong spot");

        //TC02: Outside against edge
        result = triangle.findIntsersections(new Ray(new Point(1, 1, -1),
                new Vector(0, 1, 1)));
        assertNull(result, "Wrong number of points");

        //TC03: Outside against vertex
        result = triangle.findIntsersections(new Ray(new Point(2, 1, -1),
                new Vector(0, 2, 1)));
        assertNull(result, "Wrong number of points");


        // =============== Boundary Values Tests ==================
        //**** Group: the ray begins "before" the plane
        //TC11: On edge
        result = triangle.findIntsersections(new Ray(new Point(2, 0, -1),
                new Vector(0, 1, 1)));
        assertNull(result, "Wrong number of points");

        //TC12: In vertex
        result = triangle.findIntsersections(new Ray(new Point(1, 0, -1),
                new Vector(0, 1, 1)));
        assertNull(result, "Wrong number of points");

        //TC13: On edge's continuation
        result = triangle.findIntsersections(new Ray(new Point(0, 0, -1),
                new Vector(0, 1, 1)));
        assertNull(result, "Wrong number of points");
    }

}