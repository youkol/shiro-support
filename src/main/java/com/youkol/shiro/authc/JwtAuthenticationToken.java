package com.youkol.shiro.authc;

import org.apache.shiro.authc.HostAuthenticationToken;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jackiea
 */
@Getter
@Setter
@ToString(callSuper = true)
public class JwtAuthenticationToken implements HostAuthenticationToken {

    private static final long serialVersionUID = 1L;

    private String username;

    private String token;

    private String host;

    public JwtAuthenticationToken() {
    }

    public JwtAuthenticationToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public JwtAuthenticationToken(String username, String token, String host) {
        this.username = username;
        this.token = token;
        this.host = host;
    }

    @Override
    public Object getPrincipal() {
        return getUsername();
    }

    @Override
    public Object getCredentials() {
        return getToken();
    }
    
}
