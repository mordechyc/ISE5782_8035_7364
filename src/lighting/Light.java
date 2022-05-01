package lighting;

import primitives.Color;

public abstract class Light {

    protected Color intensity; //Intensity of ambient light color

    protected Light(Color intensity) {
        this.intensity = intensity;
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
