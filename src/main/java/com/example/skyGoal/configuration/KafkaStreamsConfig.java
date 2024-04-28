package com.example.skyGoal.configuration;

import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import com.example.skyGoal.repository.FootballMatchRepository;
import com.example.skyGoal.repository.WeatherDataRepository;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
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
import scala.Tuple2;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStreamsConfig.class);

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private FootballMatchRepository footballMatchRepository;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.match-duration-minutes:90}")
    private long matchDurationMinutes;

    @Bean
    public KafkaStreams kafkaStreams() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "skygoal-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        StreamsBuilder builder = new StreamsBuilder();
        defineStreams(builder);

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        return streams;
    }

    private void defineStreams(StreamsBuilder builder) {
        KStream<String, WeatherData> weatherStream = builder.stream("weather-data", Consumed.with(Serdes.String(), Serdes.serdeFrom(WeatherData.class)))
                .map((key, weather) -> new KeyValue<>(weather.getLocation(), weather));

        KStream<String, FootballMatch> matchStream = builder.stream("football-matches", Consumed.with(Serdes.String(), Serdes.serdeFrom(FootballMatch.class)))
                .map((key, match) -> new KeyValue<>(match.getLocation(), match));

        // Join the streams based on location and process directly
        matchStream.join(weatherStream,
                        (match, weather) -> "Match at " + match.getLocation() + ": " +
                                "Goals: " + (match.getScoreA() + match.getScoreB()) + ", " +
                                "Temperature: " + weather.getTemperature() + "°C",
                        JoinWindows.of(Duration.ofMinutes(90)) // Adjust the window duration based on expected alignment of data timestamps
                )
                .foreach((key, value) -> logger.info(value));
    }

}
