package com.example.skyGoal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class DataFetcherService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedRate = 60000) // Ruft Daten jede Minute ab
    public void fetchWeatherData() {
        String city = "Berlin"; // Stadt anpassen
        String url = "http://api.weatherapi.com/v1/current.json?q=" + city + "&key=c4a278c477164080951195137242804";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String weatherData = response.getBody();
            // Verarbeiten und Senden der Daten an Kafka
            kafkaTemplate.send("weather-data", weatherData);
            System.out.println(weatherData);
        } else {
            System.err.println("Fehler beim Abrufen der Wetterdaten");
        }
    }
    @Scheduled(fixedRate = 60000) // Ruft Daten jede Minute ab
    public void fetchFootballMatches() {
        String url = "http://api.football-data.org/v4/teams/759/matches";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String matchData = response.getBody();
            // Verarbeiten und Senden der Daten an Kafka
            kafkaTemplate.send("football-matches", matchData);
            System.out.println(matchData);
        } else {
            System.err.println("Fehler beim Abrufen der Fußballmatch-Daten");
        }
    }

}

