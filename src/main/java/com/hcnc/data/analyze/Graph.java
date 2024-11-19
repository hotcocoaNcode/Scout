package com.hcnc.data.analyze;

import com.hcnc.data.analyze.points.Point;

import java.util.Vector;

public class Graph {
    public Vector<Point> points;
    int dimensionCount;

    public Graph(int dimensionCount) {
        this.dimensionCount = dimensionCount;
        points = new Vector<>();
    }

    public void addPoint(Point p) {
        if (p.getDimensionCount() == this.dimensionCount) {
            points.add(p);
        } else {
            throw new IllegalArgumentException("Dimension count mismatch!");
        }
    }
}
