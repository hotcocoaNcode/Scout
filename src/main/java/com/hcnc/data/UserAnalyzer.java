package com.hcnc.data;

import com.hcnc.data.analyze.Graph;
import com.hcnc.data.analyze.KMeans;
import com.hcnc.data.analyze.points.CentroidPoint;
import com.hcnc.data.analyze.points.NamedPoint;
import com.hcnc.data.analyze.points.Point;
import com.hcnc.data.log.DiscordUser;
import com.hcnc.data.log.GlobalUserIndex;

import java.util.Optional;
import java.util.Scanner;

public class UserAnalyzer {
    public static void analyzeUser(String name) {
        Optional<DiscordUser> optionalUser = GlobalUserIndex.users.stream().filter(u -> u.user.equals(name)).findFirst();
        if (optionalUser.isPresent()) {
            DiscordUser user = optionalUser.get();
            Graph dataPoints = new Graph(user.userDict.size());
            for (String key : user.words.keySet()) {
                dataPoints.addPoint(new NamedPoint(key, VecUtil.normalize(user.words.get(key).getMeaningVector(user.userDict))));
            }
            KMeans means = new KMeans(dataPoints, 1);
            System.out.println("How many iterations should the model try? > ");
            means.cluster((new Scanner(System.in)).nextInt());
            int i = 0;
            for (CentroidPoint centroid : means.kMeansPoints) {
                StringBuilder toPrint = new StringBuilder();
                for (Point p : centroid.points) {
                    toPrint.append(((NamedPoint) p).name).append(": ").append(p.squaredDistance(centroid)).append('\n');
                }
                System.out.println("\nCLUSTER " + ++i + ": " + means.clusterFitness(centroid));
                System.out.println(toPrint);
            }
        } else {
            System.out.println("No user \"" + name + "\" found.");
        }
    }
}
