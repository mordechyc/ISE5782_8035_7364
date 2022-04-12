package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

/**
 * Manages Scene which includes background color, lights and geometries
 */
public class Scene {

    public final String name; //Scene name

    public Color background = Color.BLACK; //background color
    public AmbientLight ambientLight = new AmbientLight(); //ambient light
    public Geometries geometries = null; //objects

    /**
     * Constructor that takes in scene name
     * @param name name of scene
     */
    public Scene(String name) {
        this.name = name;
        this.geometries = new Geometries();
    }

    /**
     * Setter for background color
     * @param background background color of scene
     * @return this scene
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Setter for ambient light
     * @param ambientLight ambientLight of scene
     * @return this scene
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Setter for geometries
     * @param geometries geometries of scene
     * @return this scene
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }


}
