package renderer;

import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;


public class FinalShow {
    private Scene scene = new Scene("Test scene");
    private Camera camera = new Camera(new Point(100, 100, 1500), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(200, 200).setViewPlaneDistance((1000));

    @Test
    public void finalShow() {
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), new Double3(0.15)));

        scene.geometries.add(
                new Plane(new Point(-150, -150, -200),
                        new Point(150, -150, -200),
                        new Point(75, 75, -200))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),
                new Plane(new Point(0, 300, 10),
                        new Point(0, 300, -200),
                        new Point(0, 0, -200))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),
                new Plane(new Point(0, 0, 10),
                        new Point(300, 0, -200),
                        new Point(0, 0, -200))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),
                new Plane(new Point(300, 0, 20),
                        new Point(200, 0, -200),
                        new Point(200, 300, -200))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),


                 new Sphere( new Point(100, 100, -150),30) //
                .setEmission(new Color(java.awt.Color.BLUE)) //
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)) //
        );

        scene.lights.add( //
                new PointLight(new Color(700, 400, 400), new Point(40, 40, 115)) //
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add( //
                new SpotLight(new Color(700, 400, 400), new Point(5, 5, -195),new Vector(1,1,1)) //
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add( //
                new SpotLight(new Color(700, 400, 400), new Point(195, 5, -195),new Vector(-1,1,1)) //
                        .setKl(4E-4).setKq(2E-5));

                ImageWriter imageWriter= new ImageWriter("Final", 600, 600); //
                camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene)).setAdaptiveSSAA(10)//.setMultithreading(3)
                .setMultithreading(3).setDebugPrint()
                .renderImage()
                .writeToImage();
    }
}
