package com.example.skyGoal.service;

import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import com.example.skyGoal.repository.FootballMatchRepository;
import com.example.skyGoal.repository.WeatherDataRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

        @Autowired
        private WeatherDataRepository weatherDataRepository;

        @Autowired
        private FootballMatchRepository footballMatchRepository;

        @KafkaListener(topics = "weather-data", groupId = "skygoal-consumer")
        public void listenWeatherData(WeatherData data) {
            weatherDataRepository.save(data);
        }

        @KafkaListener(topics = "football-matches", groupId = "skygoal-consumer")
        public void listenFootballMatch(FootballMatch match) {
            footballMatchRepository.save(match);
        }
    }

