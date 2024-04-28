package com.example.skyGoal;

import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;
import com.example.skyGoal.repository.FootballMatchRepository;
import com.example.skyGoal.repository.WeatherDataRepository;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.Value;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableKafka
public class SkyGoalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkyGoalApplication.class, args);
	}


	@Configuration
	public class KafkaConsumerConfig {

		@Autowired
		private WeatherDataRepository weatherDataRepository;

		@Autowired
		private FootballMatchRepository footballMatchRepository;

		private <T> ConsumerFactory<String, T> consumerFactory(Class<T> type) {
			Map<String, Object> props = new HashMap<>();
			props.put("group.id", "skygoal-consumer-group");
			props.put("auto.offset.reset", "earliest");
			props.put("key.deserializer", StringDeserializer.class);
			props.put("value.deserializer", JsonDeserializer.class);
			props.put("spring.json.trusted.packages", "*");

			return new DefaultKafkaConsumerFactory<>(props);
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
	}}
