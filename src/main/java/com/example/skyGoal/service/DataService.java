package com.example.skyGoal.service;

import com.example.skyGoal.entity.AggregatedMatchWeatherData;
import com.example.skyGoal.repository.AggregatedMatchWeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DataService {

    @Autowired
    private AggregatedMatchWeatherDataRepository repository;

    public List<AggregatedMatchWeatherData> getLatestAggregatedData() {
        return repository.findTop10ByOrderByMatchTimeDesc();
    }

    public List<AggregatedMatchWeatherData> getLatestData() {
        // Hier können spezifische Filter oder Sortierungen angepasst werden, je nach Anforderung
        return getLatestAggregatedData();
    }
}

