package com.hcnc.data.log;

import com.hcnc.data.log.conversational.ConversationTracker;
import com.hcnc.data.log.text.WordVec;

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
    public WordVec otherUserCounts;
    public long lastMessageUnixMS;

    public DiscordUser(String user) {
        this.user = user;
        this.words = new HashMap<>();
        this.userDict = new ArrayList<>();
        this.otherUserCounts = new WordVec();
    }

    public DiscordUser(String user, HashMap<String, WordVec> words) {
        this.user = user;
        this.words = words;
        this.userDict = new ArrayList<>(words.keySet().stream().toList());
        this.otherUserCounts = new WordVec();
    }

    private void parseSentence(String sentence) {
        ArrayList<String> sentenceParsed = new ArrayList<>(Arrays.stream(sentence.split(" ")).toList());
        sentenceParsed.replaceAll(String::toLowerCase);
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
            File userFile = new File("db_" + user + ".txt");
            if (userFile.createNewFile()) {
                System.out.println("File for user " + user + " created");
            }
            FileWriter writer = new FileWriter("db_"+user+".txt");
            writer.write(otherUserCounts.toString() + "\n" + words.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing db_" + user + ".txt");
        }
    }

    public void parseMessage(String message) {
        if (message.contains("http") || message.contains("`")) return; // Shitty work around but you know what they say in france
        /*
        message = message.replaceAll("[.,~!?()*&^%$#@\"'=+]", "");
        message = message.replaceAll("[/\\\\-]", " ");
         */
        message = message // (somewhat less) DUCT TAPE IN PROD
                .replaceAll(" _|_ |[-/\\\\]", " ")
                .replaceAll("[*()'#{}:;=+|<>]|_$", "");
        for (String ideaBlock : message.split("[.,!?\"…]")) {
            parseSentence(ideaBlock);
        }
        for (WordVec wordVec : words.values()) {
            wordVec.updateDictionary(userDict);
        }
    }

    public void relationEvent() {
        // Under a 10 minute message gap
        if (ConversationTracker.messageMS - lastMessageUnixMS < 1000L*600L) {
            otherUserCounts.wordOccurrence(ConversationTracker.lastTalkedUser);
        }
        ConversationTracker.messageMS = lastMessageUnixMS;
        ConversationTracker.lastTalkedUser = user;
    }

    public void messageEvent(String message) {
        lastMessageUnixMS = System.currentTimeMillis();
        parseMessage(message);
        relationEvent();
        saveToFile();
    }

    @Override
    public String toString() {
        return "DiscordUser @" + user;
    }
}
