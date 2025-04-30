package com.colvir.bootcamp.homework2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KelvinToCelsiusConverterServiceTest {

    public static final KelvinToCelsiusConverterService underTest =
            new KelvinToCelsiusConverterService();

    @ParameterizedTest
    @CsvSource(value = {"20.0, -253.14999999999998"})
    void conversionTest(Double fahrenheit, Double kelvin){
        Assertions.assertEquals(underTest.convert(fahrenheit), kelvin);
    }

}