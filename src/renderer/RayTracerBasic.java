package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;
import java.util.List;

public class RayTracerBasic extends RayTracerBase  {

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = new Double3(1.0);
    private static final double EPS = 0.1;
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
        //List<GeoPoint> intersections =scene.geometries.findGeoIntersections(ray);
        //if ray finds intersection return color for pixel

        //if (intersections != null){
        //    GeoPoint closest = ray.findClosestGeoPoint(intersections);
       //     return calcColor(closest,ray);
     //   }
        //if no intersections exist
       // return  scene.background;
        GeoPoint closest = findClosestIntersection(ray);
        if(closest==null)
            return  scene.background;
        return calcColor(closest,ray);
    }

    /**
     * find closest intersection to the starting point of the ray
     * @param ray the ray that intersect with the geometries of the scene
     * @return the GeoPoint that is point is the closest point to the starting point of the ray
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        if(ray == null){
            return null;
        }

        List<GeoPoint> points = scene.geometries.findGeoIntersections(ray);
        return ray.findClosestGeoPoint(points);
    }
    /**
     * Calculate the color of ray intersection with object
     *
     * @param point The point on the surface of the object.
     * @return Color of intersection
     */
     private Color calcColor(GeoPoint point,Ray ray)
    {
        return calcColor(point, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity());
       // return (point.geometry.getEmission().add(calcLocalEffects(point, ray)).add(scene.ambientLight.getIntensity ()));
   }
    private Color calcColor(GeoPoint intersection, Ray ray, int level, Double3 k) {
        Color color = intersection.geometry.getEmission().add(calcLocalEffects(intersection, ray, k));
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray.getDir(), level, k));
    }
    /**
     * Calculate the effects of lights
     *
     * @param intersection intersection
     * @param ray ray
     * @return The color resulted by local effecrs calculation
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray,Double3 k) {
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
                //if (unshaded(intersection,l,n,nv,lightSource)) {
                Double3 ktr = transparency(intersection, l,lightSource);
                if (ktr.product(k).biggerThan(MIN_CALC_COLOR_K) ){
                        Color lightIntensity = lightSource.getIntensity(intersection.point);
                    color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }

    private Color calcGlobalEffects(GeoPoint gp, Vector v, int level, Double3 k) {
        Color color = Color.BLACK;
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();
        Double3 kkr = material.kR.product(k);
        if (kkr.biggerThan(MIN_CALC_COLOR_K) )
        {
            v = v.subtract(n.scale(v.dotProduct(n) * 2));
            color = color.add(calcGlobalEffect(new Ray(gp.point, v, n), level, material.kR, kkr));
        }
        Double3 kkt = material.kT.product(k);
        if (kkt.biggerThan(MIN_CALC_COLOR_K) )
            color = color.add(calcGlobalEffect(new Ray(gp.point, v, n), level, material.kT, kkt));
        return color;
    }
    private Color calcGlobalEffect(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = findClosestIntersection (ray);
        return ((gp == null ? scene.background : calcColor(gp, ray, level-1, kkx)
        ).scale(kx));
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

    private boolean unshaded(GeoPoint gp, Vector l, Vector n, double nv,LightSource lightSource) {
        Vector lightDirection = l.scale(-1); // from point to light source
        double lightDistance = lightSource.getDistance(gp.point);
        //Vector epsVector = n.scale(nv < 0 ? DELTA : -DELTA);
        //Point point = gp.point.add(epsVector);
        Ray lightRay = new Ray(gp.point, lightDirection,n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay,lightDistance);
        return intersections == null; //ensure if is empty;
    }
    private Double3 transparency(GeoPoint geoPoint , Vector l, LightSource lightSource ){
        Vector lightDirection = l.scale(-1); // from point to light source
        // create a new ray that is sent from point to the light source
        Ray lightRay = new Ray(geoPoint.point, lightDirection, geoPoint.normal);
        // check if another geometry is blocking us by finding intersections
        var intersections = scene.geometries.findGeoIntersections(lightRay);

        if (intersections == null){
            return new Double3(1); // There is no shadow
        }

        // the distance from the light source to the point
        double lightDistance = lightSource.getDistance(geoPoint.point);
        Double3 ktr = new Double3(1);
        for (GeoPoint gp : intersections) {
            if (alignZero(gp.point.distance(geoPoint.point) - lightDistance) <= 0) {
                ktr = gp.geometry.getMaterial().kT.product(ktr); // The transparency of each intersection
                if (ktr.lowerThan( MIN_CALC_COLOR_K)){
                    return new Double3(0); // full shadow
                }
            }
        }
        return ktr;
    }
}
