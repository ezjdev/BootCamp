package com.colvir.bootcamp.homework2.service;

import com.colvir.bootcamp.homework2.api.TemperatureConverter;
import org.springframework.stereotype.Component;

@Component(value = "celsiusToKelvin")
public class CelsiusToKelvinConverterService implements TemperatureConverter {

    @Override
    public Double convert(Double value) {
        return value + 273.15;
    }

}
