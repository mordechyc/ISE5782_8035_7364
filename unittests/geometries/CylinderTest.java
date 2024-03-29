package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Cylinder Class
 *
 * @author Mordechy Cohen
 */
class CylinderTest {

    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC01 test point not on base
        Cylinder cyl = new Cylinder(
                new Ray(new Point(1, 0, 0), new Vector(0, 1, 0)), 1d,
                2d);
        Vector temp = cyl.getNormal(new Point(2, 1, 0));
        assertEquals(temp, new Vector(1, 0, 0), "getNormal() for side is incorrect");

        //TC02 test center of base bottom
        Vector temp1 = cyl.getNormal(new Point(1.5, 0, 0));
        assertEquals(temp1, new Vector(0, 1, 0), "getNormal() for side is incorrect");

        //TC03 test center of base top
        Vector temp2 = cyl.getNormal(new Point(1.5, 2, 0));
        assertEquals(temp2, new Vector(0, 1, 0), "getNormal() for side is incorrect");

        // =============== Boundary Values Tests ==================
        //TC01 test point on bottom
        temp = cyl.getNormal(new Point(1, 0, 0));
        assertEquals(temp, new Vector(0, 1, 0), "getNormal() for bottom base is incorrect");

        //TC02 test point on top
        temp = cyl.getNormal(new Point(1, 2, 0));
        assertEquals(temp, new Vector(0, 1, 0), "getNormal() for top base is incorrect");
    }
}