package com.fubukigrin.commands.osu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import org.apache.commons.lang3.math.*;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@SuppressWarnings("unused")

public class osu extends ListenerAdapter {
    private static String BaseUrl = "https://osu.ppy.sh/api/v2/";
    private static String beatmap = "beatmap";
    private static String scores = "beatmap/scores";
    private static String users = "users";

    Endpoints api;

    public osu() throws Exception {
        api = new Endpoints();
    }

    public JsonNode getUserData(String user) throws Exception {
        String key;
        if (NumberUtils.isNumber(user)) { key = "id"; }
        else { key = "username"; }

        String temp = BaseUrl + users + "/" + user + "/osu?key=" + key;
        URI uri = new URI(temp.replace(" ", "%20"));

        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(uri)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", String.format("Bearer %s", api.accessToken))
            .GET()
            .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(postRequest, BodyHandlers.ofString());

        return null;
    }
}