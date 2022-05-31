package primitives;


public class Vector extends Point {

    /**
     * A constructor that calls the super constructor and checks if the vector is zero.
     *
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
     * A constructor that calls the super constructor and checks if the vector is  equal zero then throw exaption.
     *
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
    public Vector add(Vector vector) {
        return new Vector(this.xyz.add(vector.xyz));
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
     * create vertical vector to this vector
     * @return vertical vector to this vector
     */
    public Vector createVerticalVector(){
        double x = getX(),
                y = getY(),
                z = getZ();
        if(y == 0 && z == 0){
            return new Vector(0,1,0);
        }
        return new Vector(-y, x, 0)
                .normalize();
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
                super.toString() +
                '}';
    }
    /**
     * rotate the vectors by Rodrigues' rotation formula:
     * vRot = V * cos(theta) + (K x V) * sin(theta) + K * (K*V) * (1 - cos(theta))
     * V is this vector
     * @param k the axis vector of rotation
     * @param cosTheta cos(theta)
     * @param sinTheta sin(theta)
     */
    public void rotateVector(Vector k,  double cosTheta, double sinTheta) {
        Vector vRot;
        if (cosTheta == 0d) {
            vRot = k.crossProduct(this).scale(sinTheta);
        }
        else {
            vRot = this.scale(cosTheta);
            if (sinTheta != 0d) {
                vRot = vRot.add(k.crossProduct(this).scale(sinTheta));
            }
        }
        //xyz.s = vRot.normalize().xyz;
    }
}
