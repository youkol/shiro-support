package com.youkol.shiro.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.youkol.shiro.authc.JwtAuthenticationToken;
import com.youkol.shiro.web.util.WebUtils;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jackiea
 */
@Setter
@Getter
public class JwtAuthenticationFilter extends BasicHttpAuthenticationFilter {

    private String authcScheme = "BEARER";

    private String authzScheme = "BEARER";

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String authorizationHeader = getAuthzHeader(request);
        if (authorizationHeader == null || authorizationHeader.length() == 0) {
            return new JwtAuthenticationToken();
        }

        String[] authTokens = authorizationHeader.split(" ");
        if (authTokens == null || authTokens.length < 2) {
            return new JwtAuthenticationToken();
        }

        String host = getHost(request);

        return createToken(authTokens[1], host);
    }

    @Override
    protected String getHost(ServletRequest request) {
        return WebUtils.getRemoteAddr((HttpServletRequest) request);
    }

    protected JwtAuthenticationToken createToken(String token, String host) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String username = jwt.getClaim("username").asString();

            return new JwtAuthenticationToken(username, token, host);
        } catch (JWTDecodeException ex) {
            throw new AuthenticationException(ex);
        }
    }

}
