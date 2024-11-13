package com.hcnc.data.analyze;

import java.util.ArrayList;
import java.util.Vector;

public class KMeans {
    Graph data;
    Graph kMeansPoints;

    public KMeans(Graph data, int pointCount) {
        this.data = data;
        this.kMeansPoints = new Graph(data.dimensionCount);
        for (int i = 0; i < pointCount; i++) {
            Vector<Double> pointLocation = new Vector<>();
            for (int j = 0; j < data.dimensionCount; j++) {
                pointLocation.add(Math.random());
            }
            kMeansPoints.addPoint(new CentroidPoint(pointLocation));
        }
    }

    public void clusterStep() {
        // Assign each point to voronoi cluster
        for (int i = 0; i < data.points.size(); i++) {
            int closestPoint = -1;
            double distance = Double.MAX_VALUE;
            for (int j = 0; j < kMeansPoints.points.size(); j++) {
                if (data.points.get(i).distance(kMeansPoints.points.get(j)) < distance) {
                    closestPoint = j;
                    distance = data.points.get(i).distance(kMeansPoints.points.get(j));
                }
            }
            ((CentroidPoint)(kMeansPoints.points.get(closestPoint))).addPoint(data.points.get(i));
        }

        Graph newKMeans = new Graph(data.dimensionCount);

        for (int i = 0; i < kMeansPoints.points.size(); i++) {
            CentroidPoint point = (CentroidPoint) kMeansPoints.points.get(i);
            Vector<Double> pointLocation = new Vector<>();
            while (pointLocation.size() < data.dimensionCount) {
                pointLocation.add(0.0);
            }
            for (int j = 0; j < point.points.size(); j++) {
                for (int k = 0; k < data.dimensionCount; k++) {
                    pointLocation.set(k, pointLocation.get(k) + point.points.get(j).getPosition().get(k));
                }
            }
            // Get mean
            pointLocation.replaceAll(aDouble -> aDouble / (double) point.points.size());

            newKMeans.addPoint(new CentroidPoint(pointLocation));
        }

        kMeansPoints = newKMeans;
    }

    public void cluster(int imax) {
        for (int i = 0; i < imax; i++) {
            clusterStep();
        }
    }
}
