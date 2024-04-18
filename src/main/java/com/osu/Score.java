package com.osu;

import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;

public class Score {
    public int maxCombo;
    public int totalScore;

    public List<String> mods;

    public String grade;

    public JsonNode stats;

    public double accuracy;
    public double pp;

    public String timeSet;

    public void set(JsonNode jsonNode) {
        totalScore = jsonNode.get("score").asInt();
        maxCombo = jsonNode.get("max_combo").asInt();
        accuracy = jsonNode.get("accuracy").asDouble();
        pp = jsonNode.get("pp").asDouble();
        grade = jsonNode.get("rank").asText();

        mods = new ArrayList<>();
        for (JsonNode node: jsonNode.get("mods"))
            mods.add(node.asText());
        
        stats = jsonNode.get("statistics");

        timeSet = jsonNode.get("created_at").asText();
    }
}
