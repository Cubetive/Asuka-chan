package com.osu;

import com.fasterxml.jackson.databind.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import org.apache.commons.lang3.math.*;
import org.python.indexer.Scope;

@SuppressWarnings("unused")

public class OsuAPI {
    private static String BaseUrl = "https://osu.ppy.sh/api/v2/";
    private static String beatmap = "beatmaps/";
    private static String users = "users/";

    ObjectMapper objectMapper;

    Endpoints api;

    public OsuAPI() throws Exception {
        api = new Endpoints();
        objectMapper = new ObjectMapper();
    }

    public UserData getUser(String user) throws Exception {
        String key;
        if (NumberUtils.isNumber(user)) { key = "id"; }
        else { key = "username"; }

        String temp = BaseUrl + users + user + "/osu?key=" + key;
        URI uri = new URI(temp.replace(" ", "%20"));

        UserData userData = new UserData(sendGetRequest(uri));

        return userData;
    }

    public Beatmap getBeatmap(int id) throws Exception {
        URI uri = new URI(BaseUrl + beatmap + String.format("%s", id));

        Beatmap beatmap = new Beatmap(sendGetRequest(uri));

        return beatmap;
    }

    public Leaderboard getLeaderboard(int id) throws Exception {
        URI uri = new URI(BaseUrl + beatmap + String.format("%s/scores?legacy_only=1", id));

        Leaderboard leaderboard = new Leaderboard(sendGetRequest(uri));

        return leaderboard;
    }

    public Leaderboard getLeaderboard(int id, String type) throws Exception {
        // type should only be country, others won't work
        URI uri = new URI(BaseUrl + beatmap + String.format("%s/scores?legacy_only=1&type=%s", id, type));

        Leaderboard leaderboard = new Leaderboard(sendGetRequest(uri));

        return leaderboard;
    }

    public List<Score> getUserScores(int id, int uid) throws Exception {
        URI uri = new URI(BaseUrl + beatmap + String.format("%s/scores/users/%s/all?legacy_only=1", id, uid));

        List<Score> scores = new ArrayList<>();

        JsonNode jsonNode = sendGetRequest(uri);
        for (JsonNode node: jsonNode) {
            scores.add(new Score(node));
        }

        return scores;
    }

    public List<Score> getTopScores(int uid) throws Exception {
        URI uri = new URI(BaseUrl + users + String.format("%s/scores/best?legacy_only=1", uid));

        List<Score> scores = new ArrayList<>();

        JsonNode jsonNode = sendGetRequest(uri);
        for (JsonNode node: jsonNode) {
            scores.add(new Score(node));
        }

        return scores;
    }

    public List<Score> getRecentScores(int uid) throws Exception {
        URI uri = new URI(BaseUrl + users + String.format("%s/scores/recent?legacy_only=1&include_fails=1&limit=10"));

        List<Score> scores = new ArrayList<>();

        JsonNode jsonNode = sendGetRequest(uri);
        for (JsonNode node: jsonNode.get("scores")) {
            scores.add(new Score(node));
        }

        return scores;
    }

    public JsonNode sendGetRequest(URI uri) throws Exception {
        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(uri)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", String.format("Bearer %s", api.accessToken))
            .GET()
            .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(postRequest, BodyHandlers.ofString());

        JsonNode jsonNode = objectMapper.readTree(response.body());
        return jsonNode;
    }
}
