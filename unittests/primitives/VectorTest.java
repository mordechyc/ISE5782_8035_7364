package primitives;

import geometries.Polygon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Testing Vector Class
 *
 * @author Mordechy Cohen
 */
class VectorTest {

    Vector v1 = new Vector(1, 2, 3);
    Vector v2 = new Vector(-2, -4, -6);

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {

        // ============ Equivalence Partitions Tests ==============

        //TC01: Test that the result of add is proper
        assertEquals(v1.add(v2), new Vector(-1, -2, -3), "add() got wrong result ");

        // TC02: Test to check if throw exception for  ZERO vector
        assertThrows(IllegalArgumentException.class, () -> v1.add(new Vector(-1, -2, -3)), "add() for ZERO vector does not throw an exception");

        // =============== Boundary Values Tests ==================

    }


    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {

        // ============ Equivalence Partitions Tests ==============

        //TC01: Test that the result of scale is proper
        assertEquals(v1.scale(2), new Vector(2, 4, 6), "scale() got wrong result ");

        // TC02: Test to check if throw exception
        assertThrows(IllegalArgumentException.class, () -> v1.scale(0), "scale() for 0 does not throw an exception");

        // =============== Boundary Values Tests ==================

    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {

        // ============ Equivalence Partitions Tests ==============

        //TC01: Test that the result of dot product is proper
        assertEquals(v1.dotProduct(v2), -28d, "dotProduct() wrong result");

        // =============== Boundary Values Tests ==================

    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    public void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        Vector v2 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v2);

        // TC01: Test that length of cross-product is proper (orthogonal vectors taken
        // for simplicity)
        assertEquals(v1.length() * v2.length(), vr.length(), 0.00001, "crossProduct() wrong result length");

        // TC02: Test cross-product result orthogonality to its operands
        assertTrue(isZero(vr.dotProduct(v1)), "crossProduct() result is not orthogonal to 1st operand");
        assertTrue(isZero(vr.dotProduct(v2)), "crossProduct() result is not orthogonal to 2nd operand");

        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from cross-productof co-lined vectors
        Vector v3 = new Vector(-2, -4, -6);
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v3), "crossProduct() for parallel vectors does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {

        // ============ Equivalence Partitions Tests ==============

        //TC01: Test that the result of length Squared is proper
        assertEquals(v1.lengthSquared(), 14d, "lengthSquared() result is very wrong");

        // =============== Boundary Values Tests ==================

    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {

        // ============ Equivalence Partitions Tests ==============

        //TC01: Test to check if  the result of length is proper
        assertEquals(new Vector(0, 3, 4).length(), 5d, "length() is not what it should be");

        // =============== Boundary Values Tests ==================

    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {

        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(1, 2, 3);
        Vector u = v.normalize();
        //TC01: Test to check if the result of normalize function does create a new vector
        assertNotEquals(u, v, "normalize() function does not create a new vector");

        // TC02: Test to check if the result of vector is legal
        assertTrue(isZero(u.length() - 1), "normalize() result is not a unit vector");

        // =============== Boundary Values Tests ==================

    }
}
