package com.osu;

import com.fasterxml.jackson.databind.JsonNode;

public class Beatmap {
    public int id;
    public String status;

    public double sr;
    public double cs;
    public double od;
    public double ar;
    public double bpm;
    public String version;

    public int maxCombo;
    public int circles;
    public int sliders;
    public int spinners;

    public String title;
    public String artist;
    public String creator;

    public Beatmap(JsonNode jsonNode) {
        id = jsonNode.get("id").asInt();
        status = jsonNode.get("status").asText();

        sr = jsonNode.get("difficulty_rating").asDouble();
        cs = jsonNode.get("cs").asDouble();
        od = jsonNode.get("accuracy").asDouble();
        ar = jsonNode.get("ar").asDouble();
        bpm = jsonNode.get("bpm").asDouble();
        version = jsonNode.get("version").asText();

        maxCombo = jsonNode.get("max_combo").asInt();
        circles = jsonNode.get("count_circles").asInt();
        sliders = jsonNode.get("count_sliders").asInt();
        spinners = jsonNode.get("count_spinners").asInt();

        JsonNode beatmapset = jsonNode.get("beatmapset");
        title = beatmapset.get("title").asText();
        artist = beatmapset.get("artist").asText();
        creator = beatmapset.get("creator").asText();
    }
}
