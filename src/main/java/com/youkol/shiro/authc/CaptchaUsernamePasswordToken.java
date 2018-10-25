package com.youkol.shiro.authc;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 
 * @author jackiea
 */
public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 1L;
    
    private String captcha;
    
    public String getCaptcha() {
        return captcha;
    }
    
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
    
    public CaptchaUsernamePasswordToken(String username, char[] password) { 
        super(username, password);
        this.captcha = null;
    }
    
    public CaptchaUsernamePasswordToken(String username, char[] password, 
        boolean rememberMe, String host, String captcha) { 
        super(username, password, rememberMe, host); 
        this.captcha = captcha;
    }
    
    public CaptchaUsernamePasswordToken(String username, String password) { 
        super(username, password);
        this.captcha = null;
    }
    
    public CaptchaUsernamePasswordToken(String username, String password, 
        boolean rememberMe, String host, String captcha) { 
        super(username, password, rememberMe, host); 
        this.captcha = captcha; 
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" - ");
        sb.append(this.getUsername());
        sb.append(", rememberMe=").append(this.isRememberMe());
        if (captcha != null) {
            sb.append(", captcha=").append(this.getCaptcha());
        }
        if (this.getHost() != null) {
            sb.append(" (").append(this.getHost()).append(")");
        }
        return sb.toString();
    }
}