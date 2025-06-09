package com.colvir.bootcamp.homework17.producer.scheduler;

import com.colvir.bootcamp.homework17.producer.service.ExchangeRateProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final ExchangeRateProducerService producerService;

    @Scheduled(fixedDelay = 20_000)
    public void run() {
        producerService.processExchangeRate();
    }
}
