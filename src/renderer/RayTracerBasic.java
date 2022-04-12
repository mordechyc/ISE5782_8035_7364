package renderer;

import geometries.Geometry;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class RayTracerBasic extends RayTracerBase  {
    public RayTracerBasic(Scene scena) {
        super(scena);
    }
    public  Color traceRay(Ray ray){
        List<Point> intersections =scena.geometries.findIntersections(ray);
        if(intersections==null)
            return scena.background;
        return calcColor(ray.findClosestPoint(intersections));
    }
    public Color calcColor(Point point)
    {
        return scena.ambientLight.getIntensity();
    }
}
