package com.colvir.bootcamp.homework16.producer;

import com.colvir.bootcamp.homework16.producer.service.ExchangeRateProducerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ProducerRunner {

    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(ProducerRunner.class, args)) {
            ExchangeRateProducerService service = context.getBean(ExchangeRateProducerService.class);
            SCHEDULER.scheduleAtFixedRate(service::processExchangeRate
                    , 0, 20, TimeUnit.SECONDS);
        }
    }
}