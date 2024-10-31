package com.auth.jwt.security;

import com.auth.jwt.entities.AuthUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Brando Eli Carrillo Perez
 */
@Component
public class JWTProvider {

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder()
                .encodeToString(
                        secret.getBytes()
                );
    }

    /**
     * @param authUser entity that represents a user
     *          to be mapped and to be authenticated as a registry into
     *          a database table
     * @return elaborated token string implicitly with claims and the
     *      secret key to authenticate and authorizate users
     */
    public String createToken(AuthUser authUser) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims = Jwts.claims().subject(authUser.getUserName()).build();
        claims.put("id", authUser.getId());
        final Date now = new Date();
        Date exp = new Date(now.getTime() + 3600000 * 72);
        claims.put("exp", exp);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(exp)
                .signWith(SignatureAlgorithm.ES256, secret)
                .compact();
    }

    /**
     * @param token expected string to be equal to authorization
     *              header's token after user authentication
     * @return a boolean indicating whether received token to validate
     * has been verified and matches the expected from successfull login
     */
    public boolean validate(String token) {
        final Optional<Jws<Claims>> optJwsClaims = Optional
                .of(
                        Jwts.parser()
                                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                                .build()
                                .parseSignedClaims(token));
        return optJwsClaims.isPresent();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
