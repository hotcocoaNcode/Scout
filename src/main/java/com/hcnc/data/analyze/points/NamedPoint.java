package com.hcnc.data.analyze.points;

import java.util.Vector;

public class NamedPoint implements Point {
    public Vector<Double> vec;
    public String name;

    public NamedPoint(String name, Vector<Double> vec) {
        this.name = name;
        this.vec = vec;
    }

    public String getName() {
        return name;
    }

    @Override
    public Vector<Double> getPosition() {
        return vec;
    }

    @Override
    public int getDimensionCount() {
        return vec.size();
    }
}
