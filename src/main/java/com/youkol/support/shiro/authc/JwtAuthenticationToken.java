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
