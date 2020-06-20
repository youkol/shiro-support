/**
 * Copyright (C) 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youkol.support.shiro.jwt.token;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.util.StandardCharset;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.youkol.support.shiro.jwt.token.exception.ExpiredJwtTokenException;
import com.youkol.support.shiro.jwt.token.exception.IllegalJwtTokenException;
import com.youkol.support.shiro.jwt.token.exception.JwtTokenException;
import com.youkol.support.shiro.service.UserAccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default Implementation for JwtTokenService.
 *
 * @see <a href="https://bitbucket.org/connect2id/nimbus-jose-jwt/wiki/Home">https://bitbucket.org/connect2id/nimbus-jose-jwt/wiki/Home</a>
 * @author jackiea
 */
public class DefaultJwtTokenService extends AbstractJwtTokenService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultJwtTokenService.class);


    protected byte[] getSecretKey(UserAccount userAccount) throws KeyLengthException {
        String secret = "";
        if (this.getSharedSecret() != null) {
            secret = secret + this.getSharedSecret();
        }
        if (userAccount != null) {
            if (userAccount.getPassword() != null) {
                secret = secret + userAccount.getPassword();
            }
            if (secret == null || secret.isEmpty()) {
                secret = userAccount.getUsername() + userAccount.getUserId();
            }
        }

        if (secret.isEmpty()) {
            // The secret. Must be at least 256 bits long and not null.
            // Generate random 256*8-bit (32*8-byte) shared secret
            SecureRandom random = new SecureRandom();
            byte[] randomSecret = new byte[32*8];
            random.nextBytes(randomSecret);

            // put to sharedSecret
            this.setSharedSecret(new String(randomSecret, StandardCharset.UTF_8));
            return randomSecret;
        }

        return secret.getBytes(StandardCharset.UTF_8);
    }

    protected JWSSigner getSigner(UserAccount userAccount) throws KeyLengthException {
        JWSSigner signer = new MACSigner(this.getSecretKey(userAccount));
        return signer;
    }

    protected JWSVerifier getVerifier(UserAccount userAccount) throws JOSEException {
        JWSVerifier verifier = new MACVerifier(this.getSecretKey(userAccount));
        return verifier;
    }

    @Override
    public String generateToken(UserAccount userAccount, Map<String, Object> otherClaims) throws JwtTokenException {
        // Prepare data
        Date expiredDate = new Date(System.currentTimeMillis() + getExpireTime());
        Date createdDate = new Date();

        String username = userAccount.getUsername();

        try {
            // Create HMAC signer
            JWSSigner signer = this.getSigner(userAccount);

            // Prepare JWT with claims set
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer(this.getIssuer())
                .issueTime(createdDate)
                .expirationTime(expiredDate);
            Map<String, Object> claims = this.combineClaims(userAccount, otherClaims);
            claims.forEach( (k, v) -> {
                builder.claim(k, v);
            });

            JWTClaimsSet claimsSet = builder.build();

            // Prepare JWT header
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);

            // Apply the HMAC protection
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException ex) {
            throw new JwtTokenException("Failed to generate token.", ex);
        }
    }

    @Override
    public boolean validateToken(String token, UserAccount userAccount, Map<String, Object> otherClaims) throws ExpiredJwtTokenException, IllegalJwtTokenException {
        try {
            // parse the JWS and verify its HMAC
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = this.getVerifier(userAccount);

            boolean bResult = signedJWT.verify(verifier);

            if (new Date().after(signedJWT.getJWTClaimsSet().getExpirationTime())) {
                throw new ExpiredJwtTokenException("The token is expired.");
            }

            return bResult;
        } catch (ParseException ex) {
            logger.error("An error occured when parse token [" + token + "].", ex);
            throw new IllegalJwtTokenException("The token is illegal.", ex);
        } catch (JOSEException ex) {
            logger.error("An error occured when verify token [" + token + "].", ex);
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            String username = (String)claimsSet.getClaim(this.getUsernameKey());

            return username;
        } catch (ParseException ex) {
            logger.error("An error occured when parse token [" + token + "].", ex);
        }

        return null;
    }

    @Override
    public Object getClaimFromToken(String token, String key) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Object result = claimsSet.getClaim(key);

            return result;
        } catch (ParseException ex) {
            logger.error("An error occured when parse token [" + token + "].", ex);
        }

        return null;
    }

}
