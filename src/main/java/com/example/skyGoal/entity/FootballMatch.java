package com.example.skyGoal.entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;

import java.time.Instant;

@Entity
@Table(name = "football_matches")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FootballMatch implements SpecificRecord {
    @Id
    private String matchId;
    private String teamA;
    private String teamB;
    private int scoreA;
    private int scoreB;
    private int matchMinute;
    private long timestamp;
    private String location;

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
