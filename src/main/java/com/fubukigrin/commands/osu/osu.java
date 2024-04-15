package com.fubukigrin.commands.osu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import org.apache.commons.lang3.math.*;

@SuppressWarnings("unused")

public class osu {
    private static String BaseUrl = "https://osu.ppy.sh/api/v2/";
    private static String beatmap = "beatmaps/";
    private static String users = "users/";

    ObjectMapper objectMapper;

    Endpoints api;

    public osu() throws Exception {
        api = new Endpoints();
        objectMapper = new ObjectMapper();
    }

    public JsonNode getUserData(String user) throws Exception {
        String key;
        if (NumberUtils.isNumber(user)) { key = "id"; }
        else { key = "username"; }

        String temp = BaseUrl + users + user + "/osu?key=" + key;
        URI uri = new URI(temp.replace(" ", "%20"));

        return sendGetRequest(uri);
    }

    public JsonNode getBeatmap(int id) throws Exception {
        URI uri = new URI(BaseUrl + beatmap + String.format("lookup?id=%s", id));

        return sendGetRequest(uri);
    }

    public JsonNode getLeaderboard(int id) throws Exception {
        URI uri = new URI(BaseUrl + beatmap + String.format("%s/scores?legacy_only=1", id));

        return sendGetRequest(uri);
    }

    public JsonNode getLeaderboard(int id, String type) throws Exception {
        // type should only be country, others won't work
        URI uri = new URI(BaseUrl + beatmap + String.format("%s/scores?legacy_only=1&type=%s", id, type));

        return sendGetRequest(uri);
    }

    public JsonNode getUserScores(int id, int uid) throws Exception {
        URI uri = new URI(BaseUrl + beatmap + String.format("%s/scores/users/%s/all?legacy_only=1", id, uid));

        return sendGetRequest(uri);
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