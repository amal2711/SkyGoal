package com.example.skyGoal.repository;

import com.example.skyGoal.entity.AggregatedMatchWeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AggregatedMatchWeatherDataRepository extends JpaRepository<AggregatedMatchWeatherData, String> {
    List<AggregatedMatchWeatherData> findTop10ByOrderByMatchTimeDesc(); // das macht nichts??
}
