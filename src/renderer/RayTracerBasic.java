package renderer;

import geometries.Geometry;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

public class RayTracerBasic extends RayTracerBase  {

    /**
     * Constructor. Receives a scene
     * @param scene scene
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * Takes a ray and traces it through the scene until intersection is found.
     * If no intersection exists return background color.
     *
     * @param ray the ray that we want to trace
     * @return The color of the closest point to the camera.
     */
    @Override
    public  Color traceRay(Ray ray){
        //Find the closest intersection point to ray
        List<GeoPoint> intersections =scene.geometries.findGeoIntersections(ray);
        //if ray finds intersection return color for pixel
        if (intersections != null){
            GeoPoint closest = ray.findClosestGeoPoint(intersections);
            return calcColor(closest);
        }
        //if no intersections exist
        return  scene.background;
    }


    /**
     * Calculate the color of ray intersection with object
     *
     * @param point The point on the surface of the object.
     * @return Color of intersection
     */
     private Color calcColor(GeoPoint point)
    {
        return scene.ambientLight.getIntensity().add(point.geometry.getEmission());
    }
}
