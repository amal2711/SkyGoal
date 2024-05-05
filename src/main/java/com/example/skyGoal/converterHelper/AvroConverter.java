package com.example.skyGoal.converterHelper;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;

import java.time.ZoneId;

public class AvroConverter {

    private static final String WEATHER_SCHEMA_STRING = "{\"namespace\": \"com.skygoal.weather\", \"type\": \"record\", \"name\": \"WeatherData\", \"fields\": [{\"name\": \"location\", \"type\": \"string\"}, {\"name\": \"temperature\", \"type\": \"float\"}, {\"name\": \"humidity\", \"type\": \"float\"}, {\"name\": \"windSpeed\", \"type\": \"float\"}, {\"name\": \"timestamp\", \"type\": \"long\"}]}";
    private static final Schema WEATHER_SCHEMA = new Schema.Parser().parse(WEATHER_SCHEMA_STRING);

    private static final String FOOTBALL_SCHEMA_STRING = "{\"namespace\": \"com.skygoal.sports\", \"type\": \"record\", \"name\": \"FootballMatch\", \"fields\": [{\"name\": \"matchId\", \"type\": \"string\"}, {\"name\": \"teamA\", \"type\": \"string\"}, {\"name\": \"teamB\", \"type\": \"string\"}, {\"name\": \"scoreA\", \"type\": \"int\"}, {\"name\": \"scoreB\", \"type\": \"int\"}, {\"name\": \"matchMinute\", \"type\": \"int\"}, {\"name\": \"timestamp\", \"type\": \"long\"}, {\"name\": \"location\", \"type\": \"string\"}]}";
    private static final Schema FOOTBALL_SCHEMA = new Schema.Parser().parse(FOOTBALL_SCHEMA_STRING);

    public static GenericRecord convertToAvroRecord(WeatherData weatherData) {
        GenericRecord avroRecord = new GenericData.Record(WEATHER_SCHEMA);
        avroRecord.put("location", weatherData.getLocation());
        avroRecord.put("temperature", weatherData.getTemperature());
        avroRecord.put("humidity", weatherData.getHumidity());
        avroRecord.put("windSpeed", weatherData.getWindSpeed());
        avroRecord.put("timestamp", weatherData.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return avroRecord;
    }

    public static GenericRecord convertToAvroRecord(FootballMatch footballMatch) {
        GenericRecord avroRecord = new GenericData.Record(FOOTBALL_SCHEMA);
        avroRecord.put("matchId", footballMatch.getMatchId());
        avroRecord.put("teamA", footballMatch.getTeamA());
        avroRecord.put("teamB", footballMatch.getTeamB());
        avroRecord.put("scoreA", footballMatch.getScoreA());
        avroRecord.put("scoreB", footballMatch.getScoreB());
        avroRecord.put("matchMinute", footballMatch.getMatchMinute());
        avroRecord.put("timestamp", footballMatch.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());  // Ensure this conversion is correct
        avroRecord.put("location", footballMatch.getLocation());
        return avroRecord;
    }
}
