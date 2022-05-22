package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import java.util.List;

public interface LightSource {
    /**
     * Get the direction of the light from a point
     * @param p the point
     * @return the direction
     */
    List<Vector> getListL(Point p);
    public Color getIntensity(Point p);
    public Vector getL(Point p);
    double getDistance(Point point);
}
