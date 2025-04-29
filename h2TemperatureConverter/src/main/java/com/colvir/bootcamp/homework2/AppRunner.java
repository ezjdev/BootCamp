package com.colvir.bootcamp.homework2;

import com.colvir.bootcamp.homework2.api.TemperatureUnit;
import com.colvir.bootcamp.homework2.service.ConverterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
public class AppRunner {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context
                     = SpringApplication.run(AppRunner.class, args)){
            ConverterService converterService = context.getBean(ConverterService.class);
            Stream.of(TemperatureUnit.values())
                    .forEach(unit ->
                            log.info("{} {}"
                                    , unit
                                    , converterService.converter(unit.getName(), 20d)));
        }
    }
}
