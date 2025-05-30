package com.colvir.bootcamp.homework5.service;

import com.colvir.bootcamp.homework5.dto.security.AuthorizedUser;
import com.colvir.bootcamp.homework5.dto.security.CredentialsDto;
import com.colvir.bootcamp.homework5.model.security.Role;
import com.colvir.bootcamp.homework5.model.security.Status;
import com.colvir.bootcamp.homework5.model.security.User;
import com.colvir.bootcamp.homework5.repository.RoleRepository;
import com.colvir.bootcamp.homework5.repository.UserRepository;
import com.colvir.bootcamp.homework5.security.Authorities;
import com.colvir.bootcamp.homework5.security.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.colvir.bootcamp.homework5.security.UserPrincipal.ROLE_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    public static final String SERVICE = "service";
    public static final String USER = "USER";

    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String NO_USER = "NoUser";
    public static final String ADMIN = "admin";
    public static final String NOT_ADMIN = "not admin";
    private final Authorities authorities;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public boolean register(CredentialsDto credentials) {
        return Optional.ofNullable(credentials)
                .filter(it -> it.getPassword() != null && it.getLogin() != null)
                .map(it -> userRepository.findByLogin(it.getLogin()).isPresent())
                .filter(Boolean.TRUE::equals)
                .map(it -> !it)
                .orElseGet(() -> {
                    Optional.ofNullable(credentials)
                            .map(it -> User.builder()
                                    .login(credentials.getLogin())
                                    .status(Status.OK)
                                    .password(passwordEncoder.encode(credentials.getPassword()))
                                    .role(roleRepository.findByName(USER).orElse(new Role().setName(USER)))
                                    .build())
                            .map(userRepository::save)
                            .orElseThrow(() -> new RuntimeException(INVALID_CREDENTIALS));
                    return true;
                });
    }

    @Transactional
    public String login(CredentialsDto credentials) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getLogin(), credentials.getPassword()));
            return jwtUtil.generateToken(SERVICE
                    , credentials.getLogin()
                    , new String[]{
                            userRepository.findByLogin(credentials.getLogin())
                                    .map(it -> it.getRole().getName())
                                    .map(ROLE_PREFIX::concat)
                                    .orElse("")}
            );
        } catch (AuthenticationException ex) {
            log.error(INVALID_CREDENTIALS, ex);
            return "";
        }
    }

    public String isAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof PreAuthenticatedAuthenticationToken authToken
                && authToken.getPrincipal() instanceof AuthorizedUser authorizedUser) {
            return "%s is %s".formatted(authorizedUser.getUsername(), authorities.isAdmin() ? ADMIN : NOT_ADMIN);
        }
        return NO_USER;
    }

}
