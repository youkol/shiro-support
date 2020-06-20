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
package com.youkol.support.shiro.spring.autoconfigure;

import java.util.stream.Collectors;

import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnProperty(name = "shiro.web.enabled", matchIfMissing = true)
public class ShiroSupportAutoConfiguration {

    /**
     * Add AuthenticationListener implementation to Authenticator.
     *
     * In the ShiroWebAutoConfiguration [shiro-spring-boot-web-starter(version:1.5.0)],
     * It don't add AuthenticationListener autowired to Authenticator,
     * so add this bean.
     *
     * @param authenticationStrategy autowired authenticationStrategy
     * @param listeners autowired AuthenticationListener
     * @return Authenticator
     */
    @Bean
    @ConditionalOnMissingBean
    protected Authenticator authenticator(AuthenticationStrategy authenticationStrategy, ObjectProvider<AuthenticationListener> listeners) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(authenticationStrategy);
        authenticator.setAuthenticationListeners(listeners.orderedStream().collect(Collectors.toList()));

        return authenticator;
    }
    
}
