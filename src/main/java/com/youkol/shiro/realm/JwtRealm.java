package com.youkol.shiro.realm;

import java.util.Set;

import com.youkol.shiro.authc.JwtAuthenticationToken;
import com.youkol.shiro.service.JwtUserService;
import com.youkol.shiro.service.UserAccount;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jackiea
 */
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private JwtUserService jwtUserService;

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
        if (jwtUserService.validateToken(upToken, username, password)) {
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                jwtToken.getUsername(), upToken, getName());
            return authenticationInfo;
        }
        
        return null;
    }
    
}
