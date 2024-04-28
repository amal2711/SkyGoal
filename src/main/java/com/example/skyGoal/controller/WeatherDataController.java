package com.example.skyGoal.controller;

import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import com.example.skyGoal.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/weather")
public class WeatherDataController {

    private final WeatherDataRepository weatherDataRepository;

    @Autowired
    public WeatherDataController(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    @GetMapping
    public List<WeatherData> getAllWeather() {
        return weatherDataRepository.findAll();
    }

}