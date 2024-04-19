package com.osu;

import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;

public class Leaderboard {
    public List<Score> scores;

    public Leaderboard(JsonNode jsonNode) {
        for (JsonNode node: jsonNode.get("scores")) {
            scores.add(new Score(node.fields().next().getValue()));
        }
    }
}
