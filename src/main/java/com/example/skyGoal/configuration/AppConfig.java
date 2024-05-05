package com.example.skyGoal.configuration;

import com.example.skyGoal.entity.AggregatedMatchWeatherData;
import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {
    @Value("${schema.registry.url}")
    private String schemaRegistryUrl;

    private Map<String, Object> baseConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, KafkaAvroDeserializer.class.getName());
        props.put("schema.registry.url", schemaRegistryUrl);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
        props.put("auto.register.schemas", false);
        return props;
    }

    private ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> configs = baseConsumerConfigs();
        KafkaAvroDeserializer kafkaAvroDeserializer = new KafkaAvroDeserializer();
        kafkaAvroDeserializer.configure(configs, false);  // false indicates this is for value, not the key
        return new DefaultKafkaConsumerFactory<>(
                configs,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(kafkaAvroDeserializer)
        );
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WeatherData> weatherKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FootballMatch> footballKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AggregatedMatchWeatherData> aggregatedMatchWeatherDataKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory();
    }
}
