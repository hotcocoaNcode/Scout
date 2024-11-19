package com.hcnc.data;

import com.hcnc.data.analyze.Graph;
import com.hcnc.data.analyze.KMeans;
import com.hcnc.data.analyze.points.CentroidPoint;
import com.hcnc.data.analyze.points.NamedPoint;
import com.hcnc.data.analyze.points.Point;
import com.hcnc.data.log.DiscordUser;
import com.hcnc.data.log.GlobalUserIndex;

import java.util.Optional;

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
            // TODO: Better cluster than just iterative. Instead stop when KMeans reaches some grouping metric (also silhouette based maybe)
            means.cluster(10);
            int i = 0;
            for (CentroidPoint centroid : means.kMeansPoints) {
                double avgdist = 0;
                StringBuilder toPrint = new StringBuilder();
                for (Point p : centroid.points) {
                    double distance = p.squaredDistance(centroid);
                    toPrint.append(((NamedPoint) p).name).append(": ").append(distance).append('\n');
                    avgdist += distance;
                }
                System.out.println("\nCLUSTER " + ++i + ": " + avgdist/centroid.points.size());
                System.out.println(toPrint);
            }
        } else {
            System.out.println("No user \"" + name + "\" found.");
        }
    }
}
