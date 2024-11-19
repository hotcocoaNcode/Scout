package com.hcnc.data.analyze;

import com.hcnc.data.analyze.points.CentroidPoint;
import com.hcnc.data.analyze.points.Point;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class KMeans {
    Graph data;
    public Vector<CentroidPoint> kMeansPoints;

    public KMeans(Graph data, int pointCount) {
        this.data = data;
        this.kMeansPoints = new Vector<>();
        for (int i = 0; i < pointCount; i++) {
            Vector<Double> pointLocation = new Vector<>();
            for (int j = 0; j < data.dimensionCount; j++) {
                pointLocation.add(Math.random());
            }
            kMeansPoints.add(new CentroidPoint(pointLocation));
        }
        assignPoints();
        cleanPoints();
    }

    public Vector<Double> getMeanPosition(List<Point> points) {
        Vector<Double> pointLocation = new Vector<>();
        int dim = 0;
        while (pointLocation.size() < data.dimensionCount) {
            double sum = 0.0;
            for (Point p : points) {
                sum += p.getPosition().get(dim);
            }
            pointLocation.add(sum/points.size());
            dim++;
        }
        return pointLocation;
    }

    public void splitGroups() {
        boolean flagUpdated = false;
        Vector<CentroidPoint> newPoints = new Vector<>();
        Vector<Integer> markedForDeletion = new Vector<>();
        for (int i = 0; i < kMeansPoints.size(); i++) {
            double meanSquaredDistance = 0;
            for (Point p : kMeansPoints.get(i).points) meanSquaredDistance += p.squaredDistance(kMeansPoints.get(i));
            meanSquaredDistance /= kMeansPoints.get(i).points.size();
            if (meanSquaredDistance > 0.2d && kMeansPoints.get(i).points.size() > 10) { // TODO: Math-y way to come up with this. 0.1 just seemed high when looking at 1k+ word datasets
                int finalI = i;
                kMeansPoints.get(i).points.sort(Comparator.comparing(p -> p.squaredDistance(kMeansPoints.get(finalI))));

                newPoints.add(new CentroidPoint(getMeanPosition(kMeansPoints.get(i).points.subList(0, kMeansPoints.get(i).points.size()/2))));
                kMeansPoints.get(i).vec = getMeanPosition(kMeansPoints.get(i).points.subList(kMeansPoints.get(i).points.size()/2, kMeansPoints.get(i).points.size()));
            } else if (meanSquaredDistance == 0.0d || kMeansPoints.get(i).points.size() == 1) {
                flagUpdated = true;
                markedForDeletion.add(i);
            }
        }

        for (int n : markedForDeletion.stream().sorted(Comparator.comparingInt(value -> -value)).toList()) {
            kMeansPoints.remove(n);
        }
        kMeansPoints.addAll(newPoints);
        if (!newPoints.isEmpty() || flagUpdated) {
            assignPoints();
            cleanPoints();
        }
    }

    // Avoid NaNs
    public void cleanPoints() {
        kMeansPoints = new Vector<>(kMeansPoints.stream().filter(centroidPoint -> !centroidPoint.points.isEmpty()).toList());
    }

    // Assign each point to voronoi cluster
    public void assignPoints() {
        for (CentroidPoint point : kMeansPoints) point.points.clear();
        for (Point p : data.points) {
            int closestPoint = -1;
            double distance = Double.MAX_VALUE;
            for (int i = 0; i < kMeansPoints.size(); i++) {
                if (p.squaredDistance(kMeansPoints.get(i)) < distance) {
                    closestPoint = i;
                    distance = p.squaredDistance(kMeansPoints.get(i));
                }
            }
            kMeansPoints.get(closestPoint).addPoint(p);
        }
    }


    public void convergeKMeans() {
        Vector<CentroidPoint> newKMeans = new Vector<>();

        for (CentroidPoint kMeansPoint : kMeansPoints) {
            newKMeans.add(new CentroidPoint(getMeanPosition(kMeansPoint.points)));
        }

        kMeansPoints = newKMeans;
    }

    public void clusterStep() {
        convergeKMeans();
        assignPoints();
        cleanPoints();
        splitGroups();
    }

    public void cluster(int imax) {
        for (int i = 0; i < imax; i++) {
            clusterStep();
            System.out.println("Iteration " + i + " complete ("+kMeansPoints.size()+" centroids)");
        }
    }
}
