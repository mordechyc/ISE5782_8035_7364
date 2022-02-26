package primitives;

import java.util.Objects;

public class Point {

    protected Double3 xyz;

    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(xyz, point.xyz);
    }

    public Point add(Vector vector) {
        return new Point(this.xyz.add(vector.xyz));
    }

    public Vector subtract(Point p1) {
        return new Vector(this.xyz.subtract(p1.xyz));
    }

    public double distanceSquared(Point p) {
        return (((p.xyz.d1 - this.xyz.d1) * (p.xyz.d1 - this.xyz.d1)) + ((p.xyz.d2 - this.xyz.d2) * (p.xyz.d3 - this.xyz.d3)));
    }

    public double distance(Point p) {
        return (Math.sqrt(distanceSquared(p)));
    }

    @Override
    public String toString() {
        return "Point{" +
                "xyz=" + xyz.toString() +
                '}';
    }
}

