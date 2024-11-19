package com.hcnc.data.analyze;

import com.hcnc.data.analyze.points.CentroidPoint;
import com.hcnc.data.analyze.points.Point;

import java.util.ArrayList;
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

    public double clusterFitness(CentroidPoint centroid) {
        double fitness = 0;

        double max = 0;
        for (Point p : centroid.points) max = Math.max(max, p.squaredDistance(centroid));

        double mean = 0;
        for (Point p : centroid.points) mean += p.squaredDistance(centroid);
        mean /= centroid.points.size();

        fitness = max*0.6 + mean*0.4;
        return fitness;
    }

    // Came up with this in Desmos. If you'd like the equation,
    // f\left(x\right)=k\frac{\left(\frac{x}{p}\right)\left(\frac{x}{p}-\frac{m}{c}\right)x}{p}\ \left\{x>0\right\}
    public double clusterCountFitness(double x) {
        double m = data.points.size();
        double p = 3.3;
        double k = 0.006;
        double c = 200.0;
        double fx = k * ( ( (x/p) * ((x/p)-(m/c)) * x) / p);
        return fx;
    }

    public double maxFitness() {

        double max = Double.MIN_VALUE;
        for (CentroidPoint centroid : kMeansPoints) max = Math.max(max, clusterFitness(centroid));

        return max;
    }

    public void splitGroups() {
        boolean flagUpdated = false;
        Vector<CentroidPoint> newPoints = new Vector<>();
        Vector<Integer> markedForDeletion = new Vector<>();
        double worst = maxFitness();
        for (int i = 0; i < kMeansPoints.size(); i++) {
            double fitness = clusterFitness(kMeansPoints.get(i));
            if (fitness > worst*(3.0/4.0) && kMeansPoints.get(i).points.size() > 4) {
                int finalI = i;
                kMeansPoints.get(i).points.sort(Comparator.comparing(p -> p.squaredDistance(kMeansPoints.get(finalI))));

                newPoints.add(new CentroidPoint(getMeanPosition(kMeansPoints.get(i).points.subList(0, kMeansPoints.get(i).points.size()/2))));
                kMeansPoints.get(i).vec = getMeanPosition(kMeansPoints.get(i).points.subList(kMeansPoints.get(i).points.size()/2, kMeansPoints.get(i).points.size()));
            } else if (fitness == 0.0d || kMeansPoints.get(i).points.size() < 4 || fitness > worst*(5.0/6.0)) { // Avoid having weirdness in smaller datasets.
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
        double fitness;
        Vector<CentroidPoint> bestPoints = new Vector<>();
        double bestFit = Double.MAX_VALUE;
        int bestIter = -1;
        for (int i = 0; i < imax; i++) {
            clusterStep();
            fitness = maxFitness()+0.5*clusterCountFitness(kMeansPoints.size());
            boolean best = false;
            if (fitness < bestFit) {
                bestPoints = kMeansPoints;
                bestFit = fitness;
                bestIter = i;
                best = true;
            }
            System.out.println((best ? "BEST YET: " : "") + "Iteration " + i + " complete ("+kMeansPoints.size()+" centroids, "+fitness+" worst point, " + 0.5*clusterCountFitness(kMeansPoints.size())+ " cluster count fitness)");
        }
        kMeansPoints = bestPoints;
        System.out.println("KMeans found best iteration at " + bestIter + " with a fit of " + bestFit + " and " + kMeansPoints.size() + " centroids.");
    }

    public void clusterUntil(double max) {
        int i = 0;
        double fitness = maxFitness();
        Vector<CentroidPoint> bestPoints = new Vector<>();
        double bestFit = Double.MAX_VALUE;
        while (fitness > max) {
            clusterStep();
            fitness = maxFitness()+0.5*clusterCountFitness(kMeansPoints.size());
            boolean best = false;
            if (fitness < bestFit) {
                bestPoints = kMeansPoints;
                bestFit = fitness;
                best = true;
            }
            System.out.println((best ? "BEST YET: " : "") + "Iteration " + i++ + " complete ("+kMeansPoints.size()+" centroids, "+fitness+" worst point, " + 0.5*clusterCountFitness(kMeansPoints.size())+ " cluster count fitness)");
        }
    }
}
