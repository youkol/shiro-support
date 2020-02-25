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
package com.youkol.support.shiro.authc;

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
