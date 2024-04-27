package com.osu;

import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;

public class Score {
    public int maxCombo;
    public int totalScore;

    public List<String> mods;

    public String grade;

    public JsonNode beatmap;
    public JsonNode beatmapset;
    public JsonNode stats;

    public int count300;
    public int count100;
    public int count50;
    public int missCount;

    public double accuracy;
    public double pp;

    public String timeSet;

    public Score(JsonNode jsonNode) {
        totalScore = jsonNode.get("score").asInt();
        maxCombo = jsonNode.get("max_combo").asInt();
        accuracy = jsonNode.get("accuracy").asDouble();
        pp = jsonNode.get("pp").asDouble();
        grade = jsonNode.get("rank").asText();

        mods = new ArrayList<String>();
        for (JsonNode node: jsonNode.get("mods"))
            mods.add(node.asText());
        
        beatmap = jsonNode.get("beatmap");
        beatmapset = jsonNode.get("beatmapset");
        stats = jsonNode.get("statistics");

        count300 = stats.get("count_300").asInt();
        count100 = stats.get("count_100").asInt();
        count50 = stats.get("count_50").asInt();
        missCount = stats.get("count_miss").asInt();

        timeSet = jsonNode.get("created_at").asText();
    }

    public String getModCombo() {
        String modCombo = "";
        if (mods.size() > 0) {
            for (String mod : mods) {
                modCombo += mod;
            }
        }
        else {
            // No mod
            modCombo += "NM";
        }
        return modCombo;
    }
}
