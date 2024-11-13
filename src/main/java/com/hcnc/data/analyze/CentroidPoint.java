package com.hcnc.data.analyze;

import java.util.ArrayList;
import java.util.Vector;

public class CentroidPoint implements Point {
    public Vector<Double> vec;
    public ArrayList<Point> points;

    public CentroidPoint(Vector<Double> vec) {
        this.vec = vec;
    }
    public CentroidPoint(NumericalPoint p) { this.vec = p.vec; }

    @Override
    public Vector<Double> getPosition() {
        return vec;
    }

    @Override
    public int getDimensionCount() {
        return vec.size();
    }

    public void addPoint(Point point) {
        points.add(point);
    }
}
