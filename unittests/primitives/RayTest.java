package primitives;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {
    /**
     * Receiving the correct starting point of the ray
     */
    // ============ Equivalence Partitions Tests ==============
    @Test
    void testGetP0() {
        // TC01: test Receiving the correct starting point of the ray
        Point p = new Point(1,2,3);
        Vector v = new Vector(3,4,5);
        Ray r = new Ray(p,v);
        assertEquals(r.getP0(), p, "Get p0 Failed");
    }

    /**
     * Receiving the correct direction of the ray
     */
    @Test
    void testGetDir() {
        // TC02: test Receiving the correct direction of the ray
        Point p = new Point(1,2,3);
        Vector v = new Vector(3,4,5);
        Ray r = new Ray(p,v);
        assertEquals(r.getDir(), v.normalize(), "GetDir Failed");
    }


    Ray ray = new Ray(new Point(2,-2,3), new Vector(-2,-2,-2));

    @Test
    void testFindClosestPoint1(){
        // TC03: test that the closest point is in the middle of the list
        var points = List.of(
                new Point(0,0,1),
                new Point(0,-1,0),
                new Point(1,-2,3),
                new Point(1,1,1),
                new Point(1,2,3));

        assertEquals(points.get(points.size() / 2), ray.findClosestPoint(points));
    }

    // =============== Boundary Values Tests ==================
    // TC04: test if the list is empty
    @Test
    void testFindClosestPoint2() {
        List<Point> points = Collections.emptyList();
        assertNull(ray.findClosestPoint(points));
    }

    // TC05: test if the first point is the closest to p0
    @Test
    void testFindClosestPoint3() {
        var points = List.of(
                new Point(1,-2,3),
                new Point(0,-1,0),
                new Point(1,-2,3),
                new Point(1,1,1),
                new Point(1,2,3));
        assertEquals(points.get(0), ray.findClosestPoint(points));
    }

    // TC06: test if the last point is the closest to p0
    @Test
    void testFindClosestPoint4() {
        var points = List.of(
                new Point(1,-2,3),
                new Point(0,-1,0),
                new Point(1,-2,3),
                new Point(1,1,1),
                new Point(1,-2,3));
        assertEquals(points.get(points.size()-1), ray.findClosestPoint(points));
    }
}
/*
package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

import java.util.LinkedList;
import java.util.List;

class RayTest {

*
     * Test method for {@link primitives.Ray#findClosestPoint(List)}


    @Test
    void testFindClosestPoint() {

        Ray ray = new Ray(new Point(0,0,1),new Vector(1, 5, 10));

        // ============ Equivalence Partitions Tests ==============
        // TC01: the point is in the middle of the list
        List<Point> list = new LinkedList<>();
        list.add(new Point(5,25,51));
        list.add(new Point(3,15,31));
        list.add(new Point(1,5,11));
        list.add(new Point(2,10,21));
        Point point=ray.findClosestPoint(list);
        assertEquals(list.get(2),point, "FindClosestPoint() when point in the middle don't work");

        // =============== Boundary Values Tests ==================
        // TC02: the list is empty
        list.clear();
        assertNull(ray.findClosestPoint(list),"FindClosestPoint() when list equals null don't work");

        // TC03: the point is in the start of the list
        list = new LinkedList<>();
        list.add(new Point(1,5,11));
        list.add(new Point(5,25,51));
        list.add(new Point(3,15,31));
        list.add(new Point(2,10,21));

        assertEquals(list.get(0),ray.findClosestPoint(list),"FindClosestPoint() when point in the start don't work");

        // TC04: the point is in the end of the list
        list = new LinkedList<>();
        list.add(new Point(5,25,51));
        list.add(new Point(3,15,31));
        list.add(new Point(2,10,21));
        list.add(new Point(1,5,11));

        assertEquals(list.get(3),ray.findClosestPoint(list),"FindClosestPoint() when point in the end don't work");
    }

}
*/
