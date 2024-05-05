package com.example.skyGoal.entity;

import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Instant;

@Entity
@Table(name = "football_matches")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FootballMatch implements SpecificRecord {
    private static final String SCHEMA_JSON = "{"
            + "\"namespace\": \"com.example.skyGoal.entity\","
            + "\"type\": \"record\","
            + "\"name\": \"FootballMatch\","
            + "\"fields\": ["
            + "  {\"name\": \"matchId\", \"type\": \"string\"},"
            + "  {\"name\": \"teamA\", \"type\": \"string\"},"
            + "  {\"name\": \"teamB\", \"type\": \"string\"},"
            + "  {\"name\": \"scoreA\", \"type\": \"int\"},"
            + "  {\"name\": \"scoreB\", \"type\": \"int\"},"
            + "  {\"name\": \"matchMinute\", \"type\": \"int\"},"
            + "  {\"name\": \"timestamp\", \"type\": \"long\"},"
            + "  {\"name\": \"location\", \"type\": \"string\"}"
            + "]"
            + "}";
    public static final Schema SCHEMA = new Schema.Parser().parse(SCHEMA_JSON);

    @Id
    private String matchId;
    private String teamA;
    private String teamB;
    private int scoreA;
    private int scoreB;
    private int matchMinute;
    private LocalDateTime timestamp;
    private String location;

    @Override
    public void put(int i, Object v) {
        switch(i) {
            case 0: matchId = (String)v; break;
            case 1: teamA = (String)v; break;
            case 2: teamB = (String)v; break;
            case 3: scoreA = (Integer)v; break;
            case 4: scoreB = (Integer)v; break;
            case 5: matchMinute = (Integer)v; break;
            case 6: timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli((Long)v), ZoneId.systemDefault()); break;
            case 7: location = (String)v; break;
            default: throw new IndexOutOfBoundsException("Invalid index: " + i);
        }
    }

    @Override
    public Object get(int i) {
        switch(i) {
            case 0: return matchId;
            case 1: return teamA;
            case 2: return teamB;
            case 3: return scoreA;
            case 4: return scoreB;
            case 5: return matchMinute;
            case 6: return timestamp == null ? null : timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); // convert LocalDateTime to epoch milliseconds
            case 7: return location;
            default: throw new IndexOutOfBoundsException("Invalid index: " + i);
        }
    }

    @Override
    public Schema getSchema() {
        return SCHEMA;
    }
}
