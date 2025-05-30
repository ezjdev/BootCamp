package com.colvir.bootcamp.homework6.security.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app.jwt")
public class JwtProperties {
    private String secret;
}
