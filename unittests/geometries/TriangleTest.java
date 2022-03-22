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
    //TC01: Inside polygon/triangle(1 Point)
    @Test
    void findIntersections1() {
        Ray ray = new Ray(new Point(0, 0, -1), new Vector(1, 1, 1));
        List<Point> result = t.findIntsersections(ray);
        Point p1 = new Point(1, 1, 0);
        assertEquals(List.of(p1), result, "Inside polygon/triangle(1 Point)");
    }

    //TC02: Outside against edge(0 Point)
    @Test
    void findIntersections2() {
        Ray ray = new Ray(new Point(0, 0, -1), new Vector(2, 1, 1));
        assertNull(t.findIntsersections(ray), "Outside against edge");
    }

    //TC03: Outside against vertex(0 Point)
    @Test
    void findIntersections3() {
        Ray ray = new Ray(new Point(0, 0, -1), new Vector(3, -0.5, 1));
        assertNull(t.findIntsersections(ray), "Outside against vertex");
    }

    // =============== Boundary Values Tests ==================
    //****Three cases (the ray begins "before" the plane)**

    //TC04: On edge(0 Point)
    @Test
    void findIntersections4() {
        Ray ray = new Ray(new Point(0, 0, -1), new Vector(0, 1, 1));
        assertNull(t.findIntsersections(ray), "On edge");
    }

    //TC05: In vertex(0 Point)
    @Test
    void findIntersections5() {
        Ray ray = new Ray(new Point(0, 0, -1), new Vector(0, 3, 1));
        assertNull(t.findIntsersections(ray), "In vertex");
    }

    //TC06: On edge's continuation(0 Point)
    @Test
    void findIntersections6() {
        Ray ray6 = new Ray(new Point(0, 0, -1), new Vector(0, 4, 1));
        assertNull(t.findIntsersections(ray6), "On edge's continuation");
    }


}