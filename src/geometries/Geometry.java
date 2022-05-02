package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

public abstract class Geometry extends Intersectable  {
    protected Color emission = Color.BLACK;
    private Material material=new Material();

    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the emission color of the geometry and returns the geometry.
     *
     * @param emission The color of the light emitted by the material.
     * @return The Geometry object itself.
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * This function returns the emission color of the material
     *
     * @return The emission color of the material.
     */
    public Color getEmission() {
        return emission;
    }


    /**
     * Given a point, return the normal vector to the surface at that point
     *
     * @param p The point to get the normal for.
     * @return The normal vector to the surface at the point p.
     */
    public abstract Vector getNormal(Point p);
}
