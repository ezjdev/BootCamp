package com.colvir.bootcamp.homework16.producer.config;

import com.colvir.bootcamp.homework16.producer.client.BelarusBankClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableJms
public class JmsConfig {

    public static final String QUEUE_NAME = "exchangeRateQueue";

    @Value("${app.belarusbank.url}")
    private String belarusBankUrl;

    @Bean
    public BelarusBankClient belarusBankClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(belarusBankUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(BelarusBankClient.class);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
