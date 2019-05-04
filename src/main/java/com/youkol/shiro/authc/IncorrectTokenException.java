package com.youkol.shiro.authc;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 
 * @author jackiea
 */
public class IncorrectTokenException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public IncorrectTokenException() {
        super();
    }

    public IncorrectTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectTokenException(String message) {
        super(message);
    }

    public IncorrectTokenException(Throwable cause) {
        super(cause);
    }
}
