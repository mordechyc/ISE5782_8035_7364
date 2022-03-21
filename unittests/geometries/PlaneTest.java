package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testPlane(){
        // =============== Boundary Values Tests ==================
        //test 2 point on same spot
        assertThrows(IllegalArgumentException.class,()->new Plane(
                        new Point(0, 0, 1),
                        new Point(0, 0, 1),
                        new Point(0, 1, 0)),
                "constructor for 3 points does not throw an exception in case of 2 identical points");

        //test all points on same line
        assertThrows(IllegalArgumentException.class,()->new Plane(
                        new Point(1, 1, 1),
                        new Point(2, 2, 2),
                        new Point(3, 3, 3)),
                "constructor for 3 points does not throw an exception in case of all points on same line");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        Plane pl = new Plane(
                new Point(0, 0, 1),
                new Point(1, 0, 0),
                new Point(0, 1, 0));

        double sqrt3 = Math.sqrt(1d / 3);
        Vector result= pl.getNormal(new Point(0, 0, 1));
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3), result,"Bad normal to plane");
    }

    /**
     * Test method for {@link geometries.Plane#findIntsersections(primitives.Ray)}.
     */
    @Test
    void testFindIntsersections() {
        Plane plane = new Plane(
                new Point(1,1,0),
                new Point(2,2,0),
                new Point(3,1,0));

        // ============ Equivalence Partitions Tests ==============
        //**** Group: The Ray must be neither orthogonal nor parallel to the plane
        //TC01: Ray intersects the plane
        List<Point> result = plane.findIntsersections(new Ray(new Point(2, 1, -1),
                new Vector(0, 1, 1)));
        assertEquals(1, result.size(), "Wrong number of points");

        //TC02: Ray does not intersect the plane
        result = plane.findIntsersections(new Ray(new Point(2, 1, -1),
                new Vector(0, 1, -1)));
        assertNull(result, "Wrong number of points");

        // =============== Boundary Values Tests ==================
        //**** Group: Ray is parallel to the plane
        //TC11: the ray is included in the plane
        result = plane.findIntsersections(new Ray(new Point(2, 2, 0),
                new Vector(1, -1, 0)));
        assertNull(result, "Wrong number of points");

        //TC12: the ray is not included in the plane
        result = plane.findIntsersections(new Ray(new Point(2, 2, 67),
                new Vector(1, -1, 0)));
        assertNull(result, "Wrong number of points");

        //**** Group: Ray is orthogonal to the plane
        //TC13: according to P0 - before the plane
        result = plane.findIntsersections(new Ray(new Point(1, 1, -1),
                new Vector(0, 0, 1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(result.get(0),new Point(1,1,0),"Ray crosses plane in wrong spot");

        //TC14: according to P0 - in the plane
        result = plane.findIntsersections(new Ray(new Point(1, 1, 0),
                new Vector(0, 0, 1)));
        assertNull(result, "Wrong number of points");

        //TC15: according to P0 - after the plane
        result = plane.findIntsersections(new Ray(new Point(1, 1, 1),
                new Vector(0, 0, 1)));
        assertNull(result, "Wrong number of points");


        //TC16: Ray is neither orthogonal nor parallel to and begins at the plane(P0 is in the plane, but not the ray)
        result = plane.findIntsersections(new Ray(new Point(2, 2, 0),
                new Vector(0, 1, 1)));
        assertNull(result, "Wrong number of points");

        //TC17: Ray is neither orthogonal nor parallel to the plane and begins in the same point which appears as reference point in the plane (Q)
        result = plane.findIntsersections(new Ray(new Point(2, 2, 2),
                new Vector(0, 1, 1)));
        assertNull(result, "Wrong number of points");
    }
}