package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

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
}