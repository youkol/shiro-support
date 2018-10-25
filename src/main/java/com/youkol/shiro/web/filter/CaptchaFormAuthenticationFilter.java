package com.youkol.shiro.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.youkol.shiro.authc.CaptchaUsernamePasswordToken;
import com.youkol.shiro.authc.IncorrectCaptchaException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author jackiea
 */
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(FormAuthenticationFilter.class);

    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    public static final String CAPTCHA_SESSION_KEY = "YOUKOL_CAPTCHA_SESSION_KEY";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;

    private String captchaSessionKey = CAPTCHA_SESSION_KEY;

    public String getCaptchaParam() {
        return captchaParam;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }

    public String getCaptchaSessionKey() {
        return captchaSessionKey;
    }

    public void setCaptchaSessionKey(String captchaSessionKey) {
        this.captchaSessionKey = captchaSessionKey;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    @Override
    protected CaptchaUsernamePasswordToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        
        return new CaptchaUsernamePasswordToken(username, password, rememberMe, host, captcha);
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        CaptchaUsernamePasswordToken token = createToken(request, response);
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        
        try {
            doCaptchaValidate((HttpServletRequest)request, token);

            Subject subject = getSubject(request, response);
            subject.login(token);

            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException ex) {
            return onLoginFailure(token, ex, request, response);
        }
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token,
                                 Subject subject,
                                 ServletRequest request,
                                 ServletResponse response)
                          throws Exception {
        if (com.youkol.shiro.web.util.WebUtils.isAjaxRequest(WebUtils.toHttp(request))) { // ajax request
            return false;
        }

        return super.onLoginSuccess(token, subject, request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                 AuthenticationException e,
                                 ServletRequest request,
                                 ServletResponse response) {
        return super.onLoginFailure(token, e, request, response);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else if (com.youkol.shiro.web.util.WebUtils.isAjaxRequest(WebUtils.toHttp(request))) { // ajax request
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication. Set the " +
                        "Authentication url [" + getLoginUrl() + "] in response.");
            }
            HttpServletResponse httpResponse = WebUtils.toHttp(response);

            httpResponse.setHeader("session-status", "timeout");
            httpResponse.setHeader("redirect-url", (WebUtils.toHttp(request)).getContextPath() + getLoginUrl());

            if (log.isDebugEnabled()) {
                log.debug("session-timeout...");
            }

            return false;
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }

            saveRequestAndRedirectToLogin(request, response);
            return false;
        }
    }

    @Override
    protected String getHost(ServletRequest request) {
        return com.youkol.shiro.web.util.WebUtils.getRemoteAddr(WebUtils.toHttp(request));
    }

    /**
     * 验证码校验
     * 
     * @param request 请求
     * @param token 身份验证令牌
     */
    protected void doCaptchaValidate(HttpServletRequest request, CaptchaUsernamePasswordToken token) {
        String captcha = (String)request.getSession().getAttribute(this.getCaptchaSessionKey());
        if (captcha != null && !captcha.equalsIgnoreCase(token.getCaptcha())) {
            throw new IncorrectCaptchaException("Invalid captcha code.");
        }
    }
}