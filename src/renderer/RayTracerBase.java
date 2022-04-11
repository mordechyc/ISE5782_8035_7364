package renderer;

import scene.Scene;

public abstract class RayTracerBase {
    public RayTracerBase(Scene scena) {
        this.scena = scena;
    }

    protected Scene scena;
}
