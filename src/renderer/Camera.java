package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
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
    private ImageWriter imageWriter; // Image Writer
    private RayTracerBase rayTracer; // ray Tracer

    private int sampleNumber = 1; //Number of rays to send for single pixel

    public static int adaptiveSSAA = 0; //Decides whether adaptive super sampling anti aliasing will be used to render the image. If yes value will be larger than zero.

    private int MAX_DEPTH = 1; //Maximum depth for recursive function

    private int threadsCount = 0;
    private static final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
    private boolean print = false; // printing progress percentage

    /**
     * Set multi-threading <br>
     * - if the parameter is 0 - number of cores less 2 is taken
     *
     * @param threads number of threads
     * @return the Render object itself
     */
    public Camera setMultithreading(int threads) {
        if (threads < 0)
            throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");
        if (threads != 0)
            this.threadsCount = threads;
        else {
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            this.threadsCount = cores <= 2 ? 1 : cores;
        }
        return this;
    }

    /**
     * Set debug printing on
     *
     * @return the Render object itself
     */
    public Camera setDebugPrint() {
        print = true;
        return this;
    }

    /**
     * Pixel is an internal helper class whose objects are associated with a Render
     * object that they are generated in scope of. It is used for multithreading in
     * the Renderer and for follow up its progress.<br/>
     * There is a main follow up object and several secondary objects - one in each
     * thread.
     *
     * @author Dan
     */
    private class Pixel {
        private long maxRows = 0;
        private long maxCols = 0;
        private long pixels = 0;
        public volatile int row = 0;
        public volatile int col = -1;
        private long counter = 0;
        private int percents = 0;
        private long nextCounter = 0;

        /**
         * The constructor for initializing the main follow up Pixel object
         *
         * @param maxRows the amount of pixel rows
         * @param maxCols the amount of pixel columns
         */
        public Pixel(int maxRows, int maxCols) {
            this.maxRows = maxRows;
            this.maxCols = maxCols;
            this.pixels = (long) maxRows * maxCols;
            this.nextCounter = this.pixels / 100;
            if (Camera.this.print)
                System.out.printf("\r %02d%%", this.percents);
        }

        /**
         * Default constructor for secondary Pixel objects
         */
        public Pixel() {
        }

        /**
         * Internal function for thread-safe manipulating of main follow up Pixel object
         * - this function is critical section for all the threads, and main Pixel
         * object data is the shared data of this critical section.<br/>
         * The function provides next pixel number each call.
         *
         * @param target target secondary Pixel object to copy the row/column of the
         *               next pixel
         * @return the progress percentage for follow up: if it is 0 - nothing to print,
         * if it is -1 - the task is finished, any other value - the progress
         * percentage (only when it changes)
         */
        private synchronized int nextP(Pixel target) {
            ++col;
            ++this.counter;
            if (col < this.maxCols) {
                target.row = this.row;
                target.col = this.col;
                if (Camera.this.print && this.counter == this.nextCounter) {
                    ++this.percents;
                    this.nextCounter = this.pixels * (this.percents + 1) / 100;
                    return this.percents;
                }
                return 0;
            }
            ++row;
            if (row < this.maxRows) {
                col = 0;
                target.row = this.row;
                target.col = this.col;
                if (Camera.this.print && this.counter == this.nextCounter) {
                    ++this.percents;
                    this.nextCounter = this.pixels * (this.percents + 1) / 100;
                    return this.percents;
                }
                return 0;
            }
            return -1;
        }

        /**
         * Public function for getting next pixel number into secondary Pixel object.
         * The function prints also progress percentage in the console window.
         *
         * @param target target secondary Pixel object to copy the row/column of the
         *               next pixel
         * @return true if the work still in progress, -1 if it's done
         */
        public boolean nextPixel(Pixel target) {
            int percent = nextP(target);
            if (Camera.this.print && percent > 0)
                synchronized (this) {
                    notifyAll();
                }
            if (percent >= 0)
                return true;
            if (Camera.this.print)
                synchronized (this) {
                    notifyAll();
                }
            return false;
        }

        /**
         * Debug print of progress percentage - must be run from the main thread
         */
        public void print() {
            if (Camera.this.print)
                while (this.percents < 100)
                    try {
                        synchronized (this) {
                            wait();
                        }
                        System.out.printf("\r %02d%%", this.percents);
                        System.out.flush();
                    } catch (Exception e) {
                    }
        }
    }

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
    public Ray constructRayThroughPixel1(int nX, int nY, int j, int i) {
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

        //This will be used for supersampling.
        double xOffset = 0;
        double yOffset = 0;

        //if adaptive supersampling is enabled
        if (adaptiveSSAA > 0) {
            //check which corner of pixel to create ray to and set offset from pixel center accordingly
            switch (adaptiveSSAA){
                case 1: //top left corner
                    xOffset = -rX / 2 + 0.00000000001;
                    yOffset = rY / 2 - 0.00000000001;
                    break;
                case 2: //top right corner
                    xOffset = rX / 2 - 0.00000000001;
                    yOffset = rY / 2 - 0.00000000001;
                    break;
                case 3: //bottom right corner
                    xOffset = rX / 2 - 0.00000000001;
                    yOffset = -rY / 2 + 0.00000000001;
                    break;
                case 4: //bottom left corner
                    xOffset = -rX / 2 + 0.00000000001;
                    yOffset = -rY / 2 + 0.00000000001;
                    break;
            }

        }

        //Set pIJ to correct value. It starts at center of view plane and will be moved to correct location in view plane
        if (xJ != 0) {
            pIJ = pIJ.add(vRight.scale(xJ + xOffset));
        }
        if (yI != 0) {
            pIJ = pIJ.add(vUp.scale(yI + yOffset));
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
     * @return this, the camera itself
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
                castRayAdaptive(imageWriter.getNx(), imageWriter.getNy(), i, j,4);
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
        if(col==200 &&row==170)
        {
            int x=1;
        }
        System.out.println(col+","+row);
        Color pixelColor;
        Ray ray = constructRayThroughPixel(nX, nY, col, row);
        pixelColor = rayTracer.traceRay(ray);
        imageWriter.writePixel(col, row, pixelColor);
    }

    /**
     * Enables adaptive supersampling
     * @param num Sub sample depth
     * @return self
     */
    public Camera setAdaptiveSSAA(int num) {
        adaptiveSSAA = 1;
        sampleNumber = num;
        return this;
    }

    /**
     * Handles ray casting for adaptive super sampling
     *
     * @param nX    resolution on X axis (number of pixels in row)
     * @param nY    resolution on Y axis (number of pixels in column)
     * @param col   pixel's column number (pixel index in row)
     * @param row   pixel's row number (pixel index in column)
     * @param depth Level of depth in recursive function
     * @return Color of pixel, averaged out from all sub-squares
     */
    private Color castRayAdaptive(int nX, int nY, int col, int row, int depth) {

        System.out.println(col+","+row);

        //Create list of colors
        List<Color> cornerColorList = new LinkedList<>();
        //Find color of each corner of pixel.
        //Loop will run four times, one for each corner.
        for (int i = 1; i < 5; i++) {
            adaptiveSSAA = i;
            //Create ray from camera to corner of pixel
            Ray ray = constructRayThroughPixel(nX, nY, col, row);
            //Calculate color and add it to color list
            cornerColorList.add(rayTracer.traceRay(ray));
        }

        //If recursive function reached maximum allowed depth
        if (depth == MAX_DEPTH) {
            //set blank color
            Color cornerAvarage = Color.BLACK;
            //add colors of all four corners to blank color
            for (Color color : cornerColorList) {
                cornerAvarage = cornerAvarage.add(color);
            }
            //calculate average color and return result
            cornerAvarage = cornerAvarage.reduce(4);
            return cornerAvarage;
        }

        if (compare(cornerColorList)) {
            //return color of corner
            Color b= Color.BLACK;
            b=b.add(cornerColorList.get(0));
            b=b.add(cornerColorList.get(1));
            b=b.add(cornerColorList.get(2));
            b=b.add(cornerColorList.get(3));
            b= b.reduce(4);
            return b;

        } else {
            //Create blank color
            Color average = Color.BLACK;
            //Add to blank color the color of all four sub squares using self to calculate the color of sub square
            average = average.add(castRayAdaptive(nX * 2, nY * 2, col * 2, row * 2, depth + 1));//top left corner
            average = average.add(castRayAdaptive(nX * 2, nY * 2, col * 2 + 1, row * 2, depth + 1));//top right corner
            average = average.add(castRayAdaptive(nX * 2, nY * 2, col * 2 + 1, row * 2 + 1, depth + 1));//bottom right corner
            average = average.add(castRayAdaptive(nX * 2, nY * 2, col * 2, row * 2 + 1, depth + 1));//bottom left corner

            //calculate average color and return result
            average = average.reduce(4);
            return average;
        }
    }

    private boolean compare(List<Color> a) {
        int blue, green,red;
        blue=a.get(0).getColor().getBlue();
        green=a.get(0).getColor().getGreen();
        red=a.get(0).getColor().getRed();
        for (int i=1;i<4;i++) {
            if (a.get(i).getColor().getBlue()>blue+10||a.get(i).getColor().getBlue()<blue-10||
                    a.get(i).getColor().getGreen()>green+10||a.get(i).getColor().getGreen()<green-10||
                    a.get(i).getColor().getRed()>red+10||a.get(i).getColor().getRed()<red-10) {
                return false;
            }
        }
        return true;
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
/*
    /**
     * Rotate the camera by rotating the vectors of the camera directions <br/>
     * According the Rodrigues' rotation formula
     *
     * @param theta angle theta according to the right hand rule in degrees
     * @return this camera after the rotating
     */
  /*  public Camera rotateCamera(double theta) {
        return rotateCamera(theta, vTo);
    }*/
/*
    /**
     * Rotate the camera by rotating the vectors of the camera directions <br/>
     * According the Rodrigues' rotation formula
     *
     * @param theta angle theta according to the right hand rule in degrees
     * @param k     axis vector for the rotation
     * @return this camera after the rotating
     */
  /*  private Camera rotateCamera(double theta, Vector k) {
        double radianAngle = Math.toRadians(theta);
        double cosTheta = alignZero(Math.cos(radianAngle));
        double sinTheta = alignZero(Math.sin(radianAngle));

        vRight.rotateVector(k, cosTheta, sinTheta);
        vUp.rotateVector(k, cosTheta, sinTheta);

        return this;
    }*/
/*
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

*/
    /**
     * Rotates the camera (pitch, tilt and yaw)
     *  @param xDeg number of degrees by which the camera will be rotated around the x-axis relative to its current orientation
     * @param yDeg number of degrees by which the camera will be rotated around the y-axis relative to its current orientation
     * @param zDeg number of degrees by which the camera will be rotated around the z axis relative to its current orientation
     * @return this
     */
    public Camera rotate(double xDeg, double yDeg, double zDeg) {
        if (xDeg != 0) {
            //convert input to radians
            double xRad = Math.toRadians(xDeg);

            //check vTo vector is not on the x-axis. If it is no change is necessary.
            if (vTo.getZ() != 0 || vTo.getY() != 0) {
                //Cast vector onto YZ plane, turn it and return as new vector
                double xTheta = Math.atan2(vTo.getZ(), vTo.getY());
                double newXTheta = xTheta + xRad;
                vTo = new Vector(vTo.getX(), Math.cos(newXTheta), Math.sin(newXTheta));
            }
            //Repeat process to vUp vector
            if (vUp.getZ() != 0 || vUp.getY() != 0) {
                double xTheta = Math.atan2(vTo.getZ(), vTo.getY());
                double newXTheta = xTheta + xRad;
                vTo = new Vector(vTo.getX(), Math.cos(newXTheta), Math.sin(newXTheta));
            }
            //Find new vRight value (cross product of vTo and vUp)
            vRight = vTo.crossProduct(vUp).normalize();
        }

        if (yDeg != 0) {
            double yRad = Math.toRadians(yDeg);
            if (vTo.getX() != 0 || vTo.getZ() != 0) {
                double yTheta = Math.atan2(vTo.getX(), vTo.getZ());
                double newYTheta = yTheta + yRad;
                vTo = new Vector(Math.sin(newYTheta), vTo.getY(), Math.cos(newYTheta));
            }
            if (vUp.getX() != 0 || vUp.getZ() != 0) {
                double yTheta = Math.atan2(vUp.getX(), vUp.getZ());
                double newYTheta = yTheta + yRad;
                vUp = new Vector(Math.sin(newYTheta), vUp.getY(), Math.cos(newYTheta));
            }
            vRight = vTo.crossProduct(vUp).normalize();
        }

        if (zDeg != 0) {
            double zRad = Math.toRadians(zDeg);
            if (vTo.getY() != 0 || vTo.getX() != 0) {
                double zTheta = Math.atan2(vTo.getY(), vTo.getX());
                double newZTheta = zTheta + zRad;
                vTo = new Vector(Math.cos(newZTheta), Math.sin(newZTheta), vTo.getZ());
            }
            if (vUp.getY() != 0 || vUp.getX() != 0) {
                double zTheta = Math.atan2(vUp.getY(), vUp.getX());
                double newZTheta = zTheta + zRad;
                vUp = new Vector(Math.cos(newZTheta), Math.sin(newZTheta), vUp.getZ());
            }
            vRight = vTo.crossProduct(vUp).normalize();
        }
        return this;
    }
}
