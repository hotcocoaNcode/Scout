package com.hcnc.data.analyze;

import java.util.Vector;

public class NumericalPoint implements Point {
    public Vector<Double> vec;

    public NumericalPoint(Vector<Double> vec) {
        this.vec = vec;
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
