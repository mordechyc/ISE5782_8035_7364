package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;

public class shadowTest {
    private Scene scene1 = new Scene("Test scene");
    private Scene scene2 = new Scene("Test scene") //
            .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));
    private Camera camera1 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(150, 150) //
            .setViewPlaneDistance(1000);
    private Camera camera2 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(200, 200) //
            .setViewPlaneDistance(1000);
    private Camera camera3 = new Camera(new Point(0, 0, 5000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(150, 150) //
            .setViewPlaneDistance(2000);
    private Point[] p = { // The Triangles' vertices:
            new Point(-110, -110, -150), // the shared left-bottom
            new Point(95, 100, -150), // the shared right-top
            new Point(110, -110, -150), // the right-bottom
            new Point(-75, 78, 100) }; // the left-top
    private Point trPL = new Point(30, 10, -100); // Triangles test Position of Light
    private Point spPL = new Point(-50, -50, 25); // Sphere test Position of Light
    private Color trCL = new Color(800, 500, 250); // Triangles test Color of Light
    private Color spCL = new Color(800, 500, 0); // Sphere test Color of Light
    private Vector trDL = new Vector(-2, -2, -2); // Triangles test Direction of Light
    private Material material = new Material().setKd(0.5).setKs(0.5).setShininess(300);
    private Geometry triangle1 = new Triangle(p[0], p[1], p[2]).setMaterial(material);
    private Geometry triangle2 = new Triangle(p[0], p[1], p[3]).setMaterial(material);
    private Geometry plane1 = new Plane(p[0], p[1], p[2]).setMaterial(material);
    private Geometry plane2 = new Plane(p[0], p[1], p[3]).setMaterial(material);
    private Geometry sphere = new Sphere(new Point(0, 0, -50), 50d) //
            .setEmission(new Color(BLUE).reduce(2)) //
            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300));
    private Geometry sphere1 = new Sphere(new Point(70, 70, -50), 30d) //
            .setEmission(new Color(253, 184, 19).reduce(2)) //
            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300));
    private Geometry sphere2 = new Sphere(new Point(50, 50, 75), 20d) //
            .setEmission(new Color(253, 184, 19).reduce(2)) //
            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300));
    private Geometry sphere3 = new Sphere(new Point(75, 75, 120), 12.5d) //
            .setEmission(new Color(red).reduce(2)) //
            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300));
    /**
     * Helper function for the tests in this module
     */
    private Intersectable sphere4 = new Sphere(new Point(0, 0, -200), 60d) //
            .setEmission(new Color(BLUE)) //
            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
    private Material trMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(30);
    private Scene scene = new Scene("Test scene");
    private Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(200, 200).setViewPlaneDistance(1000) //
            .setRayTracer(new RayTracerBasic(scene));
    void sphereTriangleHelper(String pictName, Triangle triangle, Point spotLocation) {
        scene.geometries.add(plane2,sphere, triangle.setEmission(new Color(BLUE)).setMaterial(trMaterial));
        scene.lights.add( //
                new SpotLight(new Color(400, 240, 0), spotLocation, new Vector(1, 1, -3),10) //
                        .setKl(1E-5).setKq(1.5E-7));
        camera.setImageWriter(new ImageWriter(pictName, 400, 400)) //
                .renderImage() //
                .writeToImage();
    }
    /**
     * Produce a picture of a two triangles lighted by a point light
     */
    @Test
    public void softShadowOnPlane() {

      /*  scene2.geometries.add( plane2,new Triangle(new Point(15, -10, -50), new Point(2, -10, -55), new Point(23, -10, -60)).setEmission(Color.MAGENTA)
,new Sphere(new Point(10,-10,-60),5).setEmission( new Color(red)),new Sphere(new Point(20,-10,30),5).setEmission(Color.CYAN));
        scene2.lights.add(new PointLight(trCL, new Point(30,-10,-45),5).setKl(0.001).setKq(0.0002));

        ImageWriter imageWriter = new ImageWriter("softShadowOnPlane1", 500, 500);
        camera2.rotate(0,   0,225).setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene2)) //
                .renderImage() //
                .writeToImage(); //
                */

	sphereTriangleHelper("shadowSphere", //
				new Triangle(new Point(-70, -40, 0), new Point(-40, -70, 0), new Point(-68, -68, -4)), //
				new Point(-76, -76, 0));

    }
}
