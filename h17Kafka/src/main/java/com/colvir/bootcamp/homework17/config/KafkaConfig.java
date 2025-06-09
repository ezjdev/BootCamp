package com.colvir.bootcamp.homework17.config;

import com.colvir.bootcamp.homework17.producer.client.BelarusBankClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@EnableKafka
@EnableScheduling
@Configuration
public class KafkaConfig {

    public static final String TOPIC_NAME = "colvir.events";

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
    public NewTopic topic() {
        return TopicBuilder.name(TOPIC_NAME).partitions(2).build();
    }

}
