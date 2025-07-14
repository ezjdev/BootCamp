package com.colvir.bootcamp.homework26;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class WebClientRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebClientRunner.class, args);
    }

}
