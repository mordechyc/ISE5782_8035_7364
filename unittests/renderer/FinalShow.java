package renderer;

import elements.*;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;
import scene.Scene;


public class FinalShow {
    private Scene scene = new Scene("Test scene");
    private Camera camera = new Camera(new Point3D(100, 100, 1500), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(200, 200).setDistance(1000);

    @Test
    public void finalShow() {
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.geometries.add( //
                new Plane(new Point3D(-150, -150, -200),
                        new Point3D(150, -150, -200),
                        new Point3D(75, 75, -200))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),
                new Plane(new Point3D(0, 300, 10),
                        new Point3D(0, 300, -200),
                        new Point3D(0, 0, -200))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),
                new Plane(new Point3D(0, 0, 10),
                        new Point3D(300, 0, -200),
                        new Point3D(0, 0, -200))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),
                new Plane(new Point3D(300, 0, 20),
                        new Point3D(200, 0, -200),
                        new Point3D(200, 300, -200))
                        .setMaterial(new Material().setKs(0.8).setShininess(60)),


                 new Sphere(30, new Point3D(100, 100, -150)) //
                .setEmission(new Color(java.awt.Color.BLUE)) //
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)) //
        );

        scene.lights.add( //
                new PointLight(new Color(700, 400, 400), new Point3D(40, 40, 115)) //
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add( //
                new SpotLight(new Color(700, 400, 400), new Point3D(5, 5, -195),new Vector(1,1,1)) //
                        .setKl(4E-4).setKq(2E-5));
        scene.lights.add( //
                new SpotLight(new Color(700, 400, 400), new Point3D(195, 5, -195),new Vector(-1,1,1)) //
                        .setKl(4E-4).setKq(2E-5));

        Render render = new Render() //
                .setImageWriter(new ImageWriter("aaaFinal", 600, 600)) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene))
                .setMultithreading(3).setDebugPrint()
                ;
        render.renderImage();
        render.writeToImage();
    }
}
