package com.colvir.bootcamp.homework17.consumer.service;

import com.colvir.bootcamp.homework17.dto.ExchangeRateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.colvir.bootcamp.homework17.config.KafkaConfig.TOPIC_NAME;

@Slf4j
@Service
public class ExchangeRateConsumerService {

    private static final List<ExchangeRateDto> RECEIVED_EXCHANGE_RATE_LIST = new ArrayList<>();

    public List<ExchangeRateDto> getReceivedExchangeRateList() {
        return RECEIVED_EXCHANGE_RATE_LIST;
    }

    @KafkaListener(topics = TOPIC_NAME)
    public void listen(@Payload ExchangeRateDto exchangeRateDto) {
        log.info("Received: {}", exchangeRateDto);
        RECEIVED_EXCHANGE_RATE_LIST.add(exchangeRateDto);
    }

}
