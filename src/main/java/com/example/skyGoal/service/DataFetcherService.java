package com.example.skyGoal.service;

import com.example.skyGoal.converterHelper.AvroConverter;
import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.skyGoal.converterHelper.AvroConverter.convertToAvroRecord;

@Service
public class DataFetcherService {

    @Autowired
    private KafkaTemplate<String, GenericRecord> kafkaTemplate;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(fixedRate = 60000)
    public void fetchWeatherData() {
        String url = "http://api.weatherapi.com/v1/current.json?q=London&key=c4a278c477164080951195137242804";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                // Convert JSON string to JsonNode
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode locationNode = rootNode.path("location");
                String locationName = locationNode.path("name").asText();  // Extrahiere den Namen

                // Erstelle manuell eine Instanz von WeatherData
                WeatherData weatherData = new WeatherData();
                weatherData.setLocation(locationName);  // Setze den Standort als den extrahierten Namen
                weatherData.setTemperature(rootNode.path("current").path("temp_c").floatValue());
                weatherData.setHumidity(rootNode.path("current").path("humidity").floatValue());
                weatherData.setWindSpeed(rootNode.path("current").path("wind_kph").floatValue());
                weatherData.setTimestamp(LocalDateTime.now());  // Aktuelle Zeit als Annahme für Einfachheit

                // Konvertiere zu Avro-Record und sende zu Kafka
                GenericRecord avroRecord = convertToAvroRecord(weatherData);
                kafkaTemplate.send("weather-data", avroRecord);
                System.out.println("Sent Avro data to Kafka");
            } catch (Exception e) {
                System.err.println("Error processing weather data: " + e.getMessage());
            }
        } else {
            System.err.println("Failed to fetch weather data: HTTP Status " + response.getStatusCode());
        }
    }

        ;

    @Scheduled(fixedRate = 60000)
    public void fetchFootballMatches() {
        LocalDate today = LocalDate.now();
        String url = String.format("http://api.football-data.org/v4/matches?dateFrom=%s&dateTo=%s", today, today);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                // Parse the whole JSON response
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode matchesNode = rootNode.path("matches");  // Navigate to the 'matches' array

                // Convert the 'matches' node to an array of FootballMatch
                FootballMatch[] matches = objectMapper.treeToValue(matchesNode, FootballMatch[].class);

                // Process each match
                for (FootballMatch match : matches) {
                    GenericRecord avroRecord = convertToAvroRecord(match);
                    kafkaTemplate.send("football-matches", avroRecord);
                }
                System.out.println("Sent Avro data to Kafka");
            } catch (Exception e) {
                System.err.println("Error processing football data: " + e.getMessage());
            }
        } else {
            System.err.println("Failed to fetch football matches: HTTP Status " + response.getStatusCode());
        }
    }


}
