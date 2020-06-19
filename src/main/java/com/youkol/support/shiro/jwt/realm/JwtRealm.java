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
package com.youkol.support.shiro.jwt.realm;

import java.util.Set;

import com.youkol.support.shiro.authc.ExpiredTokenException;
import com.youkol.support.shiro.authc.InvalidTokenException;
import com.youkol.support.shiro.jwt.JwtUserService;
import com.youkol.support.shiro.jwt.token.JwtTokenService;
import com.youkol.support.shiro.jwt.token.exception.ExpiredJwtTokenException;
import com.youkol.support.shiro.jwt.token.exception.IllegalJwtTokenException;
import com.youkol.support.shiro.service.UserAccount;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 *
 * @author jackiea
 */
public class JwtRealm extends AuthorizingRealm {

    private JwtUserService userService;

    private JwtTokenService tokenService;

    public JwtRealm() {
        super();
    }

    public JwtRealm(JwtUserService userService, JwtTokenService tokenService) {
        super();
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof BearerToken;
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
        String token = (String)getPrincipal(principals);
        if (token == null) {
            return null;
        }

        UserAccount account = userService.findByToken(token);
        Set<String> roles = account.getRoles();
        Set<String> perms = account.getPermissions();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(perms);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        BearerToken jwtToken = (BearerToken) token;
        String currentToken = jwtToken.getToken();

        UserAccount account = userService.findByToken(currentToken);
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

        // According to the actual situation, directly verify the legitimacy of token,
        // and then make user information query.
        // Make validate token before query user infomation, it can reduce database query.
        boolean bValid = false;
        try {
            bValid = tokenService.validateToken(currentToken, account);
        } catch (IllegalJwtTokenException ex) {
            throw new InvalidTokenException("The token is illegal.");
        } catch (ExpiredJwtTokenException ex) {
            throw new ExpiredTokenException("The token is expired.", ex);
        }

        if (!bValid) {
            throw new InvalidTokenException("The token is invalidation.");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
            jwtToken.getPrincipal(), jwtToken.getCredentials(), getName());
        return authenticationInfo;
    }

    public JwtUserService getUserService() {
        return userService;
    }

    public void setUserService(JwtUserService userService) {
        this.userService = userService;
    }

    public JwtTokenService getTokenService() {
        return tokenService;
    }

    public void setTokenService(JwtTokenService tokenService) {
        this.tokenService = tokenService;
    }

}
