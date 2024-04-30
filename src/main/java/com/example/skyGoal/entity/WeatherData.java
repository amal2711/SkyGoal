package com.example.skyGoal.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;

@Entity
@Table(name = "weather_data")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData implements SpecificRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String location;
    private float temperature;
    private float humidity;
    private float windSpeed;
    private long timestamp;

    @Override
    public void put(int i, Object v) {

    }

    @Override
    public Object get(int i) {
        return null;
    }

    @Override
    public Schema getSchema() {
        return null;
    }

}
