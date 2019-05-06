package com.youkol.shiro.service;

import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Verification;

/**
 * @author jackiea
 */
public interface JwtUserService extends UserService {

    default long getExpireTime() {
        return 2 * 60 * 60 * 1000;
    }

    default String createToken(String username, String password) {
        return createToken(username, password, null);
    }

    default String createToken(String username, String password, Map<String, String> claims) {
        Date expireDate = new Date(System.currentTimeMillis() + getExpireTime());
        Algorithm algorithm = Algorithm.HMAC256(password);

        JWTCreator.Builder builder = JWT.create();
        if (claims != null && !claims.isEmpty()) {
            claims.forEach( (k, v) -> {
                builder.withClaim(k, v);
            });
        }
        String token = builder
            .withClaim("username", username)
            .withExpiresAt(expireDate)
            .sign(algorithm);

        return token;
    }

    default boolean validateToken(String token, String username, String password) {
        return validateToken(token, username, password, null);
    }

    default boolean validateToken(String token, String username, String password, Map<String, String> claims) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);

            Verification verification = JWT.require(algorithm);
            if (claims != null && !claims.isEmpty()) {
                claims.forEach( (k, v) -> {
                    verification.withClaim(k, v);
                });
            }
            JWTVerifier verifier = verification
                .withClaim("username", username)
                .build();
            
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }
}
