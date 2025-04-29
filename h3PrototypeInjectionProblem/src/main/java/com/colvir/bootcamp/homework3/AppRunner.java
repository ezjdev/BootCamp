package com.colvir.bootcamp.homework3;

import com.colvir.bootcamp.homework3.service.InjectedPrototype;
import com.colvir.bootcamp.homework3.service.MainSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class AppRunner {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context =
                     new AnnotationConfigApplicationContext("com.colvir.bootcamp.homework3.config")) {
            MainSingleton singleton = context.getBean(MainSingleton.class);
            MainSingleton singleton2 = context.getBean(MainSingleton.class);

            log.info("{}", singleton.getId());
            log.info("{}", singleton2.getId());
            log.info("{}", singleton.getId().equals(singleton2.getId()));
            log.info("{}", "---------");

            InjectedPrototype prototype = context.getBean(InjectedPrototype.class);
            InjectedPrototype prototype2 = context.getBean(InjectedPrototype.class);
            log.info("{}", prototype.getId());
            log.info("{}", prototype2.getId());
            log.info("{}", prototype.getId().equals(prototype2.getId()));
        }
    }
}
