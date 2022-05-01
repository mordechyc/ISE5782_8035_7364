package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource {

    private Point position;
    private double kC = 1;
    private double kL = 0;
    private double kQ = 0;

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

    /**
     * Sets the constant attenuation factor of the light.
     *
     * @param kC Constant attenuation factor
     * @return The object itself.
     */
    public PointLight setkC(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the light's constant attenuation factor to the given value and returns this light.
     *
     * @param kL The constant attenuation factor.
     * @return The object itself.
     */
    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * > Sets the quadratic attenuation factor
     *
     * @param kQ Constant attenuation factor
     * @return The object itself.
     */
    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
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
        return this.intensity.reduce(kC + (kL * (d)) + (kQ * (d * d)));
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
}
