package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight extends Light {

    /**
     * Constructor
     * @param Ia intensity color
     * @param Ka constant for intensity
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        super(Ia.scale(Ka));
    }

    /**
     *  default constructor :
     *  This is a constructor that sets the intensity to black.
     */
    public AmbientLight() {
        super (Color.BLACK);
    }

}
