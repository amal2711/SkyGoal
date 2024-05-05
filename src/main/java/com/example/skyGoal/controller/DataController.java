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
@RequestMapping("/api/data")
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    // Endpunkt, um die neuesten aggregierten Daten zu erhalten

    @GetMapping("/aggregated")
    public ResponseEntity<List<AggregatedMatchWeatherData>> getAggregatedData() {
        List<AggregatedMatchWeatherData> data = dataService.getLatestAggregatedData();
        return data.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(data);
    }

}



