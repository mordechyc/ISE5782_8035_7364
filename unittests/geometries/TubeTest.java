package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //test normal for point on tube
        Tube tb = new Tube(new Ray(new Point(1, 0, 0), new Vector(0, 1, 0)), 1d);
        Vector temp = tb.getNormal(new Point(2, 1, 0));
        assertEquals(temp, new Vector(1, 0, 0), "getNormal() is bad");

        // =============== Boundary Values Tests ==================
        tb = new Tube(new Ray(new Point(1, 0, 0), new Vector(0, 1, 0)), 1d);
        temp = tb.getNormal(new Point(2, 0, 0));
        assertEquals(temp, new Vector(1, 0, 0), "getNormal() for point on base rim is bad");
    }
}