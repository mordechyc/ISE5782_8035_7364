package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

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
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

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

    /**
     * sets the imageWriter field to the given imageWriter.
     *
     * @param imageWriter The ImageWriter object that will be used to write the image to a file.
     * @return The camera itself.
     */
    public Camera setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }

    /**
     * sets the ray tracer for this camera.
     *
     * @param rayTracerBase The ray tracer to use.
     * @return The camera itself.
     */
    public Camera setRayTracer(RayTracerBase rayTracerBase) {
        this.rayTracer = rayTracerBase;
        return this;
    }

    /**
     * The function checks if all params is existed, if one of them is missing
     * the function throw MissingResourceException.
     *
     * @throws MissingResourceException if one of param's Camera is missing
     * @return
     */
    public Camera renderImage() {
        try {
            if (imageWriter == null) {
                throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
            }
            if (p0 == null || vRight == null || vTo == null || vUp == null || width == 0.0 || height == 0.0 || distance == 0.0) {
                throw new MissingResourceException("missing resource", Camera.class.getName(), "");
            }
            if (rayTracer == null) {
                throw new MissingResourceException("missing resource", RayTracerBase.class.getName(), "");
            }
        } catch (MissingResourceException e) {
            throw new UnsupportedOperationException("missing resources in order to create the image"
                    + e.getClassName());
        }
        for (int i = 0; i < imageWriter.getNx(); i++) {
            for (int j = 0; j < imageWriter.getNy(); j++) {
                castRay(imageWriter.getNx(), imageWriter.getNy(), i, j);
            }
        }
        return this;
    }

    /**
     * Cast ray from camera in order to color a pixel
     *
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column)
     */
    private void castRay(int nX, int nY, int col, int row) {
        Color pixelColor;
        Ray ray = constructRayThroughPixel(nX, nY, col, row);
        pixelColor = rayTracer.traceRay(ray);
        imageWriter.writePixel(col, row, pixelColor);
    }


    /**
     * Produces a pixel-sized matrix of the view plane and colors rows and columns
     *
     * @param interval the space between the rows and columns
     * @param color    the color of the grid
     * @throws MissingResourceException if image writer for the render is missing
     */
    public void printGrid(int interval, Color color) throws MissingResourceException {
        if (imageWriter == null) {
            throw new MissingResourceException("ERROR - image writer not initlized", ImageWriter.class.getName(), "");
        }
        for (int i = 0; i < imageWriter.getNx(); i++) {
            for (int j = 0; j < imageWriter.getNy(); j++) {
                if (j % interval == 0 || i % interval == 0) {
                    imageWriter.writePixel(i, j, color);
                }
            }
        }
        imageWriter.writeToImage();
    }

    /**
     * If the image writer is not initialized, throw an exception
     *
     * @throws MissingResourceException If the image writer is not initialized.
     */
    public void writeToImage() {
        try {
            imageWriter.writeToImage();
        } catch (Exception e) {
            throw new MissingResourceException("ERROR - image writer not initlized", ImageWriter.class.getName(), "");
        }
    }



    //---------------------------------------------------------------------------------------------------------------------------------//

    /**
     * Rotate the camera by rotating the vectors of the camera directions <br/>
     * According the Rodrigues' rotation formula
     *
     * @param theta angle theta according to the right hand rule in degrees
     * @return this camera after the rotating
     */
    public Camera rotateCamera(double theta) {
        return rotateCamera(theta, vTo);
    }

    /**
     * Rotate the camera by rotating the vectors of the camera directions <br/>
     * According the Rodrigues' rotation formula
     *
     * @param theta angle theta according to the right hand rule in degrees
     * @param k     axis vector for the rotation
     * @return this camera after the rotating
     */
    private Camera rotateCamera(double theta, Vector k) {
        double radianAngle = Math.toRadians(theta);
        double cosTheta = alignZero(Math.cos(radianAngle));
        double sinTheta = alignZero(Math.sin(radianAngle));

        vRight.rotateVector(k, cosTheta, sinTheta);
        vUp.rotateVector(k, cosTheta, sinTheta);

        return this;
    }

    public Camera moveCamera(Point newPosition, Point newPointOfView) {
        // the new vTo of the camera
        Vector new_vTo = newPointOfView.subtract(newPosition).normalize();
        // the angle between the new vTo and the old
        double theta = new_vTo.dotProduct(vTo);
        // axis vector for the rotation
        Vector k = vTo.crossProduct(new_vTo).normalize();

        vTo = new_vTo;
        p0 = newPosition;

        return rotateCamera(theta, k);
    }


}
