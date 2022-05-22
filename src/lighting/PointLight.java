package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PointLight extends Light implements LightSource {

    private Point position;
    protected double radius;
    private double Kc = 1;
    private double Kl = 0;
    private double Kq = 0;

    /**
     * Constructor
     *
     * @param intensity intensity
     * @param position  position
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }
    public PointLight(Color c, Point pos, double radius) {
        super(c);
        position = pos;
        this.radius = radius;
    }
    /**
     * Sets the constant attenuation factor of the light.
     *
     * @param kc Constant attenuation factor
     * @return The object itself.
     */
    public PointLight setKc(double kc) {
        this.Kc = kc;
        return this;
    }

    /**
     * Sets the light's constant attenuation factor to the given value and returns this light.
     *
     * @param kL The constant attenuation factor.
     * @return The object itself.
     */
    public PointLight setKl(double kL) {
        this.Kl = kL;
        return this;
    }

    /**
     * > Sets the quadratic attenuation factor
     *
     * @param kq Constant attenuation factor
     * @return The object itself.
     */
    public PointLight setKq(double kq) {
        this.Kq = kq;
        return this;
    }
    public PointLight setradius(double radius) {
        this.radius = radius;
        return this;
    }
    /**
     * The intensity of a point light source is inversely proportional to the square of the distance from the light source
     *
     * @param p The point in space we're trying to calculate the intensity at.
     * @return The intensity of the light at a given point.
     */
    @Override
    public Color getIntensity(Point p) {
        double d = p.distance(this.position);
        return this.intensity.reduce(Kc + (Kl * (d)) + (Kq * (d * d)));
        // return this.intensity.reduce(kC.add(kL.scale(d)).add(kQ .scale( d * d)));
    }

    /**
     * The light vector is the vector from the light to the point.
     *
     * @param p The point on the surface of the object that we're calculating the normal for.
     * @return The vector from the light source to the point on the surface.
     */
    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }
    /**
     * get distance of light from given point
     * @param point point
     * @return double
     */
    @Override
    public double getDistance(Point point) {
         return position.distance(point);
    }
    @Override
    public List<Vector> getListL(Point p) {
        Random r = new Random();
        List<Vector> vectors = new LinkedList();
        for (double i = -radius; i < radius; i += radius / 5) {
            for (double j = -radius; j < radius; j += radius / 5) {
                if (i != 0 && j != 0) {
                    Point point = position.add(new Vector(i, 0.1d, j));
                    if (point.equals(position)){
                        vectors.add(p.subtract(point).normalize());
                    }
                    else{
                        try{
                            if (point.subtract(position).dotProduct(point.subtract(position)) <= radius * radius){
                                vectors.add(p.subtract(point).normalize());
                            }
                        }
                        catch (Exception e){
                            vectors.add(p.subtract(point).normalize());
                        }

                    }
                }

            }
        }
        vectors.add(getL(p));
        return vectors;
    }
}
