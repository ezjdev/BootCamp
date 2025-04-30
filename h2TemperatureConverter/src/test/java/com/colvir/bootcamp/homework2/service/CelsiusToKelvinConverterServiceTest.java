package com.colvir.bootcamp.homework2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CelsiusToKelvinConverterServiceTest {

    public static final CelsiusToKelvinConverterService underTest =
            new CelsiusToKelvinConverterService();

    @ParameterizedTest
    @CsvSource(value = {"20.0, 293.15"})
    void conversionTest(Double fahrenheit, Double kelvin){
        Assertions.assertEquals(underTest.convert(fahrenheit), kelvin);
    }

}