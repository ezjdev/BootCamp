package com.colvir.bootcamp.homework16.producer.service;

import com.colvir.bootcamp.homework16.producer.client.BelarusBankClient;
import com.colvir.bootcamp.homework16.producer.dto.ExchangeRateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.colvir.bootcamp.homework16.producer.config.JmsConfig.QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateProducerService {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    private final BelarusBankClient belarusBankClient;

    @Value("${app.belarusbank.city}")
    private String city;

    @SneakyThrows
    public void sendExchangeRate(ExchangeRateDto exchangeRateDto) {
        jmsTemplate.convertAndSend(QUEUE_NAME
                , objectMapper.writeValueAsString(exchangeRateDto)
                , message -> {
                    message.setStringProperty("officeId", exchangeRateDto.getFilialId());
                    return message;
                }
        );
    }

    public void processExchangeRate() {
        log.info("Sending exchange rate to BelarusBank");
        List<ExchangeRateDto> exchangeRateList = belarusBankClient.getExchangeRate(city);
        exchangeRateList
                .forEach(it -> {
                            log.info("ExchangeRate: {}", it);
                            sendExchangeRate(it);
                        }
                );
    }

}
