package com.colvir.bootcamp.homework2;

import com.colvir.bootcamp.homework2.api.TemperatureUnit;
import com.colvir.bootcamp.homework2.config.ApplicationConfig;
import com.colvir.bootcamp.homework2.service.ConverterService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.stream.Stream;

public class AppRunner {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext annotationConfigApplicationContext =
                     new AnnotationConfigApplicationContext(ApplicationConfig.class)) {
            ConverterService converterService = annotationConfigApplicationContext.getBean(ConverterService.class);
            Stream.of(TemperatureUnit.values())
                    .forEach(unit -> System.out.println(unit + " " + converterService.converter(unit.getName(), 20d)));
        }
    }
}
