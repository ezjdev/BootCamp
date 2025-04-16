package com.colvir.bootcamp.homework2.service;

import com.colvir.bootcamp.homework2.api.Converter;
import org.springframework.stereotype.Component;


@Component(value = "celsiusToFahrenheit")
public class CelsiusToFahrenheitConverterService implements Converter {

    @Override
    public Double convert(Double value) {
        return (value * ((double) 9 / 5)) + 32;
    }

}
