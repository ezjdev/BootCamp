package com.colvir.bootcamp.homework6.security.filter;

import com.colvir.bootcamp.homework6.dto.AuthorizedUser;
import com.colvir.bootcamp.homework6.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

    public static final String JWT_TOKEN = "jwtToken";
    private final JwtUtil jwtUtil;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        String jwtToken = null;
        if (session != null) {
            jwtToken = (String) session.getAttribute(JWT_TOKEN);
        }
        final Optional<String> token = Optional.ofNullable(jwtToken);
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

    private AbstractAuthenticationToken authUserByToken(AuthorizedUser authorizedUser) {
        return new PreAuthenticatedAuthenticationToken(authorizedUser, "-", AuthorityUtils.createAuthorityList(authorizedUser.getRoles()));
    }

}
