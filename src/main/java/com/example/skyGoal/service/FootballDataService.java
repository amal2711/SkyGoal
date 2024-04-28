package com.example.skyGoal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FootballDataService {

    @Autowired
    private RestTemplate restTemplate;

    public String getTeamMatches() {
        String url = "http://api.football-data.org/v4/teams/759/matches";
        return restTemplate.getForObject(url, String.class);
    }
}
