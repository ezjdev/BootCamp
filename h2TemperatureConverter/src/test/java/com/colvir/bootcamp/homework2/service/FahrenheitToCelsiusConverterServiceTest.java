package com.colvir.bootcamp.homework2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FahrenheitToCelsiusConverterServiceTest {

    public static final FahrenheitToCelsiusConverterService underTest =
            new FahrenheitToCelsiusConverterService();

    @ParameterizedTest
    @CsvSource(value = {"20.0, -6.666666666666667"})
    void conversionTest(Double fahrenheit, Double kelvin){
        Assertions.assertEquals(underTest.convert(fahrenheit), kelvin);
    }

}