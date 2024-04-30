package com.example.skyGoal.service;

import com.example.skyGoal.converterHelper.AvroConverter;
import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        String city = "Berlin";
        String url = "http://api.weatherapi.com/v1/current.json?q=" + city + "&key=c4a278c477164080951195137242804";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                WeatherData weatherData = WeatherDataService.extractWeatherData(response.getBody(), objectMapper);
                GenericRecord avroRecord = convertToAvroRecord(weatherData); // Methode, die das WeatherData Objekt in ein GenericRecord umwandelt
                kafkaTemplate.send("weather-data", avroRecord);
                System.out.println("Sent Avro data to Kafka");
            } catch (Exception e) {
                System.err.println("Error processing weather data: " + e.getMessage());
            }
        }
        ;
    }
        @Scheduled(fixedRate = 60000)
        public void fetchFootballMatches () {
            String url = "http://api.football-data.org/v4/teams/759/matches";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                try {
                    FootballMatch footballMatch = FootballDataService.extractFootballMatch(response.getBody(), objectMapper);
                    GenericRecord avroRecord = AvroConverter.convertToAvroRecord(footballMatch);
                    kafkaTemplate.send("football-matches", avroRecord);
                    System.out.println("Sent Avro data to Kafka");
                } catch (Exception e) {
                    System.err.println("Fehler bei der Verarbeitung der Fußballdaten: " + e.getMessage());
                }
            } else {
                System.err.println("Fehler beim Abrufen der Fußballmatch-Daten: HTTP-Status " + response.getStatusCode());
            }
        }

    }
