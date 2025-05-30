package com.colvir.bootcamp.homework5.dto.security;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    public AuthResponse(String token) { this.token = token; }
}