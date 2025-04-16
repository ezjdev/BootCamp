package com.colvir.bootcamp.homework2.service;

import com.colvir.bootcamp.homework2.api.TemperatureConverter;
import org.springframework.stereotype.Component;

@Component(value = "fahrenheitToCelsius")
public class FahrenheitToCelsiusConverterService implements TemperatureConverter {
    @Override
    public Double convert(Double value) {
        return (value - 32) * ((double) 5 / 9);
    }
}
