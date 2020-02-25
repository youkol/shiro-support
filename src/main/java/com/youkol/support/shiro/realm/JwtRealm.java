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

import com.youkol.support.shiro.authc.IncorrectTokenException;
import com.youkol.support.shiro.authc.JwtAuthenticationToken;
import com.youkol.support.shiro.service.JwtUserService;
import com.youkol.support.shiro.service.UserAccount;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import lombok.Getter;
import lombok.Setter;

/**
 * @deprecated
 * 
 * @author jackiea
 */
@Deprecated
public class JwtRealm extends AuthorizingRealm {

    @Getter
    @Setter
    private JwtUserService jwtUserService;

    public JwtRealm(JwtUserService userService) {
        super();
        this.jwtUserService = userService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof JwtAuthenticationToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)getAvailablePrincipal(principals);
        Set<String> roles = jwtUserService.getRoles(username);
        Set<String> perms = jwtUserService.getPermissions(username, roles);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(perms);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) token;

        UserAccount account = jwtUserService.getUserAccount(jwtToken.getUsername());
        if (account == null) {
            return null;
        }

        String upToken = jwtToken.getToken();
        String username = account.getUsername();
        String password = account.getPassword();
        if (!jwtUserService.validateToken(upToken, username, password)) {
            throw new IncorrectTokenException("Token incorrect.");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                username, upToken, getName());
        return authenticationInfo;
    }

}
