package primitives;

import java.util.Objects;

public class Point {

    final Double3 xyz;
    public static final Point ZERO = new Point(0, 0, 0);
    /**
     * This is a constructor.
     *
     * @param xyz xyz
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * This is a constructor with parms.
     *
     * @param x x
     * @param y y
     * @param z z
     */
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    /**
     * Returns true if the given object is equal to this object
     *
     * @param o the object to compare against
     * @return The boolean value of whether the two points are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(xyz, point.xyz);
    }

    /**
     * Add a vector to a point
     *
     * @param vector The vector to add to this point.
     * @return A new Point object.
     */
    public Point add(Vector vector) {
        return new Point(this.xyz.add(vector.xyz));
    }

    /**
     * Given a point, return a vector that is the difference between the point and the current point
     *
     * @param p1 The point to subtract from this point.
     * @return A new Vector object.
     */
    public Vector subtract(Point p1) {
        return new Vector(this.xyz.subtract(p1.xyz));
    }

    /**
     * Given a point, return the distance between the point and this point
     *
     * @param p The point to compare against.
     * @return The distance squared between the two points.
     */
    public double distanceSquared(Point p) {
        return (((p.xyz.d1 - this.xyz.d1) * (p.xyz.d1 - this.xyz.d1)) +
                ((p.xyz.d2 - this.xyz.d2) * (p.xyz.d2 - this.xyz.d2) +
                        (p.xyz.d3 - this.xyz.d3) * (p.xyz.d3 - this.xyz.d3)));
    }

    /**
     * Given a point, return the distance between that point and the point represented by this object
     *
     * @param p the point to which you are comparing the current point
     * @return The distance between the two points.
     */
    public double distance(Point p) {

        return (Math.sqrt(distanceSquared(p)));
    }

    /**
     * Returns the x coordinate of the point
     *
     * @return The x coordinate of the point.
     */
    public double getX(){
        return xyz.d1;
    }
    /**
     * Returns the y coordinate of the point
     *
     * @return The y coordinate of the point.
     */
    public double getY(){
        return xyz.d2;
    }
    /**
     * Returns the z coordinate of the point
     *
     * @return The z coordinate of the point.
     */
    public double getZ(){
        return xyz.d3;
    }

    /**
     * It returns a string representation of the Point object.
     *
     * @return The string representation of the Point object.
     */
    @Override
    public String toString() {
        return "Point{" +
                "xyz=" + xyz.toString() +
                '}';
    }

}

