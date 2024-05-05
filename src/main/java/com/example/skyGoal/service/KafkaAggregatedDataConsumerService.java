package com.example.skyGoal.service;

import com.example.skyGoal.entity.AggregatedMatchWeatherData;
import com.example.skyGoal.repository.AggregatedMatchWeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaAggregatedDataConsumerService {

    @Autowired
    private AggregatedMatchWeatherDataRepository aggregatedDataRepository;

    @KafkaListener(topics = "aggregated-match-weather", groupId = "skygoal-consumer", containerFactory = "aggregatedMatchWeatherDataKafkaListenerContainerFactory")
    public void listenAggregatedMatchWeatherData(AggregatedMatchWeatherData data) {
        aggregatedDataRepository.save(data);
        System.out.println("Aggregated data saved: " + data);
    }
}
