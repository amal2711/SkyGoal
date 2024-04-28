package com.example.skyGoal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SkyGoalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkyGoalApplication.class, args);
	}

	}
