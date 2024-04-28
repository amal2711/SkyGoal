package com.example.skyGoal.configuration;

import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import com.example.skyGoal.repository.FootballMatchRepository;
import com.example.skyGoal.repository.WeatherDataRepository;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private FootballMatchRepository footballMatchRepository;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

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

        // Define your Kafka Streams processing logic here
        KStream<String, WeatherData> weatherStream = builder.stream("weather-data");
        KStream<String, FootballMatch> matchStream = builder.stream("football-matches");

        // Example aggregation: Count occurrences of weather data
        weatherStream.groupByKey().count().toStream().foreach((key, value) -> {
            System.out.println("Weather data count for key " + key + ": " + value);
        });
        // Example aggregation: Count occurrences of football matches
        KStream<String, Long> matchCountStream = matchStream.groupByKey().count().toStream();
        matchCountStream.foreach((key, value) -> {
            System.out.println("Football match count for key " + key + ": " + value);
        });

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        return streams;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WeatherData> weatherKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, WeatherData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(WeatherData.class));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FootballMatch> footballMatchKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FootballMatch> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(FootballMatch.class));
        return factory;
    }

    @KafkaListener(topics = "weather-data", containerFactory = "weatherKafkaListenerContainerFactory")
    public void consumeWeatherData(WeatherData data) {
        System.out.println("Received Weather Data: " + data);
        weatherDataRepository.save(data);
    }

    @KafkaListener(topics = "football-matches", containerFactory = "footballMatchKafkaListenerContainerFactory")
    public void consumeFootballMatch(FootballMatch match) {
        System.out.println("Received Football Match: " + match);
        footballMatchRepository.save(match);
    }
}
