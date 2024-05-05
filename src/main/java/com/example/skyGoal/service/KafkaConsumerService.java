package com.example.skyGoal.service;

import com.example.skyGoal.entity.AggregatedMatchWeatherData;
import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import com.example.skyGoal.repository.AggregatedMatchWeatherDataRepository;
import com.example.skyGoal.repository.FootballMatchRepository;
import com.example.skyGoal.repository.WeatherDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private FootballMatchRepository footballMatchRepository;

    @Autowired
    private AggregatedMatchWeatherDataRepository aggregatedMatchWeatherDataRepository;

    @KafkaListener(topics = "weather-data", groupId = "skygoal-consumer", containerFactory = "weatherKafkaListenerContainerFactory")
    public void listenWeatherData(ConsumerRecord<String, WeatherData> record) {
        WeatherData data = record.value();
        weatherDataRepository.save(data);
    }

    @KafkaListener(topics = "football-matches", groupId = "skygoal-consumer", containerFactory = "footballKafkaListenerContainerFactory")
    public void listenFootballMatch(ConsumerRecord<String, FootballMatch> record) {
        FootballMatch match = record.value();
        footballMatchRepository.save(match);
    }

    @KafkaListener(topics = "aggregated-match-weather", groupId = "skygoal-consumer", containerFactory = "aggregatedMatchWeatherDataKafkaListenerContainerFactory")
    public void listenAggregatedMatchWeatherData(ConsumerRecord<String, AggregatedMatchWeatherData> record) {
        AggregatedMatchWeatherData data = record.value();
        aggregatedMatchWeatherDataRepository.save(data);
    }
}
