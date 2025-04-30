package com.colvir.bootcamp.homework2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FahrenheitToKelvinConverterServiceTest {

    public static final FahrenheitToKelvinConverterService underTest =
            new FahrenheitToKelvinConverterService();

    @ParameterizedTest
    @CsvSource(value = {"20.0, 266.4833333333333"})
    void conversionTest(Double fahrenheit, Double kelvin){
        Assertions.assertEquals(underTest.convert(fahrenheit), kelvin);
    }

}