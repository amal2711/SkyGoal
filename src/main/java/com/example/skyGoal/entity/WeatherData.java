package com.example.skyGoal.entity;

import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "weather_data")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData implements SpecificRecord {
    private static final String SCHEMA_JSON = "{"
            + "\"namespace\": \"com.example.skyGoal.entity\","
            + "\"type\": \"record\","
            + "\"name\": \"WeatherData\","
            + "\"fields\": ["
            + "  {\"name\": \"id\", \"type\": \"long\"},"
            + "  {\"name\": \"location\", \"type\": \"string\"},"
            + "  {\"name\": \"temperature\", \"type\": \"float\"},"
            + "  {\"name\": \"humidity\", \"type\": \"float\"},"
            + "  {\"name\": \"windSpeed\", \"type\": \"float\"},"
            + "  {\"name\": \"timestamp\", \"type\": \"long\"}"
            + "]"
            + "}";
    private static final Schema SCHEMA = new Schema.Parser().parse(SCHEMA_JSON);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String location;
    private float temperature;
    private float humidity;
    private float windSpeed;
    private LocalDateTime timestamp;

    @Override
    public void put(int i, Object v) {
        switch(i) {
            case 0: id = (Long)v; break;
            case 1: location = (String)v; break;
            case 2: temperature = (Float)v; break;
            case 3: humidity = (Float)v; break;
            case 4: windSpeed = (Float)v; break;
            case 6: timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli((Long)v), ZoneId.systemDefault()); break;
            default: throw new IndexOutOfBoundsException("Invalid index: " + i);
        }
    }

    @Override
    public Object get(int i) {
        switch(i) {
            case 0: return id;
            case 1: return location;
            case 2: return temperature;
            case 3: return humidity;
            case 4: return windSpeed;
            case 6: return timestamp == null ? null : timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); // convert LocalDateTime to epoch milliseconds
            default: throw new IndexOutOfBoundsException("Invalid index: " + i);
        }
    }

    @Override
    public Schema getSchema() {
        return SCHEMA;
    }
}
