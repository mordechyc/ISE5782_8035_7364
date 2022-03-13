package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        //============ Equivalence Partitions Tests ==============//
        //T01: Testing that getNormal returns the right vector
        Sphere sp = new Sphere(new Point(1, 1, 0), 1);
        assertEquals(sp.getNormal(new Point(2, 1, 0)), new Vector(1, 0, 0), "getNormal() is bad");
    }
}