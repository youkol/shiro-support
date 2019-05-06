package com.youkol.shiro.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.youkol.shiro.authc.JwtAuthenticationToken;
import com.youkol.shiro.web.util.WebUtils;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jackiea
 */
@Slf4j
@Setter
@Getter
public class JwtAuthenticationFilter extends BasicHttpAuthenticationFilter {

    private String authcScheme = "BEARER";

    private String authzScheme = "BEARER";

    private boolean ignoreOptionsRequest = true;

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String authorizationHeader = getAuthzHeader(request);
        if (authorizationHeader == null || authorizationHeader.length() == 0) {
            return new JwtAuthenticationToken();
        }

        String[] prinCred = getPrincipalsAndCredentials(authorizationHeader, request);
        String username = prinCred[0];
        String token = prinCred[1];
        String host = getHost(request);

        return new JwtAuthenticationToken(username, token, host);
    }

    @Override
    protected String getHost(ServletRequest request) {
        return WebUtils.getRemoteAddr((HttpServletRequest) request);
    }

    @Override
    protected String[] getPrincipalsAndCredentials(String scheme, String encoded) {
        try {
            DecodedJWT jwt = JWT.decode(encoded);
            String username = jwt.getClaim("username").asString();

            String[] result = {username, encoded};
            return result;
        } catch (JWTDecodeException ex) {
            log.error("Occur an error, when get username from the token.", ex);
            // throw new IncorrectTokenException(ex);
            String[] result = {null, encoded};
            return result;
        }
    }

    /**
     * 添加跨域支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-control-Allow-Origin", httpRequest.getHeader("Origin"));
        httpResponse.setHeader("Access-Control-Allow-Methods", httpRequest.getHeader("Access-Control-Request-Method"));
        httpResponse.setHeader("Access-Control-Allow-Headers", httpRequest.getHeader("Access-Control-Request-Headers"));
        if (this.isIgnoreOptionsRequest() && "OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        return super.preHandle(httpRequest, httpResponse);
    }

}
