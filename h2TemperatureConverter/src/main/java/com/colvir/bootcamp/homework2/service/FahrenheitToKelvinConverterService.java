package com.colvir.bootcamp.homework2.service;

import com.colvir.bootcamp.homework2.api.TemperatureConverter;
import org.springframework.stereotype.Component;

@Component(value = "fahrenheitToKelvin")
public class FahrenheitToKelvinConverterService implements TemperatureConverter {
    @Override
    public Double convert(Double value) {
        return (value - 32) * ((double) 5 / 9) + 273.15;
    }
}
