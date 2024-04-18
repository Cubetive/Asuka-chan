package com.fubukigrin.commands.osu;

import java.net.*;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")

// Osu!Standard only, not supported other modes yet

public class Types {
    public static class UserData {
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

        public void set(JsonNode jsonNode) throws Exception {
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
    
    public static class Beatmap {
        public String status;

        public double sr;
        public double cs;
        public double od;
        public double ar;
        public double bpm;
        public String version;

        public int circles;
        public int sliders;
        public int spinners;

        public String title;
        public String artist;
        public String creator;

        public void set(JsonNode jsonNode) {
            status = jsonNode.get("status").asText();

            sr = jsonNode.get("difficulty_rating").asDouble();
            cs = jsonNode.get("cs").asDouble();
            od = jsonNode.get("accuracy").asDouble();
            ar = jsonNode.get("ar").asDouble();
            bpm = jsonNode.get("bpm").asDouble();
            version = jsonNode.get("version").asText();

            circles = jsonNode.get("count_circles").asInt();
            sliders = jsonNode.get("count_sliders").asInt();
            spinners = jsonNode.get("count_spinners").asInt();

            System.out.println(jsonNode.toString());
            JsonNode beatmapset = jsonNode.get("beatmapset");
            title = beatmapset.get("title").asText();
            artist = beatmapset.get("artist").asText();
            creator = beatmapset.get("creator").asText();
        }
    }

    public static class Score {
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
    
    public static class Leaderboard {
        public List<Score> scores;

        public void set(JsonNode jsonNode) {
            Score score = new Score();

            for (JsonNode node: jsonNode.get("scores")) {
                score.set(node.fields().next().getValue());
                scores.add(score);
            }
        }
    }
}
