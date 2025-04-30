package com.colvir.bootcamp.homework3.service;

import com.colvir.bootcamp.homework3.config.ApplicationConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = {ApplicationConfig.class})
class MainSingletonTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testSpringInjection() {
        MainSingleton singleton = context.getBean(MainSingleton.class);
        MainSingleton singleton2 = context.getBean(MainSingleton.class);

        Assertions.assertNotEquals(singleton.getId(), singleton2.getId(), "The value obtained from Singleton must not be equal");

        InjectedPrototype prototype = context.getBean(InjectedPrototype.class);
        InjectedPrototype prototype2 = context.getBean(InjectedPrototype.class);
        Assertions.assertNotEquals(prototype.getId(), prototype2.getId(), "The value obtained from Prototype must not be equal");
    }

}