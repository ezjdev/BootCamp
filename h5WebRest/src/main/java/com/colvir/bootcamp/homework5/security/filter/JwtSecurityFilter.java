package com.colvir.bootcamp.homework5.security.filter;

import com.colvir.bootcamp.homework5.security.util.JwtUtil;
import com.colvir.bootcamp.homework5.dto.security.AuthorizedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final Optional<String> token = getTokenIfPresent(request);
        // if token passed into request header
        if (token.isPresent()) {
            final AuthorizedUser authorizedUser = jwtUtil.parseToken(token.get());
            if (authorizedUser != null) {
                Authentication authenticationToken = authUserByToken(authorizedUser);
                // propagate authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    public Optional<String> getTokenIfPresent(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.contains("Bearer"))
                .map(authHeader -> authHeader.split("Bearer ")[1]);
    }

    private AbstractAuthenticationToken authUserByToken(AuthorizedUser authorizedUser) {
        return new PreAuthenticatedAuthenticationToken(authorizedUser, "-", AuthorityUtils.createAuthorityList(authorizedUser.getRoles()));
    }

}
