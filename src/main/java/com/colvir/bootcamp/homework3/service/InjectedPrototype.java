package com.colvir.bootcamp.homework3.service;

import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Random;

@Getter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InjectedPrototype {

    private static final Random RANDOM = new Random();
    private final Long id = RANDOM.nextLong();

}
