package com.colvir.bootcamp.homework16.consumer.service;

import com.colvir.bootcamp.homework16.consumer.dto.ExchangeRateDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateConsumerService {

    private final ObjectMapper objectMapper;

    public static final String QUEUE_NAME = "exchangeRateQueue";

    @JmsListener(destination = QUEUE_NAME, selector = "officeId = '2678'")
    public void onMessage(Message message) throws JMSException, JsonProcessingException {
        if (message instanceof TextMessage textMessage) {
            String text = textMessage.getText();
            ExchangeRateDto exchangeRateDto = objectMapper.readValue(text, ExchangeRateDto.class);
            log.info("Received exchange rate: {}", exchangeRateDto);
        }
    }

}
