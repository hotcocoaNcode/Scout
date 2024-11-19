package com.hcnc.data;

import java.util.Vector;

public class VecUtil {
    public static Vector<Double> vec(double... args) {
        Vector<Double> vec = new Vector<>();
        for (double arg : args) {
            vec.add(arg);
        }
        return vec;
    }

    public static Vector<Double> normalize(Vector<Double> vec) {
        double sum = 0;
        for (double n : vec) {
            sum += n;
        }
        for (int i = 0; i < vec.size(); i++) {
            vec.set(i, vec.get(i)/sum);
        }
        return vec;
    }

    public static Vector<Double> nvec(double... args) {
        return new Vector<>(normalize(vec(args)));
    }
}
