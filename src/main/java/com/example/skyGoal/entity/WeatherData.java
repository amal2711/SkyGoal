package com.example.skyGoal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weather_data")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String location;
    private float temperature;
    private float humidity;
    private float windSpeed;
    private long timestamp;
}
