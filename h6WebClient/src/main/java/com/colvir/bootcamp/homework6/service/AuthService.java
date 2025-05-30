package com.colvir.bootcamp.homework6.service;

import com.colvir.bootcamp.homework6.api.PlaylistClient;
import com.colvir.bootcamp.homework6.dto.CredentialsDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.colvir.bootcamp.homework6.security.filter.JwtSecurityFilter.JWT_TOKEN;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PlaylistClient playlistClient;
    private final HttpSession session;

    // Authenticate and get JWT token
    public String authenticate(CredentialsDto credentialsDto) {
        return playlistClient.login(credentialsDto).getBody();
    }

    public String getBearerString() {
        return "Bearer ".concat(session.getAttribute(JWT_TOKEN).toString());
    }

}
