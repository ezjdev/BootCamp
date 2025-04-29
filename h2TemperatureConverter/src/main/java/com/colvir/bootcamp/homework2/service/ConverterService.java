package com.colvir.bootcamp.homework2.service;

import com.colvir.bootcamp.homework2.api.TemperatureConverter;
import com.colvir.bootcamp.homework2.exception.TemperatureConverterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConverterService {

    private final Map<String, TemperatureConverter> converters;

    public Double converter(String type, Double value) {
        return Optional.ofNullable(converters.get(type))
                .map(converter -> converter.convert(value))
                .orElseThrow(() -> new TemperatureConverterNotFoundException(type));
    }
}
