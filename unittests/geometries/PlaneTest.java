package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {
    Plane plane = new Plane(
            new Point(0, 0, 1),
            new Point(0, 2, 0),
            new Point(1, 0, 0));
    /**
     *test for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testPlane() {
        try {
            new Plane(
                    new Point(1, 0, 0),
                    new Point(2, 0, 0),
                    new Point(4, 0, 0));
            fail("constructor crate plane with 3 point on the same line");
        } catch (IllegalArgumentException e) {
        }
    }
    @Test
    void getQ0() {
    }

    @Test
    void getNormal() {
        assertEquals(1, plane.getNormal(null).length());
    }
    @Test
    void getNormal2() {
        Plane p1 = new Plane(
                new Point(0,1,0),
                new Point(1,0,0),
                new Point(0,0,1));

        Plane p2 = new Plane(
                new Point(1,0,0),
                new Point(0,0,1),
                new Point(0,1,0));

        assertEquals(p1.getNormal(null), p2.getNormal(null));
    }
}