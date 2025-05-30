package com.colvir.bootcamp.homework6;

import com.colvir.bootcamp.homework6.security.util.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class WebClientRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebClientRunner.class, args);
    }

}
