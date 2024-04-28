package com.example.skyGoal.configuration;

import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import com.example.skyGoal.repository.FootballMatchRepository;
import com.example.skyGoal.repository.WeatherDataRepository;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static org.slf4j.LoggerFactory.*;

@Configuration
public class KafkaStreamsConfig {

    private static final Logger logger = getLogger(KafkaStreamsConfig.class);

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private FootballMatchRepository footballMatchRepository;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    @Bean
    public KafkaStreams kafkaStreams() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "skygoal-streams");
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams/skygoal-streams-" + UUID.randomUUID().toString());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        StreamsBuilder builder = new StreamsBuilder();
        defineStreams(builder);

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        return streams;
    }

//    private void defineStreams(StreamsBuilder builder) {
//        KStream<String, WeatherData> weatherStream = builder.stream("weather-data");
//        KStream<String, FootballMatch> matchStream = builder.stream("football-matches");
//
//        // Total goals scored per match
//        matchStream.groupByKey()
//                .aggregate(() -> 0, (key, match, aggregate) -> aggregate + match.getScoreA() + match.getScoreB(), Materialized.with(Serdes.String(), Serdes.Integer()))
//                .toStream()
//                .foreach((key, value) -> logger.info("Total goals scored in match {}: {}", key, value));
//
//        // Goal difference per match
//        matchStream.foreach((key, match) -> {
//            int goalDifference = Math.abs(match.getScoreA() - match.getScoreB());
//            logger.info("Goal difference in match {}: {}", key, goalDifference);
//        });
//
//        // Average temperature during matches
//        weatherStream.groupByKey()
//                .windowedBy(TimeWindows.of(Duration.ofMinutes(matchDurationMinutes)))
//                .aggregate(
//                        () -> 0.0f,
//                        (key, weather, sum) -> sum + weather.getTemperature(),
//                        Materialized.with(Serdes.String(), Serdes.Float())
//                )
//                .toStream()
//                .mapValues((windowedKey, tempSum) -> tempSum / matchDurationMinutes)
//                .foreach((key, avgTemp) -> logger.info("Average temperature during match from {} to {}: {}", key.window().startTime(), key.window().endTime(), avgTemp));
//    }
private void defineStreams(StreamsBuilder builder) {
    KStream<String, WeatherData> weatherStream = builder.stream("weather-data",
            Consumed.with(Serdes.String(), new JsonSerde<>(WeatherData.class)));

    KStream<String, FootballMatch> matchStream = builder.stream("football-matches",
            Consumed.with(Serdes.String(), new JsonSerde<>(FootballMatch.class)));

    // Join the weather and match streams based on the timestamps
    matchStream.join(weatherStream,
                    this::matchWeatherJoiner, // Implementieren Sie eine Join-Funktion
                    JoinWindows.of(Duration.ofHours(2)), // Zeitfenster von +/- 2 Stunden um die Match-Zeit
                    StreamJoined.with(
                            Serdes.String(),
                            new JsonSerde<>(FootballMatch.class),
                            new JsonSerde<>(WeatherData.class))
            )
            // Nehmen Sie hier weitere Transformationen vor, z.B. Aggregation
            // ...
            .to("aggregated-match-weather", Produced.with(Serdes.String(), new JsonSerde<>(AggregatedMatchWeatherData.class)));
}

    private AggregatedMatchWeatherData matchWeatherJoiner(FootballMatch match, WeatherData weather) {
        // Prüfen, ob der Wetterdaten-Zeitstempel mit dem Match-Zeitstempel innerhalb eines bestimmten Zeitfensters übereinstimmt
        // Hier ist angenommen, dass das Match und das Wetter das gleiche Format für Zeitstempel verwenden und beide in Millisekunden seit Epoche (UTC) sind
        long matchTime = match.getTimestamp();
        long weatherTime = weather.getTimestamp();
        long timeDifference = Math.abs(matchTime - weatherTime);

        // Wenn das Wetter innerhalb von +/- 2 Stunden des Matches liegt, dann fassen wir es zusammen
        if (timeDifference <= Duration.ofHours(2).toMillis()) {
            return new AggregatedMatchWeatherData(match, weather);
        } else {
            // Wenn nicht, dann ignorieren oder handhaben Sie diesen Fall entsprechend
            return null; // oder eine neue Instanz von AggregatedMatchWeatherData mit default Werten
        }
    }

}
