package lighting;

import org.junit.jupiter.api.Test;

import lighting.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.Scene;
import static java.awt.Color.*;

/**
 * Test rendering a basic image
 *
 * @author Dan
 */

public class LightsTests {
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
	 * Produce a picture of a sphere lighted by a directional light
	 */
	@Test
	public void sphereDirectional() {
		scene1.geometries.add(sphere);
		scene1.lights.add(new DirectionalLight(spCL, new Vector(1, 1, -0.5)));

		ImageWriter imageWriter = new ImageWriter("lightSphereDirectional", 500, 500);
		camera1.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage() //
				.writeToImage(); //
	}

	/**
	 * Produce a picture of a sphere lighted by a point light
	 */
	@Test
	public void spherePoint() {
		scene1.geometries.add(sphere);
		scene1.lights.add(new PointLight(spCL, spPL,10).setKl(0.001).setKq(0.0002));

		ImageWriter imageWriter = new ImageWriter("lightSpherePoint", 500, 500);
		camera1.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage() //
				.writeToImage(); //
	}

	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void sphereSpot() {
		scene1.geometries.add(sphere);
		scene1.lights.add(new SpotLight(spCL, spPL, new Vector(1, 1, -0.5),10).setKl(0.001).setKq(0.0001));

		ImageWriter imageWriter = new ImageWriter("lightSphereSpot", 500, 500);
		camera1.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage() //
				.writeToImage(); //
	}

	/**
	 * Produce a picture of a two triangles lighted by a directional light
	 */
	@Test
	public void trianglesDirectional() {
		scene2.geometries.add(triangle1, triangle2);
		scene2.lights.add(new DirectionalLight(trCL, trDL));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesDirectional", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() //
				.writeToImage(); //
	}

	/**
	 * Produce a picture of a two triangles lighted by a point light
	 */
	@Test
	public void trianglesPoint() {
		scene2.geometries.add(triangle1, triangle2,new Sphere(new Point(20,50,-100),30).setEmission( new Color(red)));
		scene2.lights.add(new PointLight(trCL, trPL,10).setKl(0.001).setKq(0.0002));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesPoint", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() //
				.writeToImage(); //
	}

	/**
	 * Produce a picture of a two triangles lighted by a spot light
	 */
	@Test
	public void trianglesSpot() {
		scene2.geometries.add(triangle1, triangle2);
		scene2.lights.add(new SpotLight(trCL, trPL, trDL,10).setKl(0.001).setKq(0.0001));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesSpot", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() //
				.writeToImage(); //
	}

	/**
	 * Produce a picture of a sphere lighted by a narrow spot light
	 */
	@Test
	public void sphereSpotSharp() {
		scene1.geometries.add(sphere);
		scene1.lights
				.add(new SpotLight(spCL, spPL, new Vector(1, 1, -0.5),10).setNarrowBeam(5).setKl(0.001).setKq(0.00004));

		ImageWriter imageWriter = new ImageWriter("lightSphereSpotSharp", 500, 500);
		camera1.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage() //
				.writeToImage(); //
	}

	/**
	 * Produce a picture of a two triangles lighted by a narrow spot light
	 */
	@Test
	public void trianglesSpotSharp() {
		scene2.geometries.add(triangle1, triangle2);
		scene2.lights.add(new SpotLight(trCL, trPL, trDL,10).setNarrowBeam(10).setKl(0.001).setKq(0.00004));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesSpotSharp", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() //
				.writeToImage(); //
	}
	/**
	 * Produce a picture of a two triangles lighted by all type of Light Source
	 */
	@Test
	public void trianglesSpotwithallTypeofLightSource() {
		scene2.geometries.add(triangle1, triangle2);
		scene2.lights.add(new SpotLight(trCL, trPL, trDL,10).setKl(0.001).setKq(0.0001));
		scene2.lights.add(new PointLight(trCL, new Point(-30, -10, 100),10));
		scene2.lights.add(new DirectionalLight(trCL, trDL.scale(-2)));
		ImageWriter imageWriter = new ImageWriter("lightTrianglesSpotwithallTypeofLightSource", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() //
				.writeToImage(); //
	}
	/**
	 * Produce a picture of a two triangles lighted by a point light
	 */
	@Test
	public void planesPoint() {

		scene2.geometries.add(plane1, plane2,triangle1,new Sphere(new Point(20,50,-100),30).setEmission( new Color(red)),new Sphere(new Point(150,10,-100),2));
		scene2.lights.add(new PointLight(trCL, trPL,10).setKl(0.001).setKq(0.0002));

		ImageWriter imageWriter = new ImageWriter("lightPlanesPoint", 500, 500);
		camera2.rotate(0,0,220).setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() //
				.writeToImage(); //
	}
	/**
	 * Produce a picture of a sphere lighted by all type of Light Source
	 */
	@Test
	public void sphereSpotSharpWithFewLightSource() {
		scene1.geometries.add(sphere.setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20).setkT(new Double3(0.5))));
		scene1.geometries.add(sphere2);
		scene1.geometries.add(sphere3);
		scene1.geometries.add(new Sphere(new Point(0, 0, -50), 25d) //
				.setEmission(new Color(red).reduce(2)) );
		scene1.geometries.add(new Plane(new Point(0,-70,1),new Vector(0,3,1)).setEmission(new Color(20, 20, 20)) //
				.setMaterial(new Material().setkR(new Double3(1))));
		scene1.geometries.add(new Plane(new Point(-50,-70,1),new Vector(20,0,2)).setEmission(new Color(20, 20, 20)) //
				.setMaterial(new Material().setkR(new Double3(1))));
		scene1.lights.add(new SpotLight(spCL, spPL, new Vector(1, 1, -0.5),10).setNarrowBeam(100).setKl(0.001).setKq(0.00004));
		scene1.lights.add(new PointLight(trCL,new Point(-1, 1, -50),10));
		scene1.lights.add(new DirectionalLight(trCL, trDL.scale(0.5)));
		scene1.lights.add(new PointLight(trCL, new Point(0,1,-0.25),10));
		//scene1.lights.add(new DirectionalLight(trCL, new Vector(-200,20,-170)));

		ImageWriter imageWriter = new ImageWriter("lightSphereSpotSharpWithFewLightSource", 500, 500);
		camera3.setImageWriter(imageWriter).setViewPlaneDistance(1300) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage() //
				.writeToImage(); //
	}


	@Test
	/**
	 * Produce a picture of a Sun systemm lighted by all type of Light Source
	 */
	public void sunSystem() {
		//	private Geometry sphere = new Sphere(new Point(0, 0, -50), 50d) //
		//			.setEmission(new Color(BLUE).reduce(2)) //
		//			.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300));
		//	private Geometry sphere1 = new Sphere(new Point(70, 70, -50), 30d) //
		//			.setEmission(new Color(253, 184, 19).reduce(2)) //
		//			.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300));
		scene1.background=new Color(68, 165, 212);
		scene1.geometries.add(sphere);
		scene1.geometries.add(sphere1);
		scene1.lights.add(new SpotLight(spCL, spPL, new Vector(0.75, 5, 0.5),10).setNarrowBeam(100).setKl(0.001).setKq(0.00004));
		scene1.lights.add(new PointLight(trCL,new Point(70, 70, -50),10));
		scene1.lights.add(new DirectionalLight(trCL, trDL.scale(2)));
		ImageWriter imageWriter = new ImageWriter("sunSystem", 500, 500);
		camera1.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage() //
				.writeToImage(); //
	}
}
