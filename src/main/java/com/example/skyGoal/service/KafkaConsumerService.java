package com.example.skyGoal.service;

import com.example.skyGoal.configuration.AggregatedMatchWeatherData;
//import com.example.skyGoal.configuration.SpecificAvroDeserializer;
import com.example.skyGoal.configuration.PublicSpecificAvroDeserializer;
import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import com.example.skyGoal.repository.FootballMatchRepository;
import com.example.skyGoal.repository.WeatherDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroDeserializer;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaConsumerService {

    @Autowired
    private ObjectMapper objectMapper; // Stelle sicher, dass ObjectMapper autowired ist.

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private FootballMatchRepository footballMatchRepository;

    @KafkaListener(topics = "weather-data", groupId = "skygoal-consumer")
    public void listenWeatherData(ConsumerRecord<String, String> record) {
        try {
            WeatherData data = objectMapper.readValue(record.value(), WeatherData.class);
            weatherDataRepository.save(data);
        } catch (Exception e) {
            // Hier solltest du eine angemessene Fehlerbehandlung hinzufügen.
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "football-matches", groupId = "skygoal-consumer")
    public void listenFootballMatch(ConsumerRecord<String, String> record) {
        try {
            FootballMatch match = objectMapper.readValue(record.value(), FootballMatch.class);
            footballMatchRepository.save(match);
        } catch (Exception e) {
            // Hier solltest du eine angemessene Fehlerbehandlung hinzufügen.
            e.printStackTrace();
        }
    }
    private Map<String, Object> baseConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SpecificAvroDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "skygoal-consumer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put("schema.registry.url", "http://localhost:8081");
        return props;
    }
    @Bean
    public ConsumerFactory<String, AggregatedMatchWeatherData> aggregatedMatchWeatherConsumerFactory() {
        Map<String, Object> props = consumerConfigs(AggregatedMatchWeatherData.class.getName());
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new PublicSpecificAvroDeserializer<>(AggregatedMatchWeatherData.class));
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AggregatedMatchWeatherData> aggregatedMatchWeatherKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(AggregatedMatchWeatherData.class, AggregatedMatchWeatherData.class.getName());
    }

    private <T extends SpecificRecordBase> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(Class<T> valueType, String valueDefaultType) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
                consumerConfigs(valueDefaultType),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new PublicSpecificAvroDeserializer<T>(valueType))  // Explizite Typangabe hier
        ));
        return factory;
    }



    private Map<String, Object> consumerConfigs(String valueDefaultType) {
        Map<String, Object> props = new HashMap<>(baseConsumerConfigs());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueDefaultType);
        return props;
    }

}


