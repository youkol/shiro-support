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

import com.nimbusds.jwt.SignedJWT;
import com.youkol.support.shiro.jwt.token.NimbusJwtTokenService;
import com.youkol.support.shiro.jwt.token.JwtTokenService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroJwtTokenServiceConfiguration {

    @Configuration
    @ConditionalOnClass(SignedJWT.class)
    @ConditionalOnMissingBean(JwtTokenService.class)
    static class NimbusTokenServiceConfiguration {

        @Bean
        public JwtTokenService jwtTokenService(ShiroJwtProperties properties) {
            NimbusJwtTokenService jwtTokenService = new NimbusJwtTokenService();

            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            map.from(properties::getSharedSecret).to(jwtTokenService::setSharedSecret);

            map.from(properties::getIssuer).to(jwtTokenService::setIssuer);
            map.from(properties::getUseridKey).to(jwtTokenService::setUserIdKey);
            map.from(properties::getUsernameKey).to(jwtTokenService::setUsernameKey);
            map.from(properties::getRolesKey).to(jwtTokenService::setRolesKey);
            map.from(properties::getPermsKey).to(jwtTokenService::setPermsKey);
            map.from(properties::getExpireTime).as(t -> t.getSeconds() * 1000L).to(jwtTokenService::setExpireTime);

            return jwtTokenService;
        }
    }
}
