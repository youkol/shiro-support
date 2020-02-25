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
package com.youkol.support.shiro.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.youkol.support.shiro.authc.JwtAuthenticationToken;
import com.youkol.support.shiro.web.util.WebTools;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @deprecated
 * 不再推荐使用，shiro1.5之后，增加了HTTP Bearer protocol相关实现，
 * 具体使用请参见https://github.com/apache/shiro
 *
 * @see org.apache.shiro.web.filter.authc.BearerHttpAuthenticationFilter
 * @see org.apache.shiro.authc.BearerToken
 *
 * @author jackiea
 */
@Slf4j
@Setter
@Getter
@Deprecated
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
        return WebTools.getRemoteAddr(WebUtils.toHttp(request));
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
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
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
