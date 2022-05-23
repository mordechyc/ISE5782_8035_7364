package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import static primitives.Util.alignZero;

import java.util.List;

/**
 * Class for handling the calculation of color the ray from camera returns
 */
public class RayTracerBasic extends RayTracerBase {

    private static final int MAX_CALC_COLOR_LEVEL = 10; //Number of ray bounces to calculate
    private static final double MIN_CALC_COLOR_K = 0.001; //Minimum level of impact calculation must have on color for calculation to continue
    private static final Double3 INITIAL_K = new Double3(1.0); //Starting value for ray impact
    private static final boolean SOFT_SHADOW=true   ;
    //private static final int NUM_OF_RAYS=10;
    private static final double EPS = 0.1;

    /**
     * Constructor. Receives a scene
     *
     * @param scene scene
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * Takes a ray and traces it through the scene until intersection is found.
     * If no intersection exists return background color.
     *
     * @param ray Ray to track and find color
     * @return Color for pixel
     */
    @Override
    public Color traceRay(Ray ray) {
        //Find closest intersection point to ray
        GeoPoint closest = findClosestIntersection(ray);
        //if ray finds intersection return hit color
        if (closest != null) {
            return calcColor(closest, ray);
        }
        //if no intersections exist
        return scene.background;
    }

    /**
     * find closest intersection to the starting point of the ray
     *
     * @param ray the ray that intersect with the geometries of the scene
     * @return the GeoPoint that is point is the closest point to the starting point of the ray
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        if (ray == null) {
            return null;
        }

        List<GeoPoint> points = scene.geometries.findGeoIntersections(ray);
        return ray.findClosestGeoPoint(points);
    }

    /**
     * Calculate the color of ray intersection with object
     *
     * @param point GeoPoint of intersection
     * @param ray   The ray that intersected object
     * @return Color of intersection
     */
    private Color calcColor(GeoPoint point, Ray ray) {
        return calcColor(point, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity());
    }

    /**
     * Calculate color of intersection point with lighting effects
     *
     * @param intersection GeoPoint of intersection
     * @param ray          The ray that intersected object
     * @param level        ray intersection depth (number of ray bounces)
     * @param k            Attenuation factor of the intersected ray
     * @return Color of intersection
     */
    private Color calcColor(GeoPoint intersection, Ray ray, int level, Double3 k) {
        //find emission color for intersected object and add local effects to color.
        //Local effects include specular and diffuse.
        Color color = intersection.geometry.getEmission().add(calcLocalEffects(intersection, ray, k));
        //if on final bounce return color result. Else add global effects to color.
        //Global effects include reflections and refractions.
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray.getDir(), level, k));
    }

    /**
     * Calculate the effects of lights
     *
     * @param intersection intersection
     * @param ray          ray
     * @return The color resulted by local effecrs calculation
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray, Double3 k) {
        Vector v = ray.getDir();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;
        int nShininess = intersection.geometry.getMaterial().Shininess;

        Double3 kd = intersection.geometry.getMaterial().kD;
        Double3 ks = intersection.geometry.getMaterial().kS;
        Color color = Color.BLACK;
        //get color given by every light source
        if (SOFT_SHADOW) {
            for (LightSource lightSource : scene.lights) {
                Color color1 = new Color(0, 0, 0);
                for (Vector l : lightSource.getListL(intersection.point)) {
                    double nl = alignZero(n.dotProduct(l));
                    if (nl * nv > 0) { // sign(nl) == sign(nv)
                        //get transparency of the object
                        Double3 ktr = transparency(intersection, l, lightSource);
                        if (ktr.product(k).biggerThan(MIN_CALC_COLOR_K)) { //check if the depth of calculation was reached then don't calculate any more
                            // color is scaled by transparency to get the right color effect
                            Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                            //get effects of the color and add them to the color
                            color1 = color1.add(calcDiffusive(kd, l, n, lightIntensity),
                                    calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                        }
                    }
                }
                color = color.add(color1.reduce(lightSource.getListL(intersection.point).size()));
            }
            
        }
        else {
            for (LightSource lightSource : scene.lights) {
                Vector l = lightSource.getL(intersection.point);
                double nl = alignZero(n.dotProduct(l));
                if (nl * nv > 0) { // checks if nl == nv
                    //if (unshaded(intersection,l,n,nv,lightSource)) {
                    Double3 ktr = transparency(intersection, l, lightSource);
                    if (ktr.product(k).biggerThan(MIN_CALC_COLOR_K)) {
                        Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                        color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                                calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                    }
                }
            }
        }

        return color;
    }


    /**
     * Calculates global effects (reflections and refractions)
     *
     * @param gp    GeoPoint of intersection
     * @param v     the vector that intersected object
     * @param level ray intersection depth (number of ray bounces)
     * @param k     Attenuation factor of the intersected ray
     * @return Intersection color with effects included
     */
    private Color calcGlobalEffects(GeoPoint gp, Vector v, int level, Double3 k) {
        Color color = Color.BLACK;
        //save the material of the intersected object
        Material material = gp.geometry.getMaterial();
        //set the reflection attenuation factor
        Double3 kkr = material.kR.product(k);
        //save the normal at the intersection point
        Vector n = gp.geometry.getNormal(gp.point);
        //if difference in color is not too small
        if (kkr.biggerThan(MIN_CALC_COLOR_K)) {
            //find reflection
            v = v.subtract(n.scale(v.dotProduct(n) * 2));
            color = color.add(calcGlobalEffect(new Ray(gp.point, v, n), level, material.kR, kkr));
        }
        //set the refraction attenuation factor
        Double3 kkt = material.kT.product(k);
        //if difference in color is not too small
        if (kkt.biggerThan(MIN_CALC_COLOR_K))
            //calculate refraction
            color = color.add(calcGlobalEffect(new Ray(gp.point, v, n), level, material.kT, kkt));
        return color;
    }


    /**
     * It calculates the color of a ray by finding the closest intersection point, and then calculating the color of the
     * intersection point
     *
     * @param ray   the ray that we're currently tracing
     * @param level the recursion level
     * @param kx    the color of the current material
     * @param kkx   the attenuation factor of the light source
     * @return The color of the closest intersection point.
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = findClosestIntersection(ray);
        return ((gp == null ? scene.background : calcColor(gp, ray, level - 1, kkx)
        ).scale(kx));
    }

    /**
     * Calculate the diffuse of light in intersection point
     *
     * @param kd             Diffusiveness level
     * @param l              Dot product between normal in intersection point and normalized vector from light to intersection point
     * @param n              n
     * @param lightIntensity Light intensity
     * @return Diffuse color
     */
    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color lightIntensity) {
        double ln = alignZero(l.dotProduct(n));
        if (ln < 0)
            ln = ln * -1;
        //Calculate the diffuse level and return color with diffuse calculation added
        return lightIntensity.scale(kd.scale(ln));
    }

    /**
     * Calculate the specular color of light in intersection point
     *
     * @param ks             Specular level
     * @param l              Normalized vector from light to intersection point
     * @param n              Normal in intersection point
     * @param v              Direction of camera
     * @param nShininess     Level of shininess
     * @param lightIntensity Light intensity
     * @return Specular color
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        //Find vector from light source to camera
        Vector r = l.subtract(n.scale(l.dotProduct(n) * 2));
        //Save reverse vector of vector from camera
        double vr = alignZero(v.scale(-1).dotProduct(r));
        if (vr < 0)
            vr = 0;
        //Calculate specular intensity
        vr = Math.pow(vr, nShininess);
        return lightIntensity.scale(ks.scale(vr));
    }

    /**
     * =============maybe we need to delete this func=========
     *
     * @param gp          gp
     * @param l           l
     * @param n           n
     * @param nv          nv
     * @param lightSource lightSource
     * @return .
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, double nv, LightSource lightSource) {
        Vector lightDirection = l.scale(-1); // from point to light source
        double lightDistance = lightSource.getDistance(gp.point);
        //Vector epsVector = n.scale(nv < 0 ? DELTA : -DELTA);
        //Point point = gp.point.add(epsVector);
        Ray lightRay = new Ray(gp.point, lightDirection, n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay, lightDistance);
        return intersections == null; //ensure if is empty;
    }

    /**
     * Calculate shadow transparency
     *
     * @param lightSource The light source
     * @param l           Ray from the light source
     * @param geoPoint    Intersection point
     * @return Level of transparency
     */
    private Double3 transparency(GeoPoint geoPoint, Vector l, LightSource lightSource) {
        Vector lightDirection = l.scale(-1); // from point to light source
        // create a new ray that is sent from point to the light source
        Ray lightRay = new Ray(geoPoint.point, lightDirection, geoPoint.normal);
        // check if another geometry is blocking us by finding intersections
        var intersections = scene.geometries.findGeoIntersections(lightRay);
        if (intersections == null) {
            return new Double3(1); // There is no shadow
        }

        // the distance from the light source to the point
        double lightDistance = lightSource.getDistance(geoPoint.point);
        Double3 ktr = new Double3(1);
        for (GeoPoint gp : intersections) {
            if (alignZero(gp.point.distance(geoPoint.point) - lightDistance) <= 0) {//<=0
                ktr = gp.geometry.getMaterial().kT.product(ktr); // The transparency of each intersection
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                    return new Double3(0); // full shadow
                }
            }
        }
        return ktr;
    }
}
