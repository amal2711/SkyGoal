package com.example.skyGoal.configuration;

import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    // Basis Konfiguration für alle Kafka Consumers
    private Map<String, Object> baseConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "skygoal-consumer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(StreamsConfig.STATE_DIR_CONFIG, "C:/Users/amam2/IdeaProjects/StreamKafka/SkyGoal");
        return props;
    }

    @Bean
    public ConsumerFactory<String, WeatherData> weatherConsumerFactory() {
        Map<String, Object> props = consumerConfigs(WeatherData.class.getName());
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(WeatherData.class));
    }

    @Bean
    public ConsumerFactory<String, FootballMatch> footballConsumerFactory() {
        Map<String, Object> props = consumerConfigs(FootballMatch.class.getName());
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(FootballMatch.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WeatherData> weatherKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(WeatherData.class, WeatherData.class.getName());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FootballMatch> footballKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(FootballMatch.class, FootballMatch.class.getName());
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(Class<T> valueType, String valueDefaultType) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
                consumerConfigs(valueDefaultType), new StringDeserializer(), new ErrorHandlingDeserializer<>(new JsonDeserializer<>(valueType))
        ));
        return factory;
    }

    private Map<String, Object> consumerConfigs(String valueDefaultType) {
        Map<String, Object> props = new HashMap<>(baseConsumerConfigs());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueDefaultType);
        return props;
    }
}

