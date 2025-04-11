package com.colvir.bootcamp.homework1;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        Stream.of(String.join(" ", args)
                        .split("[^0-9А-яA-z]")
                )
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.groupingBy(Function.identity()
                                            , LinkedHashMap::new
                                            , Collectors.counting())
                )
                .entrySet()
                .stream()
                .sorted(Map.Entry
                            .<String, Long>comparingByValue()
                            .reversed())
                .map(Map.Entry::getKey)
                .forEach(System.out::println);
    }

}
