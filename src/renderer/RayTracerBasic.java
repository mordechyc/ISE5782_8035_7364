package renderer;

import geometries.Geometry;
import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;
import java.util.List;

public class RayTracerBasic extends RayTracerBase  {

    private static final double DELTA = 0.1;

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
            return calcColor(closest,ray);
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
     private Color calcColor(GeoPoint point,Ray ray)
    {
        return scene.ambientLight.getIntensity().add(point.geometry.getEmission()).add(calcLocalEffects(point, ray));
       // return scene.ambientLight.getIntensity().add(point.geometry.getEmission());
    }
    /**
     * Calculate the effects of lights
     *
     * @param intersection intersection
     * @param ray ray
     * @return The color resulted by local effecrs calculation
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
        Vector v = ray.getDir();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;
        int nShininess = intersection.geometry.getMaterial().Shininess;

        Double3 kd = intersection.geometry.getMaterial().kD;
        Double3 ks = intersection.geometry.getMaterial().kS;
        Color color = Color.BLACK;
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(intersection.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // checks if nl == nv
                if (unshaded(intersection,l,n,nv)) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point);
                    color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }


    /**
     * Calculates diffusive light
     * @param kd kd
     * @param l l
     * @param n n
     * @param lightIntensity lightIntensity
     * @return The color of diffusive effects
     */
    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color lightIntensity) {
        double ln = alignZero(l.dotProduct(n));
        if (ln < 0)
            ln = ln * -1;
        return lightIntensity.scale(kd.scale(ln));
    }

    /**
     * Calculate specular light
     * @param ks ks
     * @param l l
     * @param n n
     * @param v v
     * @param nShininess nShininess
     * @param lightIntensity lightIntensity
     * @return The color of specular reflection
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        Vector r = l.subtract(n.scale(l.dotProduct(n) * 2));
        double vr = alignZero(v.scale(-1).dotProduct(r));
        if (vr < 0)
            vr = 0;
        vr = Math.pow(vr, nShininess);
        return lightIntensity.scale(ks.scale(vr));
    }

    private boolean unshaded(GeoPoint gp, Vector l, Vector n, double nv) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector epsVector = n.scale(nv < 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);
        Ray lightRay = new Ray(point, lightDirection);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
        return intersections == null; //ensure if is empty;
    }
}
