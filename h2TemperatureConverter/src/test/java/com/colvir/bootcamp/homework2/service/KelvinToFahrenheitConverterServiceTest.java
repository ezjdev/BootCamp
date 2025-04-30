package com.colvir.bootcamp.homework2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KelvinToFahrenheitConverterServiceTest {

    public static final KelvinToFahrenheitConverterService underTest =
            new KelvinToFahrenheitConverterService();

    @ParameterizedTest
    @CsvSource(value = {"20.0, -423.66999999999996"})
    void conversionTest(Double fahrenheit, Double kelvin){
        Assertions.assertEquals(underTest.convert(fahrenheit), kelvin);
    }
}