package com.hcnc.data.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DiscordUser {
    public String user;
    public HashMap<String, WordVec> words;
    public ArrayList<String> userDict;

    public DiscordUser(String user) {
        this.user = user;
        this.words = new HashMap<>();
        this.userDict = new ArrayList<>();
    }

    public DiscordUser(String user, HashMap<String, WordVec> words) {
        this.user = user;
        this.words = words;
        this.userDict = new ArrayList<>(words.keySet().stream().toList());
    }

    private void parseSentence(String sentence) {
        ArrayList<String> sentenceParsed = new ArrayList<>(Arrays.stream(sentence.split(" ")).toList());
        sentenceParsed.replaceAll(s -> s.toLowerCase().replaceAll("\\p{Punct}", ""));
        sentenceParsed.removeAll(sentenceParsed.stream().filter(String::isEmpty).toList());
        for (String word : sentenceParsed) {
            if (!words.containsKey(word)) {
                words.put(word, new WordVec());
            }
            //TODO: ouch, o(n^2)
            for (String otherWord : sentenceParsed) {
                userDict.add(otherWord);
                words.get(word).wordOccurrence(otherWord);
            }
        }
    }

    public void saveToFile() {
        try {
            File userFile = new File(user + ".txt");
            if (userFile.createNewFile()) {
                System.out.println("File for user " + user + " created");
            }
            FileWriter writer = new FileWriter(user+".txt");
            writer.write(user + "\n" + words.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing " + user + ".txt");
            e.printStackTrace();
        }
    }

    public void parseMessage(String message) {
        if (message.contains("http")) return; // Shitty work around but you know what they say in france
        for (String ideaBlock : message.split("\\p{Punct}")) {
            parseSentence(ideaBlock);
        }
        for (WordVec wordVec : words.values()) {
            wordVec.updateDictionary(userDict);
        }
        saveToFile();
    }

    @Override
    public String toString() {
        return "DiscordUser @" + user;
    }
}