package renderer;

import geometries.Geometry;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTests {
    @Test
    public void testIntersections() {
        //set camera for tests
        Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0));
        camera.setViewPlaneDistance(1);
        camera.setViewPlaneSize(3, 3);

        // ============ Sphere Tests ==============
        //TC01: sphere with r=1
        assertEquals(2,
                countIntersections(camera,new Sphere(new Point(0d,0d,-3d), 1d)),
                "Wrong number of intersections with sphere.");

        //TC02: sphere with r=2.5
        assertEquals(18,
                countIntersections(camera,new Sphere(new Point(0d,0d,-3d), 2.5d)),
                "Wrong number of intersections with sphere.");

        //TC03: sphere with r=2
        assertEquals(10,
                countIntersections(camera,new Sphere(new Point(0d,0d,-2.5d), 2d)),
                "Wrong number of intersections with sphere.");

        //TC04: sphere with r=4, plane is inside sphere
        assertEquals(9,
                countIntersections(camera,new Sphere(new Point(0d,0d,-1d), 4d)),
                "Wrong number of intersections with sphere.");

        //TC05: sphere with r=0.5. sphere is behind view plane.
        assertEquals(0,
                countIntersections(camera,new Sphere(new Point(0d,0d,1d), 0.5d)),
                "Wrong number of intersections with sphere.");

        // ============ Plane Tests ==============
        //TC11: plane parallel to view plane
        assertEquals(9,
                countIntersections(camera,new Plane(new Point(0,0,-3),camera.getvTo())),
                "Wrong number of intersections with plane.");

        //TC12: plane in front of camera, not parallel to view plane
        assertEquals(9,
                countIntersections(camera,new Plane(new Point(-5,0,-3),new Point(5,0,-3),new Point(0,5,-2))),
                "Wrong number of intersections with plane.");

        //TC13: plane in front of camera, not parallel to view plane, but parallel to ray from camera
        assertEquals(6,
                countIntersections(camera,new Plane(new Point(-5,0,-6),new Point(5,0,-6),new Point(0,5,-1))),
                "Wrong number of intersections with plane.");

        // ============ Triangle Tests ==============
        //TC21: triangle in front of camera, parallel to view plane
        assertEquals(1,
                countIntersections(camera,new Triangle(new Point(0,1,-2),new Point(1,-1,-2),new Point(-1,-1,-2))),
                "Wrong number of intersections with plane.");

        //TC22: tall triangle in front of camera, parallel to view plane
        assertEquals(2,
                countIntersections(camera,new Triangle(new Point(0,20,-2),new Point(1,-1,-2),new Point(-1,-1,-2))),
                "Wrong number of intersections with plane.");
    }

    //function receives camera and geometric shape and counts and returns total number of intersections between shapes and rays from camera.
    //view plane is 3x3
    private int countIntersections(Camera cam, Geometry obj) {
        int sum = 0;

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                //create ray through pixel
                Ray tester = cam.constructRayThroughPixel(3, 3, j, i);
                //find intersections between ray and shape
                List result = obj.findIntersections(tester);
                if (result!=null){
                    //add found intersections to sum
                    sum += result.size();
                }
            }
        }
        //return total number of intersections found
        return sum;
    }

}