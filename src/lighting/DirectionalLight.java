package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import java.util.List;

public class DirectionalLight extends Light implements LightSource {

    private Vector direction;

    /**
     * Constructor
     *
     * @param intensity intensity
     * @param direction direction
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    /**
     * It returns the intensity of the light source
     *
     * @param p The point in space where the light is being calculated.
     * @return The intensity of the light source.
     */
    @Override
    public Color getIntensity(Point p) {
        return this.intensity;
    }

    /**
     * Return the direction of the light source.
     *
     * @param p The point on the surface of the object that we're looking at.
     * @return The direction of the ray.
     */
    @Override
    public Vector getL(Point p) {
        return this.direction.normalize();
    }

    /**
     * get distance of light from given point
     * @param point
     * @return
     */
    @Override
    public double getDistance(Point point) {
        //directional light comes from everywhere (sort of) so we want to return a point that definitely is
        // farther away than any point the function may receive.
        return Double.POSITIVE_INFINITY;
    }
    @Override
    public List<Vector> getListL(Point p) {
        return List.of(getL(p));
    }
}
