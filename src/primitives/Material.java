package primitives;

public class Material {

    public Double3 kD = Double3.ZERO;
    public Double3 kS = Double3.ZERO;
    public int Shininess = 0;

    /**
     * Set the diffuse color of the material to the given color and return the material.
     *
     * @param kD The diffuse color of the material.
     * @return The material itself.
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Set the diffuse coefficient to the given value.
     *
     * @param kD Diffuse reflectivity.
     * @return The material itself.
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Set the specular reflectance of this material to the given value.
     *
     * @param kS specular reflectivity
     * @return The material itself.
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Set the specular reflectance of the material to the given value.
     *
     * @param kS specular reflectivity
     * @return The material itself.
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * This function sets the shininess of the material.
     *
     * @param shininess The shininess of the material.
     * @return The material object itself.
     */
    public Material setShininess(int shininess) {
        this.Shininess = shininess;
        return this;
    }
}
