package com.osu;

import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;

public class Leaderboard {
    public List<Score> scores;

    public Leaderboard(JsonNode jsonNode) {
        Score score = new Score();

        for (JsonNode node: jsonNode.get("scores")) {
            score.set(node.fields().next().getValue());
            scores.add(score);
        }
    }
}
