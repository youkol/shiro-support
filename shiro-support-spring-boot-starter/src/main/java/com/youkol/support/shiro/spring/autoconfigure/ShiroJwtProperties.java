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
package com.youkol.support.shiro.spring.autoconfigure;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

@ConfigurationProperties(prefix = "youkol.shiro.jwt")
public class ShiroJwtProperties {
    
    private boolean enabled = true;

    private String sharedSecret;

    private String issuer;

    private String useridKey;

    private String usernameKey;

    private String rolesKey;

    private String permsKey;

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSharedSecret() {
        return sharedSecret;
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
     * Default jwt token timeout.
     * UNIT: Milliseconds,
     * Default: One day.
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration expireTime = Duration.ofDays(1);

    public String getUseridKey() {
        return useridKey;
    }

    public void setUseridKey(String useridKey) {
        this.useridKey = useridKey;
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

    public Duration getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Duration expireTime) {
        this.expireTime = expireTime;
    }

}
