package com.colvir.bootcamp.homework6.security.util;

import com.colvir.bootcamp.homework6.dto.AuthorizedUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtUtil {

    private static final String LOGIN_CLAIM = "login";
    private static final String RIGHTS_CLAIM = "rights";

    private final JWSVerifier jwsVerifier;

    public JwtUtil(JwtProperties jwtProperties) throws JOSEException {
        final String secret = jwtProperties.getSecret();
        this.jwsVerifier = new MACVerifier(secret);
    }

    @SneakyThrows
    public AuthorizedUser parseToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        final JWTClaimsSet jwtClaims;
        final SignedJWT decodedJWT = SignedJWT.parse(token);
        if (decodedJWT.verify(jwsVerifier) && isValid(jwtClaims = decodedJWT.getJWTClaimsSet())) {
            final String login = this.<String>getClaim(jwtClaims, LOGIN_CLAIM)
                    .filter(StringUtils::isNotEmpty).orElseThrow();
            final String[] userRights = this.<List<String>>getClaim(jwtClaims, RIGHTS_CLAIM)
                    .map(list -> list.toArray(String[]::new)).orElse(new String[]{});
            return new AuthorizedUser(login, userRights);
        }
        throw new IllegalArgumentException();
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<T> getClaim(JWTClaimsSet jwtClaims, String claim) {
        return Optional.ofNullable((T) jwtClaims.getClaim(claim));
    }

    public boolean isValid(JWTClaimsSet jwtClaims) {
        Date referenceTime = new Date();
        Date expirationTime = jwtClaims.getExpirationTime();
        Date notBeforeTime = jwtClaims.getNotBeforeTime();
        boolean expired = expirationTime != null && expirationTime.before(referenceTime);
        boolean yetValid = notBeforeTime == null || notBeforeTime.before(referenceTime);
        return !expired && yetValid;
    }

}
