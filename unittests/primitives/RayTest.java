package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

import java.util.LinkedList;
import java.util.List;

class RayTest {

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}
     */
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

        assertEquals(list.get(2),ray.findClosestPoint(list), "FindClosestPoint() when point in the middle don't work");

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