package com.osu;

import java.net.*;

import com.fasterxml.jackson.databind.JsonNode;

public class UserData {
    public String username;
    public int id;
    public URL avatarUrl;
    public String countryCode;

    public int globalRank;
    public int countryRank;
    public int peakRank;
    public String peakRankUpdate;

    public double levelCurrent;
    public double levelProgress;

    public double pp;
    public double accuracy;

    public int playCount;
    public int playTime;

    public String lastSeen;

    public JsonNode gradeCounts;

    public UserData(JsonNode jsonNode) throws Exception {
        username = jsonNode.get("username").asText();
        id = jsonNode.get("id").asInt();
        avatarUrl = new URL(jsonNode.get("avatar_url").asText());
        countryCode = jsonNode.get("country_code").asText();

        peakRank = jsonNode.get("rank_highest").get("rank").asInt();
        peakRankUpdate = jsonNode.get("rank_highest").get("updated_at").asText();

        lastSeen = jsonNode.get("last_visit").asText();

        JsonNode stats = jsonNode.get("statistics");

        globalRank = stats.get("global_rank").asInt();
        countryRank = stats.get("country_rank").asInt();

        levelCurrent = stats.get("level").get("current").asDouble();
        levelProgress = stats.get("level").get("progress").asDouble();

        pp = stats.get("pp").asDouble();
        accuracy = stats.get("hit_accuracy").asDouble();

        playCount = stats.get("play_count").asInt();
        playTime = stats.get("play_time").asInt();

        gradeCounts = stats.get("grade_counts");
    }
}