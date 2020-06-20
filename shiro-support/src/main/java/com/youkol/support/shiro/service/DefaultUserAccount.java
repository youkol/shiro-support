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
package com.youkol.support.shiro.service;

import java.util.Set;

/**
 * Default implementation for UserAccount.
 *
 * @author jackiea
 */
public class DefaultUserAccount implements UserAccount {

    private static final long serialVersionUID = 1L;

    Set<String> roles;

    Set<String> permissions;

    private String userId;

    private String username;

    private String password;

    private String salt;

    private String status;

    private boolean credentialsCorrect;

    private boolean credentialsNonExpired;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean enabled;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCredentialsCorrect() {
        return credentialsCorrect;
    }

    public void setCredentialsCorrect(boolean credentialsCorrect) {
        this.credentialsCorrect = credentialsCorrect;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "DefaultUserAccount [accountNonExpired=" + accountNonExpired + ", accountNonLocked=" + accountNonLocked
                + ", credentialsCorrect=" + credentialsCorrect + ", credentialsNonExpired=" + credentialsNonExpired
                + ", enabled=" + enabled + ", password=" + password + ", permissions=" + permissions + ", roles="
                + roles + ", salt=" + salt + ", status=" + status + ", userId=" + userId + ", username=" + username
                + "]";
    }
}
