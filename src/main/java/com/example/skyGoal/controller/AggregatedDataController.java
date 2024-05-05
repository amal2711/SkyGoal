package com.example.skyGoal.controller;

import com.example.skyGoal.entity.AggregatedMatchWeatherData;
import com.example.skyGoal.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/aggregated")
public class AggregatedDataController {

    @Autowired
    private DataService dataService;  // Verweis auf DataService statt KafkaAggregatedDataConsumerService

    @GetMapping("/latest")
    public ResponseEntity<List<AggregatedMatchWeatherData>> getLatestAggregatedData() {
        List<AggregatedMatchWeatherData> data = dataService.getLatestData();  // Aufruf von DataService
        return data.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(data);
    }
}
