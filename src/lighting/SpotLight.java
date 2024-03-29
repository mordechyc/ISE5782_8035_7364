package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Util;
import primitives.Vector;

public class SpotLight extends PointLight {

    private Vector direction;

    private double NarrowBeam = 1d;


    /**
     * Constructor
     *
     * @param intensity intensity
     * @param position position
     * @param direction direction
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    public SpotLight(Color c, Point pos, Vector direction, double radius) {
        super(c, pos, radius);
        this.direction = direction.normalize();
    }
    /**
     * > Sets the narrow beam of the spot light
     *
     * @param narrowBeam The angle of the narrow beam in degrees.
     * @return The object itself.
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        NarrowBeam = narrowBeam;
        return this;
    }
    /**
     * If the dot product between the light's direction and the vector from the light to the point is negative, return
     * black. Otherwise, return the light's intensity scaled by the dot product
     *
     * @param p the point on the surface
     * @return The intensity of the light at the point p.
     */
    @Override
    public Color getIntensity(Point p) {
        double dotProduct = Util.alignZero(direction.dotProduct(super.getL(p)));
        if (dotProduct <= 0)
            return Color.BLACK;
        if(NarrowBeam!=1)
           dotProduct= Math.pow(dotProduct,NarrowBeam);
        return super.getIntensity(p).scale(dotProduct);
    }

    /**
     * > This function returns the vector from the point p to the light source
     *
     * @param p The point on the surface of the object.
     * @return The vector from the light source to the point on the surface.
     */
    @Override
    public Vector getL(Point p) {
        return super.getL(p);
    }
}
