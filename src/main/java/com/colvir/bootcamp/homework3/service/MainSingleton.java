package com.colvir.bootcamp.homework3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainSingleton {

    @Lookup
    public InjectedPrototype getPrototype() {
        return null;
    }

    public Long getId() {
        return getPrototype().getId();
    }

}
