package com.colvir.bootcamp.homework5.security.util;

import com.colvir.bootcamp.homework5.dto.security.AuthorizedUser;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtUtil {

    private static final String LOGIN_CLAIM = "login";
    private static final String RIGHTS_CLAIM = "rights";
    private static final JWSHeader JWT_HEADER = new JWSHeader(JWSAlgorithm.HS256);

    private final JWSSigner jwtSigner;
    private final JWSVerifier jwsVerifier;

    public JwtUtil(JwtProperties jwtProperties) throws JOSEException {
        final String secret = jwtProperties.getSecret();
        this.jwtSigner = new MACSigner(secret);
        this.jwsVerifier = new MACVerifier(secret);
    }

    @SneakyThrows
    public String generateToken(String issuer, String login, String[] rights) {
        Pair<Date, Date> issueAndExpirationTimes = getIssueAndExpirationTimes();
        // Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .subject(login)
                .claim(LOGIN_CLAIM, login)
                .claim(RIGHTS_CLAIM, rights)
                .issueTime(issueAndExpirationTimes.getLeft())
                .expirationTime(issueAndExpirationTimes.getRight())
                .build();

        SignedJWT signedJWT = new SignedJWT(JWT_HEADER, claimsSet);

        // Apply the HMAC protection
        signedJWT.sign(jwtSigner);

        // Serialize to compact form, produces something like that (jwt sample is below)
        // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
        return signedJWT.serialize();
    }

    private Pair<Date, Date> getIssueAndExpirationTimes() {
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        Instant expirationAt = issuedAt.plus(Duration.ofDays(1));
        return Pair.of(Date.from(issuedAt), Date.from(expirationAt));
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
