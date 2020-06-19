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

import java.util.Collections;
import java.util.Map;

import com.youkol.support.shiro.jwt.token.exception.ExpiredJwtTokenException;
import com.youkol.support.shiro.jwt.token.exception.IllegalJwtTokenException;
import com.youkol.support.shiro.jwt.token.exception.JwtTokenException;
import com.youkol.support.shiro.service.UserAccount;

/**
 * Jwt Token Service.
 *
 * @author jackiea
 */
public interface JwtTokenService {

    public static final long TOKEN_EXPIRED_TIME = 24 * 60 * 60 * 1000;

    public static final String USERID_KEY = "userId";

    public static final String USERNAME_KEY = "username";

    /**
     * Returns the token expired time.
     * UNIT: Milliseconds
     * the default time is 24 hours.
     *
     * @return token expired time
     */
    default long getExpireTime() {
        return TOKEN_EXPIRED_TIME;
    }

    default String getUserIdKey() {
        return USERID_KEY;
    }

    default String getUsernameKey() {
        return USERNAME_KEY;
    }

    default String generateToken(UserAccount userAccount) throws JwtTokenException {
        return this.generateToken(userAccount, Collections.emptyMap());
    }

    /**
     * Generate a token by specific parameters.
     * <p><strong>Note: The claim of token must have the username.</strong>
     *
     * @param userAccount User information.
     * @param otherClaims Other claims you ready put to token.
     * @return Generated token
     * @throws JwtTokenException If generate token failed.
     */
    String generateToken(UserAccount userAccount, Map<String, Object> otherClaims) throws JwtTokenException;

    boolean validateToken(String token, UserAccount userAccount, Map<String, Object> otherClaims) throws ExpiredJwtTokenException, IllegalJwtTokenException;

    default boolean validateToken(String token, UserAccount userAccount) throws ExpiredJwtTokenException, IllegalJwtTokenException {
        return this.validateToken(token, userAccount, Collections.emptyMap());
    }

    String getUsernameFromToken(String token);

    Object getClaimFromToken(String token, String key);

}
