package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {

    private Color intensity; //Intensity of ambient light color

    /**
     * Constructor
     * @param Ia intensity color
     * @param Ka constant for intensity
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        this.intensity = Ia.scale(Ka);
    }

    /**
     *  default constructor :
     *  This is a constructor that sets the intensity to black.
     */
    public AmbientLight() {
        this.intensity = Color.BLACK;
    }

    /**
     * This function returns the intensity of the light.
     *
     * @return The intensity of the light.
     */
    public Color getIntensity() {
        return intensity;
    }
}
