package renderer;

import primitives.Point;
import primitives.Vector;

import static primitives.Util.isZero;

public class Camera {

    private Point p0;    // Location of camera
    private Vector vUp;    // Vector facing up out of the camera
    private Vector vTo;    // Vector facing forward out of the camera
    private Vector vRight; // Vector facing right out of the camera
    private double width;  // Width of viewing plane
    private double height; // Height of viewing plane
    private double distance;   // Distance of viewing plane from camera

    /**
     * Constructor that receives location, forward vector and up vector
     *
     * @param p0  Location of camera
     * @param vTo Vector pointing directly out of the camera
     * @param vUp Vector point up from lens
     * @throws IllegalArgumentException When to and up vectors aren't orthogonal.
     */
    public Camera(Point p0, Vector vTo, Vector vUp) {
        this.p0 = p0;
        //Check both vector are orthogonal. If not throw exception.
        if (vTo.dotProduct(vUp) != 0) {
            throw new IllegalArgumentException("Vectors vTo and vUp are not orthogonal.");
        }
        //Normalize both vectors
        this.vTo = vTo.normalize();
        this.vUp = vUp.normalize();
        //set right vector as cross product between forward and up vectors
        this.vRight = vTo.crossProduct(vUp).normalize();
    }

    /**
     * Set size of view plane
     *
     * @param width  plane width
     * @param height plane height
     * @return Camera
     */
    public Camera setViewPlaneSize(double width, double height) {
        if (width <= 0) {
            throw new IllegalArgumentException("Illegal value of width");
        }
        this.width = width;

        if (height <= 0) {
            throw new IllegalArgumentException("Illegal value of height");
        }
        this.height = height;
        return this;
    }

    /**
     * Set distance of view plane from camera
     *
     * @param distance Distance
     * @return Camera
     */
    public Camera setViewPlaneDistance(double distance) {
        //ensure camera and view plane are not in same location
        if (isZero(distance)) {
            throw new IllegalArgumentException("Distance cannot be zero. Did you think I wouldn't notice?");
        }
        this.distance = distance;
        return this;
    }


    /**
     * Getter for p0 (location of camera)
     *
     * @return location of camera
     */
    public Point getP0() {
        return p0;
    }

    /**
     * Setter for p0 (location of camera)
     *
     * @param p0 new camera location
     */
    public void setP0(Point p0) {
        this.p0 = p0;
    }

    /**
     * Getter for up vector
     *
     * @return up vector
     */
    public Vector getvUp() {
        return vUp;
    }

    /**
     * Getter for forward vector
     *
     * @return forward vector
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * Getter for right vector
     *
     * @return right vector
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     * Getter for width of view plane
     *
     * @return view plane width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Getter for height of view plane
     *
     * @return view plane height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Getter distance of view plane from camera
     *
     * @return distance
     */
    public double getDistance() {
        return distance;
    }

}
