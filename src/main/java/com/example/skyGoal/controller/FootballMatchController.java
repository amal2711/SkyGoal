package com.example.skyGoal.controller;

import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.repository.FootballMatchRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/football")
public class FootballMatchController {

    private final FootballMatchRepository repository;

    public FootballMatchController(FootballMatchRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<FootballMatch> getAllMatches() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public FootballMatch getMatchById(@PathVariable String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Match not found"));
    }
}
