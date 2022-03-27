package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{
    private List<Intersectable> intersectables;
    @Override
    public List<Point> findIntsersections(Ray ray) {
        return null;
    }
    public Geometries() {
        this.intersectables = new LinkedList<Intersectable>();
    }

    public Geometries(Intersectable...geomtries) {

    }
    public  void  add(Intersectable...geomtries) {

    }
}
