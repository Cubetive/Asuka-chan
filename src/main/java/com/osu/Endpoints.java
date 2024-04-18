package com.osu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.python.util.PythonInterpreter;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.awt.Desktop;

import io.github.cdimascio.dotenv.Dotenv;

public class Endpoints {
    private String Oauth = "https://osu.ppy.sh/oauth/";
    private String Token = "token";
    private String authorize = "authorize";

    private String clientID;
    private String clientSecret;
    private String redirectURI;
    private String code;

    public String accessToken;
    public String refreshToken;

    ObjectMapper objectMapper;

    public Endpoints() throws Exception {
        Dotenv config = Dotenv.configure().load();

        clientID = config.get("client_id");
        clientSecret = config.get("client_secret");
        redirectURI = config.get("redirect_uri");

        objectMapper = new ObjectMapper();

        File file = new File("tokens.json");
        if (!file.exists() || file.isDirectory()) {
            requestData();
        }
        else {
            JsonNode node = objectMapper.readTree(file);

            accessToken = node.get("access_token").asText();
            refreshToken = node.get("refresh_token").asText();
        }

        refreshData();
    }

    public void requestData() throws Exception {
        String params = String.format(
            "?client_id=%s&redirect_uri=%s&response_type=code&scope=public",
            clientID, redirectURI
        );
        URI oauth = new URI(Oauth + authorize + params);

        try {
            PythonInterpreter pyInterp = new PythonInterpreter();
            Desktop desktop = Desktop.getDesktop();

            desktop.browse(oauth);

            String port = redirectURI.split(":")[1];
            String pythonWebServer = "from socket import socket, AF_INET, SOCK_STREAM\n"+
                "serversocket = socket(AF_INET, SOCK_STREAM)\n"+
                String.format("serversocket.bind(('localhost', %s))\n", port)+
                "serversocket.listen(1)\n"+
                "connection, _ = serversocket.accept()\n"+
                "data = str(connection.recv(8192))\n"+
                "connection.send(b'HTTP/1.0 200 OK\\n')\n"+
                "connection.send(b'Content-Type: text/html\\n')\n"+
                "connection.send(b'\\n')\n"+
                "connection.send(b\"\"\"<html><body>\n"+
                "        <h2>Code received.</h2>\n"+
                "        You may now close this tab safely.\n"+
                "        </body></html>\"\"\")\n"+
                "code = data.split('code=')[1].split(' ')[0]";
            pyInterp.exec(pythonWebServer);

            code = pyInterp.get("code").asString();
            pyInterp.cleanup();
            pyInterp.close();

            getToken();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public String getToken() throws Exception {
        String params = String.format(
            "client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code&redirect_uri=%s",
            clientID, clientSecret, code, redirectURI
        );

        URI oauth = new URI(Oauth + Token);

        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(oauth)
            .header("Accept", "application/json")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(BodyPublishers.ofString(params))
            .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(postRequest, BodyHandlers.ofString());

        JsonNode node = objectMapper.readTree(response.body());

        accessToken = node.get("access_token").asText();
        refreshToken = node.get("refresh_token").asText();

        saveCredentials();

        return null;
    }

    public void saveCredentials() throws Exception {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("access_token", accessToken); 
        jsonNode.put("refresh_token", refreshToken); 
        objectMapper.writeValue(new File("tokens.json"), jsonNode);
    }

    public void refreshData() throws Exception {
        String params = String.format(
            "client_id=%s&client_secret=%s&grant_type=refresh_token&refresh_token=%s",
            clientID, clientSecret, refreshToken
        );

        URI oauth = new URI(Oauth + Token);

        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(oauth)
            .header("Accept", "application/json")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(BodyPublishers.ofString(params))
            .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(postRequest, BodyHandlers.ofString());

        JsonNode node = objectMapper.readTree(response.body());

        accessToken = node.get("access_token").asText();
        refreshToken = node.get("refresh_token").asText();

        saveCredentials();
    }
}
