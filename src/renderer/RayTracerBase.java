package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

public abstract class RayTracerBase {
    protected Scene scena;
    public RayTracerBase(Scene scena) {
        this.scena = scena;
    }
    /**
     * Trace the ray and calculates the color
     * @param ray the ray that came out of the camera
     * @return the color of the object that the ray is interact with
     */
    public abstract  Color traceRay(Ray ray);

}
