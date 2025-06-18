package com.colvir.bootcamp.homework5.controller;

import com.colvir.bootcamp.homework5.dto.security.CredentialsDto;
import com.colvir.bootcamp.homework5.service.AuthService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    public static final String USERNAME_ALREADY_REGISTERED = "Username already registered";
    public static final String USER_REGISTERED = "User registered";
    private final AuthService authService;

    public static final String INVALID_CREDENTIALS = "Invalid credentials";

    @Timed("register")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CredentialsDto credentials) {
        return Optional.ofNullable(credentials)
                .filter(authService::register)
                .map(it -> ResponseEntity.ok(USER_REGISTERED))
                .orElseGet(() -> ResponseEntity.badRequest().body(USERNAME_ALREADY_REGISTERED));
    }

    @Timed("login")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody CredentialsDto credentials) {
        return Optional.ofNullable(credentials)
                .map(authService::login)
                .filter(Predicate.not(String::isBlank))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_CREDENTIALS));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/secured/role")
    public String isAdmin() {
        return authService.isAdmin();
    }
}
