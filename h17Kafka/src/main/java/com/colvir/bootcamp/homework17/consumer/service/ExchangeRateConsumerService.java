package com.colvir.bootcamp.homework17.consumer.service;

import com.colvir.bootcamp.homework17.dto.ExchangeRateDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.colvir.bootcamp.homework17.config.KafkaConfig.TOPIC_NAME;

@Slf4j
@Service
@Getter
public class ExchangeRateConsumerService {

    private final List<ExchangeRateDto> receivedExchangeRateList = new CopyOnWriteArrayList<>();

    @KafkaListener(topics = TOPIC_NAME)
    public void listen(@Payload ExchangeRateDto exchangeRateDto) {
        log.info("Received: {}", exchangeRateDto);
        receivedExchangeRateList.add(exchangeRateDto);
    }

}
