package com.example.skyGoal.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            if (request.getURI().getHost().contains("football-data.org")) {
                request.getHeaders().add("X-Auth-Token", "d220966069ac4f7c9c334b05dc7c9ee7");
            } else if (request.getURI().getHost().contains("weatherapi.com")) {
                request.getHeaders().add("key", "c4a278c477164080951195137242804");
            }
            return execution.execute(request, body);
        });

        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}

