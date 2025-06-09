package com.colvir.bootcamp.homework17.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static com.colvir.bootcamp.homework17.config.KafkaConfig.TOPIC_NAME;

@Slf4j
@Service
public class ExchangeRateConsumerService {

    @KafkaListener(topics = TOPIC_NAME)
    public void listen(@Payload String exchangeRateDto) {
        log.info("Received exchange rate {}", exchangeRateDto);
    }

}
