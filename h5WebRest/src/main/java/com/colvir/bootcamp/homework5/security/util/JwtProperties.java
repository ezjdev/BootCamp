package com.colvir.bootcamp.homework5.security.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app.jwt")
public class JwtProperties {
    private String secret;
}
