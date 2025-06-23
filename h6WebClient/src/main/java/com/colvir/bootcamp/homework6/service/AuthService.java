package com.colvir.bootcamp.homework6.service;

import com.colvir.bootcamp.homework6.feign.PlaylistClient;
import com.colvir.bootcamp.homework6.dto.CredentialsDto;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
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
    @Timed("authenticate")
    public String authenticate(CredentialsDto credentialsDto) {
        return playlistClient.login(credentialsDto).getBody();
    }

    @Counted("get_bearer_string")
    public String getBearerString() {
        return "Bearer ".concat(session.getAttribute(JWT_TOKEN).toString());
    }

}
