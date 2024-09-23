package com.osu;

import com.fasterxml.jackson.databind.*;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.*;
import java.util.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import org.apache.commons.lang3.math.*;

class APIClient {
    private String Oauth = "https://osu.ppy.sh/oauth/";
    private String Token = "token";

    private String clientID;
    private String clientSecret;

    public String accessToken;
    public long expiresIn;
    public String tokenType;

    HttpClient httpClient;

    public APIClient() throws Exception {
        Dotenv config = Dotenv.configure().load();

        clientID = config.get("CLIENT_ID");
        clientSecret = config.get("CLIENT_SECRET");

        httpClient = HttpClient.newHttpClient();
    }

    public String getToken() throws Exception {
        if (expiresIn > System.currentTimeMillis() && accessToken != null) {
            return accessToken;
        }

        String params = String.format(
                "client_id=%s&client_secret=%s&grant_type=client_credentials&scope=public",
                clientID, clientSecret);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Oauth + Token))
                .POST(BodyPublishers.ofString(params))
                .setHeader("Accept", "application/json")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

        JsonNode node = new ObjectMapper().readTree(response.body());
        accessToken = node.get("access_token").asText();
        expiresIn = System.currentTimeMillis() + node.get("expires_in").asLong();
        tokenType = node.get("token_type").asText();

        return accessToken;
    }

    private HttpRequest.Builder requestBuilder(URI uri) throws Exception {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", String.format("Bearer %s", getToken()));
    }

    public JsonNode sendGetRequest(URI uri) throws Exception {
        HttpRequest getRequest = requestBuilder(uri).GET().build();
        return new ObjectMapper().readTree(httpClient.send(getRequest, BodyHandlers.ofString()).body());
    }

    public JsonNode sendPostRequest(URI uri, String body) throws Exception {
        HttpRequest postRequest = requestBuilder(uri).POST(BodyPublishers.ofString(body)).build();
        return new ObjectMapper().readTree(httpClient.send(postRequest, BodyHandlers.ofString()).body());
    }

}

public class OsuAPI {

    private APIClient api;

    public OsuAPI() throws Exception {
        api = new APIClient();
        api.getToken();
    }

    public UserData getUser(String user) throws Exception {
        String key;
        if (NumberUtils.isNumber(user)) {
            key = "id";
        } else {
            key = "username";
        }

        String temp = Constants.USER_URL + user + "/osu?key=" + key;
        URI uri = new URI(temp.replace(" ", "%20"));

        UserData userData = new UserData(api.sendGetRequest(uri));

        return userData;
    }

    public Beatmap getBeatmap(int id) throws Exception {
        URI uri = new URI(Constants.BEATMAP_URL + id);

        Beatmap beatmap = new Beatmap(api.sendGetRequest(uri));

        return beatmap;
    }

    public Leaderboard getLeaderboard(int id) throws Exception {
        URI uri = new URI(Constants.BEATMAP_URL + String.format("%s/scores?legacy_only=1", id));

        Leaderboard leaderboard = new Leaderboard(api.sendGetRequest(uri));

        return leaderboard;
    }

    public Leaderboard getLeaderboard(int id, String type) throws Exception {
        // type should only be country, others won't work
        URI uri = new URI(Constants.BEATMAP_URL + String.format("%s/scores?legacy_only=1&type=%s", id, type));

        Leaderboard leaderboard = new Leaderboard(api.sendGetRequest(uri));

        return leaderboard;
    }

    public List<Score> getUserScores(int id, int uid) throws Exception {
        URI uri = new URI(Constants.BEATMAP_URL + String.format("%s/scores/users/%s/all?legacy_only=1", id, uid));

        List<Score> scores = new ArrayList<>();

        JsonNode jsonNode = api.sendGetRequest(uri);
        for (JsonNode node : jsonNode) {
            scores.add(new Score(node));
        }

        return scores;
    }

    public List<Score> getTopScores(int uid) throws Exception {
        URI uri = new URI(Constants.USER_URL + String.format("%s/scores/best?legacy_only=1&limit=100", uid));

        List<Score> scores = new ArrayList<>();

        JsonNode jsonNode = api.sendGetRequest(uri);
        for (JsonNode node : jsonNode) {
            scores.add(new Score(node));
        }

        return scores;
    }

    public List<Score> getRecentScores(int uid) throws Exception {
        URI uri = new URI(
                Constants.USER_URL + String.format("%s/scores/recent?legacy_only=1&include_fails=1&limit=10"));

        List<Score> scores = new ArrayList<>();

        JsonNode jsonNode = api.sendGetRequest(uri);
        for (JsonNode node : jsonNode.get("scores")) {
            scores.add(new Score(node));
        }

        return scores;
    }
}
