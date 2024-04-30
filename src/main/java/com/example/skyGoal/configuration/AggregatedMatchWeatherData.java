package com.example.skyGoal.configuration;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.specific.SpecificRecordBase;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor  // No-argument constructor for deserialization
@AllArgsConstructor // All-argument constructor for easy creation
@Getter
@Setter
public class AggregatedMatchWeatherData extends SpecificRecordBase {
    private String matchId;
    private String competitionName;
    private String teamA;
    private String teamB;
    private int scoreA;
    private int scoreB;
    private float temperature;
    private String matchTime;
    private Integer matchMinute;

    private static final Schema SCHEMA = new Schema.Parser().parse("{\"namespace\": \"com.skygoal.sports\", \"type\": \"record\", \"name\": \"AggregatedMatchWeatherData\", \"fields\": [{\"name\": \"matchId\", \"type\": \"string\"}, {\"name\": \"competitionName\", \"type\": \"string\"}, {\"name\": \"teamA\", \"type\": \"string\"}, {\"name\": \"teamB\", \"type\": \"string\"}, {\"name\": \"scoreA\", \"type\": \"int\"}, {\"name\": \"scoreB\", \"type\": \"int\"}, {\"name\": \"temperature\", \"type\": \"float\"}, {\"name\": \"matchTime\", \"type\": \"string\"}, {\"name\": \"matchMinute\", \"type\": \"int\"}]}");

    @Override
    public void put(int i, Object v) {
        switch(i) {
            case 0: matchId = v.toString(); break;
            case 1: competitionName = v.toString(); break;
            case 2: teamA = v.toString(); break;
            case 3: teamB = v.toString(); break;
            case 4: scoreA = (Integer)v; break;
            case 5: scoreB = (Integer)v; break;
            case 6: temperature = (Float)v; break;
            case 7: matchTime = v.toString(); break;
            case 8: matchMinute = (Integer)v; break;
            default: throw new IndexOutOfBoundsException("Invalid index: " + i);
        }
    }

    @Override
    public Object get(int i) {
        switch(i) {
            case 0: return matchId;
            case 1: return competitionName;
            case 2: return teamA;
            case 3: return teamB;
            case 4: return scoreA;
            case 5: return scoreB;
            case 6: return temperature;
            case 7: return matchTime;
            case 8: return matchMinute;
            default: throw new IndexOutOfBoundsException("Invalid index: " + i);
        }
    }


    @Override
    public Schema getSchema() {
        return SCHEMA;
    }

    public static String formatTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"));
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
