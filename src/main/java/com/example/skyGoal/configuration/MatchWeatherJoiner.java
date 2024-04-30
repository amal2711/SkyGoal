//package com.example.skyGoal.configuration;
//
//import com.example.skyGoal.entity.FootballMatch;
//import com.example.skyGoal.entity.WeatherData;
//import org.apache.kafka.streams.kstream.ValueJoiner;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//
//// Define the ValueJoiner
//public class MatchWeatherJoiner implements ValueJoiner<FootballMatch, WeatherData, AggregatedMatchWeatherData> {
//    @Override
//    public AggregatedMatchWeatherData apply(FootballMatch match, WeatherData weather) {
//        // Convert both times to the same timezone, here assumed both are already in UTC for simplification
//        ZonedDateTime matchTime = Instant.ofEpochMilli(match.getTimestamp()).atZone(ZoneId.of("UTC"));
//        ZonedDateTime weatherTime = Instant.ofEpochSecond(weather.getTimestamp()).atZone(ZoneId.of("UTC"));
//
//        // Check if weather data is within one hour of match start
//        if (Math.abs(Duration.between(matchTime, weatherTime).toHours()) <= 1) {
//            // Assuming the locations are the same, or this check would be here
//            return new AggregatedMatchWeatherData(
//                    match.getMatchId(),
//                    match.getLocation(), // assuming location is the competition name or similar
//                    match.getTeamA(),
//                    match.getTeamB(),
//                    match.getScoreA(),
//                    match.getScoreB(),
//                    weather.getTemperature(),
//                    matchTime.toString(), // Match time in UTC as string
//                    match.getMatchMinute() // Directly taken from match data
//            );
//        } else {
//            return null; // or handle this scenario appropriately
//        }
//    }
//}
//
