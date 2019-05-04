package com.youkol.shiro.service;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

/**
 * @author jackiea
 */
public interface JwtUserService extends UserService {

    default long getExpireTime() {
        return 2 * 60 * 60 * 1000;
    }

    default String createToken(String username, String password) {
        Date expireDate = new Date(System.currentTimeMillis() + getExpireTime());
        Algorithm algorithm = Algorithm.HMAC256(password);
        String token = JWT.create()
            .withClaim("username", username)
            .withExpiresAt(expireDate)
            .sign(algorithm);

        return token;
    }

    default boolean validateToken(String token, String username, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("username", username)
                .build();
            
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }
}
