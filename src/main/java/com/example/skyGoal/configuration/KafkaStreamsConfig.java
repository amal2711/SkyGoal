package com.example.skyGoal.configuration;

import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${schema.registry.url}")
    private String schemaRegistryUrl;
    @Bean
    public ProducerFactory<String, GenericRecord> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put("schema.registry.url", schemaRegistryUrl);

        return new DefaultKafkaProducerFactory<>(configProps);
    }
    @Bean
    public KafkaTemplate<String, GenericRecord> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    @Bean
    public KafkaStreams kafkaStreams() {
        StreamsBuilder builder = new StreamsBuilder();
        Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", schemaRegistryUrl);

        Serde<FootballMatch> footballMatchSerde = new SpecificAvroSerde<>();
        Serde<WeatherData> weatherDataSerde = new SpecificAvroSerde<>();
        Serde<AggregatedMatchWeatherData> aggregatedMatchWeatherDataSerde = new SpecificAvroSerde<>();

        footballMatchSerde.configure(serdeConfig, false);
        weatherDataSerde.configure(serdeConfig, false);
        aggregatedMatchWeatherDataSerde.configure(serdeConfig, false);

        KStream<String, FootballMatch> matchesStream = builder.stream(
                "football-matches", Consumed.with(Serdes.String(), footballMatchSerde));

        KStream<String, WeatherData> weatherStream = builder.stream(
                "weather-data", Consumed.with(Serdes.String(), weatherDataSerde));

        KStream<String, AggregatedMatchWeatherData> joinedStream = matchesStream.join(
                weatherStream,
                this::matchWeatherJoiner,
                JoinWindows.ofTimeDifferenceAndGrace(Duration.ofHours(1), Duration.ofMinutes(5)),
                StreamJoined.with(Serdes.String(), footballMatchSerde, weatherDataSerde));

        joinedStream.to("aggregated-match-weather", Produced.with(Serdes.String(), aggregatedMatchWeatherDataSerde));

        Properties props = streamProperties();
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        return streams;
    }

    private Properties streamProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "skygoal-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return props;
    }

    private AggregatedMatchWeatherData matchWeatherJoiner(FootballMatch match, WeatherData weather) {
        return new AggregatedMatchWeatherData(
                match.getMatchId(),
                match.getLocation(),
                match.getTeamA(),
                match.getTeamB(),
                match.getScoreA(),
                match.getScoreB(),
                weather.getTemperature(),
                AggregatedMatchWeatherData.formatTimestamp(match.getTimestamp()),
                match.getMatchMinute()
        );
    }
}
