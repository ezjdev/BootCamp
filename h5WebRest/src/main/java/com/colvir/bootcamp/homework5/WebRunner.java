package com.colvir.bootcamp.homework5;

import com.colvir.bootcamp.homework5.security.util.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class WebRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebRunner.class, args);
    }
}
