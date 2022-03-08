package primitives;


public class Vector extends Point {

    /**
     *     A constructor that calls the super constructor and checks if the vector is zero.
     * @param x x
     * @param y y
     * @param z z
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("ERROR - zero is not valid to vector");
    }

    /**
     *     A constructor that calls the super constructor and checks if the vector is  equal zero then throw exaption.
     * @param xyz xyz
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("ERROR - zero is not valid to vector");
    }

    /**
     * Tadds a vector on a vector to recieve a new combined vector
     *
     * @param vector The vector to add to this vector.
     * @return A new Vector object.
     */
    @Override
    public Point add(Vector vector) {
        return new Vector(this.xyz.add(vector.xyz));
        //return super.add(vector);
    }

    /**
     * multiplyes vector by a scale to receive a new vector
     *
     * @param d the scalar
     * @return A new Vector object.
     */
    public Vector scale(double d) {
        return new Vector(xyz.scale(d));
    }

    /**
     * method creates new point by multipltying two points
     *
     * @param v3 v3
     * @return new point by multipltying two points
     */
    public double dotProduct(Vector v3) {
        Double3 abc = xyz.product(v3.xyz);
        return (abc.d1 + abc.d2 + abc.d3);
    }

    /**
     * ethod that multiplies two vectors to recieve a vector that is vertical to both vectors
     *
     * @param v2 v2
     * @return multiplies two vectors to recieve a vector that is vertical to both vectors
     */
    public Vector crossProduct(Vector v2) {
        double i = xyz.d2 * v2.xyz.d3 - xyz.d3 * v2.xyz.d2;
        double j = -(xyz.d1 * v2.xyz.d3 - xyz.d3 * v2.xyz.d1);
        double k = xyz.d1 * v2.xyz.d2 - xyz.d2 * v2.xyz.d1;
        return new Vector(i, j, k);
    }

    /**
     * calculates and returns the square of the vectors length
     *
     * @return new vector
     */
    public double lengthSquared() {
        return (xyz.d1 * xyz.d1 + xyz.d2 * xyz.d2 + xyz.d3 * xyz.d3);
    }

    /**
     * alculates and returns the vectors length
     *
     * @return vectors length
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Given a vector, return a new vector with the same direction but with a length of 1
     *
     * @return A new Vector object.
     */
    public Vector normalize() {
        return new Vector(xyz.reduce(length()));
    }

    /**
     * It overrides the toString() method of the superclass.
     *
     * @return The string representation of the vector.
     */
    @Override
    public String toString() {
        return "Vector{" +
                super.toString()+
                '}';
    }

    /**
     * Returns true if the object is equal to this object
     *
     * @param o the object to compare against this object for equality.
     * @return The return type is boolean.
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

}
