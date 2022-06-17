package renderer;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

public class AdaptiveSuperSamplingTest {
    private Scene scene = new Scene("Test scene");
    private Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(200, 200).setViewPlaneDistance(1000);

    /**
     * Produce a picture of a two triangles lit by a point light with a Sphere
     * producing a shading with soft shadows
     */
    @Test
    public void trianglesSpheresAdaptiveSuperSamplingSoftShadow() {
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), new Double3(0.15)));

        scene.geometries.add( //
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)), //
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)), //
                new Sphere(new Point(0, 0, -115), 30) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)) //
        );
        scene.lights.add( //
                new PointLight(new Color(700, 400, 400), new Point(40, 40, 115), 10
                ) //
                        .setKl(4E-4).setKq(2E-5));

        ImageWriter imageWriter = new ImageWriter("adaptive super sampling test", 600, 600); //
        camera
                .setRayTracer(new RayTracerBasic(scene))
                .setAdaptiveSSAA(3)
                .setMultithreading(3)
                .setDebugPrint()
        ;
        camera.setImageWriter(imageWriter);
        camera.renderImage();
        imageWriter.writeToImage();
    }

    @Test
    public void sphereTriangleInitial() {
        scene.geometries.add( //
                new Sphere(new Point(0, 0, -200), 60) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), //
                new Triangle(new Point(-70, -40, 0), new Point(-40, -70, 0), new Point(-68, -68, -4)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)) //
        );
        scene.lights.add( //
                new SpotLight(new Color(400, 240, 0), new Point(-100, -100, 200), new Vector(1, 1, -3), 5) //
                        .setKl(1E-5).setKq(1.5E-7));

        ImageWriter imageWriter = new ImageWriter("adaptive soft shadow Sphere Triangle smooth", 400, 400); //
        camera
                .setRayTracer(new RayTracerBasic(scene))
                .setMultithreading(3)
                .setDebugPrint()
                .setAdaptiveSSAA(7)
        ;
        camera.setImageWriter(imageWriter);
        camera.renderImage();
        imageWriter.writeToImage();
    }
}
