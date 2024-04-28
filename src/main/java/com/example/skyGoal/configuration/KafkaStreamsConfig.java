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
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
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

    private <T> ConsumerFactory<String, T> consumerFactory(Class<T> type) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "skygoal-consumer-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

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
        KStream<String, WeatherData> weatherStream = builder.stream("weather-data");
        KStream<String, FootballMatch> matchStream = builder.stream("football-matches");

        // Total goals scored per match
        matchStream.groupByKey()
                .aggregate(() -> 0, (key, match, aggregate) -> aggregate + match.getScoreA() + match.getScoreB(), Materialized.with(Serdes.String(), Serdes.Integer()))
                .toStream()
                .foreach((key, value) -> logger.info("Total goals scored in match {}: {}", key, value));

        // Goal difference per match
        matchStream.foreach((key, match) -> {
            int goalDifference = Math.abs(match.getScoreA() - match.getScoreB());
            logger.info("Goal difference in match {}: {}", key, goalDifference);
        });

        // Average temperature during matches
        weatherStream.groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofMinutes(matchDurationMinutes)))
                .aggregate(
                        () -> 0.0f,
                        (key, weather, sum) -> sum + weather.getTemperature(),
                        Materialized.with(Serdes.String(), Serdes.Float())
                )
                .toStream()
                .mapValues((windowedKey, tempSum) -> tempSum / matchDurationMinutes)
                .foreach((key, avgTemp) -> logger.info("Average temperature during match from {} to {}: {}", key.window().startTime(), key.window().endTime(), avgTemp));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WeatherData> weatherKafkaListenerContainerFactory() {
        return createFactory(WeatherData.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FootballMatch> footballMatchKafkaListenerContainerFactory() {
        return createFactory(FootballMatch.class);
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> createFactory(Class<T> clazz) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(clazz));
        return factory;
    }

    @KafkaListener(topics = "weather-data", containerFactory = "weatherKafkaListenerContainerFactory")
    public void consumeWeatherData(WeatherData data) {
        logger.info("Received Weather Data: {}", data);
        weatherDataRepository.save(data);
    }

    @KafkaListener(topics = "football-matches", containerFactory = "footballMatchKafkaListenerContainerFactory")
    public void consumeFootballMatch(FootballMatch match) {
        logger.info("Received Football Match: {}", match);
        footballMatchRepository.save(match);
    }
}
