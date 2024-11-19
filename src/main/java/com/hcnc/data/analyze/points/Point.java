package com.hcnc.data.analyze.points;

import java.util.Vector;

public interface Point {

    Vector<Double> getPosition();
    int getDimensionCount();

    default double squaredDistance(Point p) {
        double sum = 0.0;
        for (int i = 0; i < Math.min(this.getDimensionCount(), p.getDimensionCount()); i++) {
            double difference = this.getPosition().get(i) - p.getPosition().get(i);
            sum += difference*difference;
        }
        return sum;
    }

    default double distance(Point p) {
        return Math.sqrt(squaredDistance(p));
    }

    default String printablePoint() {
        return this.getClass().getSimpleName() + "(" + this.getPosition() + ")";
    }
}
