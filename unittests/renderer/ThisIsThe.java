package renderer;


import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;


public class ThisIsThe {
    private Scene scene = new Scene("Test scene");
    private Camera camera = new Camera(new Point(100, 100, 1500), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(200, 200).setViewPlaneDistance(1000);

    @Test
    public void thisisthe() {
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), new Double3(0.15)));

        scene.geometries.add( //
                //Walls
                //back wall
                new Plane(new Point(-150, -150, -500),
                        new Point(150, 150, -500),
                        new Point(-150, 150, -500))
                        .setEmission(new Color(110, 55, 0))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),
                //left side wall
                new Plane(new Point(0, 200, 0),
                        new Point(0, 200, -200),
                        new Point(0, 0, -200))
                        .setEmission(new Color(50, 5, 50))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),
                //floor
                new Plane(new Point(0, 0, 0),
                        new Point(200, 0, -200),
                        new Point(0, 0, -200))
                        .setEmission(new Color(5, 80, 5))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),
                //right side wall
                new Plane(new Point(200, 0, 20),
                        new Point(200, 0, -200),
                        new Point(200, 200, -200))
                        .setEmission(new Color(50, 5, 50))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),
                //ceiling
                new Plane(new Point(0, 200, 0),
                        new Point(0, 200, -200),
                        new Point(200, 200, -200))
                        .setEmission(new Color(5, 50, 50))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),

                //upper sphere
                new Sphere(new Point(100, 100, -250), 40)  //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30).setkR(new Double3(1))), //
                //bottom back sphere
                new Sphere(new Point(155, 40, 150), 40)  //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30).setKd(new Double3(1))), //
                //bottom front sphere
                new Sphere(new Point(180, 20, 250), 20)  //
                        .setEmission(new Color(150, 15, 45))
                        .setMaterial(new Material().setKd(0.5).setKs(0.2).setShininess(10).setkR(new Double3(0.2))), //

                //pyramid
                new Triangle(new Point(45, 95, 145), new Point(40, 0, 300),
                        new Point(90, 0, 140)) //
                        .setEmission(new Color(4, 45, 45)) //
                        .setMaterial(new Material().setkR(new Double3(0.5))),
                new Triangle(new Point(45, 95, 145), new Point(40, 0, 300),
                        new Point(5, 0, 140)) //
                        .setEmission(new Color(4, 45, 45)) //
                        .setMaterial(new Material().setkR(new Double3(0.5))),
                new Triangle(new Point(45, 95, 145), new Point(5, 0, 140),
                        new Point(90, 0, 140))
                        .setEmission(new Color(4, 45, 45)).setMaterial(new Material().setkR(new Double3(0.5))));
        //.setMaterial(new Material().setkR( new Double3(0.5);

        scene.lights.add( //
                new PointLight(new Color(255, 255, 255), new Point(20, 20, -300), 5) //
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add( //
                new PointLight(new Color(255, 255, 255), new Point(180, 20, -300), 5) //
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add( //
                new PointLight(new Color(255, 255, 255), new Point(180, 180, -300), 5) //
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add( //
                new PointLight(new Color(255, 255, 255), new Point(20, 180, -300), 5) //
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add( //
                new PointLight(new Color(255, 255, 255), new Point(100, 100, 300), 5) //
                        .setKl(4E-4).setKq(2E-5));


        ImageWriter imageWriter = new ImageWriter("FinalSoft&Smooth2", 600, 600); //
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene))
                .setMultithreading(3).setDebugPrint().setAdaptiveSSAA(3)
                .renderImage()
                .writeToImage();
    }
}