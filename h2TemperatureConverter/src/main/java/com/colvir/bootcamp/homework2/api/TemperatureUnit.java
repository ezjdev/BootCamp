package com.colvir.bootcamp.homework2.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TemperatureUnit {
    FAHRENHEIT_TO_CELSIUS("fahrenheitToCelsius"),
    FAHRENHEIT_TO_KELVIN("fahrenheitToKelvin"),
    KELVIN_TO_CELSIUS("kelvinToCelsius"),
    KELVIN_TO_FAHRENHEIT("kelvinToFahrenheit"),
    CELSIUS_TO_KELVIN("celsiusToKelvin"),
    CELSIUS_TO_FAHRENHEIT("celsiusToFahrenheit");

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
