package com.hcnc.data.log;

import java.util.HashMap;
import java.util.List;

public class WordVec extends HashMap<String, Integer> {

    public WordVec() {
        super();
    }

    public WordVec(String wordVecString) {
        super();
        for (String word : wordVecString.split("\5")) {
            String[] occurs = word.split("\7");
            if (occurs.length == 2) this.put(occurs[0], Integer.parseInt(occurs[1]));
        }
    }

    HashMap<String, Double> normalizedVector() {
        HashMap<String, Double> normalized = new HashMap<>();
        double total = 0;
        for (String key : this.keySet()) {
            normalized.put(key, 0.0d);
            total += this.get(key).doubleValue();
        }
        for (String key : normalized.keySet()) {
            normalized.replace(key, normalized.get(key)/total);
        }
        return normalized;
    }

    void wordOccurrence(String word) {
        if (this.containsKey(word)) {
            this.replace(word, this.get(word) + 1);
        } else {
            this.put(word, 1);
        }
    }

    void updateDictionary(List<String> words){
        for (String word : words) {
            if (!this.containsKey(word)) {
                this.put(word, 0);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String key : this.keySet()) {
            s.append(key).append("\7").append(this.get(key).toString()).append("\5");
        }
        return s.toString();
    }
}
