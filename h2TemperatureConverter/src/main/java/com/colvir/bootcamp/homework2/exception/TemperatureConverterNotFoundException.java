package com.colvir.bootcamp.homework2.exception;

public class TemperatureConverterNotFoundException extends RuntimeException {
    public TemperatureConverterNotFoundException(String type) {
        super(String.format("Temperature converter %s not found", type));
    }
}
