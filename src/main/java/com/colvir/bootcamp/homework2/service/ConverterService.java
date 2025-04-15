package com.colvir.bootcamp.homework2.service;

import com.colvir.bootcamp.homework2.api.Converter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConverterService {

    private final Map<String, Converter> converters;

    public ConverterService(Map<String, Converter> converters) {
        this.converters = converters;
    }

    public Double converter(String type, Double value) {
        return converters.get(type).convert(value);
    }
}
