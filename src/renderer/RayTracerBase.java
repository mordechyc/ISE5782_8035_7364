package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

public abstract class RayTracerBase {

    protected Scene scene; //objects of Scene

    /**
     * Constructor. Receives a scene
     * @param scene scene
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * abstract method
     * Trace the ray and calculates the color
     * @param ray the ray that came out of the camera
     * @return the color of the object that the ray is interact with
     */
    public abstract  Color traceRay(Ray ray);

}
