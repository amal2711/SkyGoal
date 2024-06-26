package com.example.skyGoal.service;

import com.example.skyGoal.entity.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class WeatherDataService {
    public static WeatherData extractWeatherData(String weatherJson, ObjectMapper objectMapper) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(weatherJson);
        String location = root.path("location").path("name").asText();
        float temperature = (float) root.path("current").path("temp_c").asDouble();
        float humidity = (float) root.path("current").path("humidity").asDouble();
        float windSpeed = (float) root.path("current").path("wind_kph").asDouble();
        Instant timestamp = Instant.ofEpochSecond(root.path("location").path("localtime_epoch").asLong());

        return WeatherData.builder()
                .location(location)
                .temperature(temperature)
                .humidity(humidity)
                .windSpeed(windSpeed)
                .timestamp(timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
    }

}
