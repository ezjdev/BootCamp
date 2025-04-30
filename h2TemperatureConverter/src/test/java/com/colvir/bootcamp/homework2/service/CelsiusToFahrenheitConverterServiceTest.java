package com.colvir.bootcamp.homework2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CelsiusToFahrenheitConverterServiceTest {

    public static final CelsiusToFahrenheitConverterService underTest =
            new CelsiusToFahrenheitConverterService();

    @ParameterizedTest
    @CsvSource(value = {"20.0, 68.0"})
    void conversionTest(Double fahrenheit, Double kelvin){
        Assertions.assertEquals(underTest.convert(fahrenheit), kelvin);
    }

}