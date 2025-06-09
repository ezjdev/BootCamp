package com.colvir.bootcamp.homework17.producer.service;

import com.colvir.bootcamp.homework17.dto.ExchangeRateDto;
import com.colvir.bootcamp.homework17.producer.client.BelarusBankClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.colvir.bootcamp.homework17.config.KafkaConfig.TOPIC_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateProducerService {

    private final KafkaTemplate<String, ExchangeRateDto> kafkaTemplate;

    private final BelarusBankClient belarusBankClient;

    @Value("${app.belarusbank.city}")
    private String city;

    @SneakyThrows
    public void sendExchangeRate(ExchangeRateDto exchangeRateDto) {
        kafkaTemplate.send(TOPIC_NAME
                        , exchangeRateDto.getFilialId()
                        , exchangeRateDto)
                .whenComplete(
                        (result, exception) -> Optional.ofNullable(exception)
                                .ifPresentOrElse(
                                        it -> log.error("Error while sending event", it),
                                        () -> log.info("""
                                                        
                                                        ExchangeRate: {}
                                                        Offset: {}"""
                                                , exchangeRateDto
                                                , result.getRecordMetadata().offset())
                                )
                );
    }

    public void processExchangeRate() {
        log.info("Getting exchange rate from BelarusBank");
        List<ExchangeRateDto> exchangeRateList = belarusBankClient.getExchangeRate(city);
        exchangeRateList.forEach(this::sendExchangeRate);
    }

}
