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

import java.util.HashMap;
import java.util.Map;

import com.youkol.support.shiro.service.UserAccount;

/**
 * @author jackiea
 */
public abstract class AbstractJwtTokenService implements JwtTokenService {

    public static final String ROLES_KEY = "roles";

    public static final String PERMS_KEY = "perms";

    private String userIdKey = JwtTokenService.USERID_KEY;

    private String usernameKey = JwtTokenService.USERNAME_KEY;

    private String rolesKey = ROLES_KEY;

    private String permsKey = PERMS_KEY;

    private long expireTime = JwtTokenService.TOKEN_EXPIRED_TIME;

    private String issuer = DEFAULT_ISSUER;

    private String sharedSecret;

    protected Map<String, Object> combineClaims(UserAccount userAccount, Map<String, Object> otherClaims) {
        Map<String, Object> claims = new HashMap<>();
        if (otherClaims != null) {
            claims.putAll(otherClaims);
        }
        if (userAccount == null) {
            return claims;
        }

        claims.put(this.getUserIdKey(), userAccount.getUserId());
        claims.put(this.getUsernameKey(), userAccount.getUsername());
        claims.put(this.getRolesKey(), userAccount.getRoles());
        claims.put(this.getPermsKey(), userAccount.getPermissions());

        return claims;
    }

    public String getUserIdKey() {
        return userIdKey;
    }

    public void setUserIdKey(String userIdKey) {
        this.userIdKey = userIdKey;
    }

    public String getUsernameKey() {
        return usernameKey;
    }

    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }

    public String getRolesKey() {
        return rolesKey;
    }

    public void setRolesKey(String rolesKey) {
        this.rolesKey = rolesKey;
    }

    public String getPermsKey() {
        return permsKey;
    }

    public void setPermsKey(String permsKey) {
        this.permsKey = permsKey;
    }

    @Override
    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    @Override
    public String getSharedSecret() {
        return sharedSecret;
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

}
