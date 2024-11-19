package com.hcnc;

import com.hcnc.data.VecUtil;
import com.hcnc.data.analyze.Graph;
import com.hcnc.data.analyze.KMeans;
import com.hcnc.data.analyze.points.CentroidPoint;
import com.hcnc.data.analyze.points.NumericalPoint;
import com.hcnc.data.analyze.points.Point;


public class TestKMean {
    public static void main(String[] args) {
        Graph g = new Graph(2);
        for (int i = 0; i < 1000; i++) {
            g.addPoint(new NumericalPoint(VecUtil.nvec(Math.random(), Math.random())));
        }

        KMeans k = new KMeans(g, 1);
        for (int i = 0; i < 10; i++) {
            System.out.println("iter " + i);

            for (CentroidPoint centroid : k.kMeansPoints) {
                System.out.println(centroid.printablePoint());
                double adist = 0;
                int j = 0;
                for (Point p : centroid.points) {
                    adist += p.squaredDistance(centroid);
                    j++;
                }
                adist /= j;
                System.out.println(adist);
            }
            System.out.println();

            k.clusterStep();
        }
    }
}
