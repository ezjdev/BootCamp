package com.colvir.bootcamp.homework5.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Authorities {

    public static final String ROLE_ADMIN = UserPrincipal.ROLE_PREFIX + "ADMIN";

    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(ROLE_ADMIN::equals);
    }

}
