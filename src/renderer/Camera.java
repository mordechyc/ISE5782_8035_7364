package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Camera {

    private Point p0;          // Location of camera
    private Vector vUp;        // Vector facing up out of the camera
    private Vector vTo;        // Vector facing forward out of the camera
    private Vector vRight;     // Vector facing right out of the camera
    private double width;      // Width of viewing plane
    private double height;     // Height of viewing plane
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
     * Creates a ray the travels from camera through  pixel on the grid
     *
     * @param nX Number of rows in view plane
     * @param nY Number of columns in view plane
     * @param j  Index in view plane
     * @param i  Index in view plane
     * @return Ray that goes from camera to point (j,i) in view plane
     */
    public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {
        //Find center of image
        Point pc = p0.add(vTo.scale(distance));
        Point pIJ = pc;

        //Find number of pixels for size of view plane (divide height and width by number of actual pixels)
        //Ry = h/Ny
        //Rx = w/Nx
        double rY = alignZero(height / nY);
        double rX = alignZero(width / nX);
        //Find center of pixel
        //Yi = -(i - (Ny - 1)/2) * Ry
        //Xj = (J - (Nx -1)/2) * Rx
        double xJ = alignZero((j - ((nX - 1) / 2d)) * rX);
        double yI = alignZero(-(i - ((nY - 1) / 2d)) * rY);

        //Set pIJ to correct value. It starts at center of view plane and will be moved to correct location in view plane
        if (xJ != 0) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(vUp.scale(yI));
        }
        //Vi, j = Pi, j - P0
        Vector vIJ = pIJ.subtract(p0);

        //Return ray from camera (p0) to found point in view plane (vIJ)
        return new Ray(p0, vIJ);
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
     * Getter for p0 (location of camera)
     *
     * @return location of camera
     */
    public Point getP0() {
        return p0;
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
