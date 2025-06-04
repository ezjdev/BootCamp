package com.colvir.bootcamp.homework16.consumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class JmsConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
