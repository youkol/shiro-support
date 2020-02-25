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

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youkol.support.shiro.web.util.WebTools;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jackiea
 */
@Getter
@Setter
@Slf4j
public class FormTokenFilter extends FormAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    private boolean ignoreOptionsRequest = true;

    public FormTokenFilter() {
        super();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean loggedIn = false;
        if (isLoginRequest(request, response) && isLoginSubmission(request, response)) {
            loggedIn = executeLogin(request, response);
        }

        return loggedIn;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
            ServletResponse response) throws Exception {
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        return super.onLoginFailure(token, e, request, response);
        // throw new AuthenticationException("", e);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        try {
            UsernamePasswordToken token = objectMapper.readValue(request.getInputStream(), UsernamePasswordToken.class);
            token.setHost(host);
            token.setRememberMe(rememberMe);

            return token;
        } catch (IOException ex) {
            log.error("Json from request parse error.", ex);

        }

        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setHost(host);
        token.setRememberMe(rememberMe);
        return token;
    }

    @Override
    protected String getHost(ServletRequest request) {
        return WebTools.getRemoteAddr(WebUtils.toHttp(request));
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
