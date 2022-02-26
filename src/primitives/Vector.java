package primitives;

import static primitives.Util.isZero;

public class Vector extends Point {

    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO))

            throw new IllegalArgumentException("ERROR - zero is not valid to vector");
    }

    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("ERROR - zero is not valid to vector");

    }

    @Override
    public Point add(Vector vector) {
        return new Vector(this.xyz.add(vector.xyz));
        //return super.add(vector);
    }

    public Vector scale(double d) {
        return new Vector(xyz.scale(d));
    }

    public double dotProduct(Vector v3) {
        Double3 abc = xyz.product(v3.xyz);
        return (abc.d1 + abc.d2 + abc.d3);
    }

    public Vector crossProduct(Vector v2) {
        double i = xyz.d2 * v2.xyz.d3 - xyz.d3 * v2.xyz.d2;
        double j = -(xyz.d1 * v2.xyz.d3 - xyz.d3 * v2.xyz.d1);
        double k = xyz.d1 * v2.xyz.d2 - xyz.d2 * v2.xyz.d1;
        return new Vector(i, j, k);
    }

    public double lengthSquared() {
        return (xyz.d1 * xyz.d1 + xyz.d2 * xyz.d2 + xyz.d3 * xyz.d3);
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public Vector normalize() {
        return new Vector(xyz.reduce(length()));
    }

    @Override
    public String toString() {
        return "Vector{" +
                super.toString()+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

}
