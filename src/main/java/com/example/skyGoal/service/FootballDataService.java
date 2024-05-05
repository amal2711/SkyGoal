package com.example.skyGoal.service;

import com.example.skyGoal.entity.FootballMatch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FootballDataService {
    public static FootballMatch extractFootballMatch(String matchJson, ObjectMapper objectMapper) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(matchJson);
        JsonNode match = root.path("matches").get(0);

        String matchId = match.path("id").asText();
        String teamA = match.path("homeTeam").path("name").asText();
        String teamB = match.path("awayTeam").path("name").asText();
        int scoreA = match.path("score").path("fullTime").path("home").asInt();
        int scoreB = match.path("score").path("fullTime").path("away").asInt();
        int matchMinute = match.path("minute").asInt(0);
        LocalDateTime timestamp = LocalDateTime.parse(match.path("utcDate").asText(), DateTimeFormatter.ISO_DATE_TIME);
        String location = match.path("venue").asText();

        return FootballMatch.builder()
                .matchId(matchId)
                .teamA(teamA)
                .teamB(teamB)
                .scoreA(scoreA)
                .scoreB(scoreB)
                .matchMinute(matchMinute)
                .timestamp(timestamp)
                .location(location)
                .build();
    }
}
