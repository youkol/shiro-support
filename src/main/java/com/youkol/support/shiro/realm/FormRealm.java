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
package com.youkol.support.shiro.realm;

import java.util.Set;

import com.youkol.support.shiro.io.CustomByteSource;
import com.youkol.support.shiro.service.UserAccount;
import com.youkol.support.shiro.service.UserService;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @author jackiea
 */
public class FormRealm extends AuthorizingRealm {

    private UserService userService;

    public FormRealm() {
        super();
    }

    public FormRealm(UserService userService) {
        super();
        this.userService = userService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof UsernamePasswordToken;
    }

    // multi realm situation.
    @SuppressWarnings("unchecked")
    protected Object getPrincipal(PrincipalCollection principals) {
        return principals.fromRealm(getName())
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) getPrincipal(principals);
        if (username == null) {
            return null;
        }

        UserAccount account = userService.findByUsername(username);
        Set<String> roles = account.getRoles();
        Set<String> perms = account.getPermissions();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(perms);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        UserAccount account = userService.findByUsername(upToken.getUsername());
        if (account == null) {
            throw new UnknownAccountException("The account doesn't exist.");
        }
        if (!account.isEnabled()) {
            throw new DisabledAccountException("The account has been disabled.");
        }
        if (!account.isAccountNonLocked()) {
            throw new LockedAccountException("The account has been locked.");
        }
        if (!account.isCredentialsCorrect()) {
            throw new IncorrectCredentialsException("The account credentials is not correct.");
        }
        if (!account.isCredentialsNonExpired()) {
            throw new ExpiredCredentialsException("The account credentials is expired.");
        }

        String username = account.getUsername();
        String password = account.getPassword();
        
        String passwordSalt = this.getPasswordSalt(account);
        ByteSource credentialsSalt = new CustomByteSource(passwordSalt);

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                username, password, credentialsSalt, getName());
        return authenticationInfo;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    protected String getPasswordSalt(UserAccount account) {
        StringBuilder salt = new StringBuilder();
        if (account.getUsername() != null) {
            salt.append(account.getUsername());
        }
        if (account.getSalt() != null) {
            salt.append(account.getSalt());
        }

        return salt.toString();
    }

}
