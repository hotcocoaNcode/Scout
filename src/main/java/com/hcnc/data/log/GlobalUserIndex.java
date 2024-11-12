package com.hcnc.data.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

public class GlobalUserIndex {
    static ArrayList<DiscordUser> users = new ArrayList<>();

    static int indexUser(String name) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).user.equals(name)) {
                return i;
            }
        }
        users.add(new DiscordUser(name));
        return users.size() - 1;
    }

    public static void init() {
        if (Objects.isNull(new File(".").listFiles())) return;
        ArrayList<String> userfiles = new ArrayList<>(Stream.of(Objects.requireNonNull(new File(".").listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .toList());
        for (String s : userfiles) {
            try {
                File userfile = new File(s);
                Scanner fileIn = new Scanner(userfile);
                fileIn.nextLine();
                String lineWeCareAbout = fileIn.nextLine();
                lineWeCareAbout = lineWeCareAbout.substring(1, lineWeCareAbout.length() - 1);
                ArrayList<String> splitEntries = new ArrayList<>(Arrays.stream(lineWeCareAbout.split(", ")).toList());
                HashMap<String, WordVec> userDict = new HashMap<>();
                for (String entry : splitEntries) {
                    String[] words = entry.split("=");
                    userDict.put(words[0], new WordVec(words[1]));
                }
                fileIn.close();
                users.add(new DiscordUser(s.substring(0, s.length()-4), userDict));
                System.out.println("Got " + users.get(users.size()-1) + " from disk");
            } catch (FileNotFoundException e) {
                System.out.println("FileNotFound parsing " + s);
            }
        }
        System.out.println("Got " + users.size() + " total");
    }

    public static void parseMessage(String user, String message) {
        users.get(indexUser(user)).parseMessage(message);
    }
}
