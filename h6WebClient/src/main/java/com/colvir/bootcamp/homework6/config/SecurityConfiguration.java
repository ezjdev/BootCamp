package com.colvir.bootcamp.homework6.config;

import com.colvir.bootcamp.homework6.security.filter.JwtSecurityFilter;
import com.colvir.bootcamp.homework6.security.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/css/**", "/images/**", "/actuator", "/actuator/**").permitAll()
                        .anyRequest().hasRole("USER")
                )
                .addFilterBefore(new JwtSecurityFilter(jwtUtil), BasicAuthenticationFilter.class)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}