package com.youkol.shiro.authc;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 
 * @author jackiea
 */
public class IncorrectCaptchaException extends AuthenticationException {

    private static final long serialVersionUID = 1L;
    
    public IncorrectCaptchaException() {
        super();
    }
    
    public IncorrectCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public IncorrectCaptchaException(String message) {
        super(message);
    }
    
    public IncorrectCaptchaException(Throwable cause) {
        super(cause);
    }
}