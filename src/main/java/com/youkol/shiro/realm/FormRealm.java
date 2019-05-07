package com.youkol.shiro.realm;

import java.util.Set;

import com.youkol.shiro.service.UserAccount;
import com.youkol.shiro.service.UserService;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jackiea
 */
public class FormRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)getAvailablePrincipal(principals);
        Set<String> roles = userService.getRoles(username);
        Set<String> perms = userService.getPermissions(username, roles);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(perms);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        UserAccount account = userService.getUserAccount(upToken.getUsername());
        if (account == null) {
            return null;
        }

        String username = account.getUsername();
        String password = account.getPassword();
        String salt = userService.getSalt(account);
        ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
        
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                username, password, credentialsSalt, getName());
        return authenticationInfo;
    }

}
