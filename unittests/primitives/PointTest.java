package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class
 * @author Mordechy Cohen Avichai Kadosh
 */
class PointTest {

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    public void testAdd() {
        Point p1 = new Point(1,2,3);
        // ============ Equivalence Partitions Tests ==============
        Vector v=new Vector(1,2,3);
        Point newP=new Point(2,4,6);
        //TC01: Test that the result of add is proper
        assertEquals(newP,p1.add(v),"add() got wrong result ");

        // =============== Boundary Values Tests ==================


    }

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    public void subtract() {
        Point p1 = new Point(1,2,3);
        // ============ Equivalence Partitions Tests ==============
        Vector v=new Vector(1,2,3);
        Point newP=new Point(0,0,0);
        //TC01: Test that the result of substract is proper
        assertEquals(newP,p1.subtract(v),"substract() got wrong result ");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void distanceSquared() {
        Point p1 = new Point(1,1,2);
        Point p2 = new Point(1,4,6);

        // ============ Equivalence Partitions Tests ==============
        double newDistanceSquered=25;
        //TC01: Test that the result of substract is proper
        assertEquals(p1.distanceSquared(p2),newDistanceSquered,"distanceSquared() got wrong result ");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void distance() {
        Point p1 = new Point(1,1,2);
        Point p2 = new Point(1,4,6);

        // ============ Equivalence Partitions Tests ==============
        double newDistanceSquered=5;
        //TC01: Test that the result of substract is proper
        assertEquals(p1.distance(p2),newDistanceSquered,"distance() got wrong result ");
    }
}