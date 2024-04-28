package com.example.skyGoal.repository;

import com.example.skyGoal.entity.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, String> {
}
