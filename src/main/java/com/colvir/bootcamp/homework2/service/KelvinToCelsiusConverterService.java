package com.colvir.bootcamp.homework2.service;

import com.colvir.bootcamp.homework2.api.Converter;
import org.springframework.stereotype.Component;

@Component(value = "kelvinToCelsius")
public class KelvinToCelsiusConverterService implements Converter {
    @Override
    public Double convert(Double value) {
        return value - 273.15;
    }
}
