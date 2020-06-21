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

import com.youkol.support.shiro.cache.spring.SpringCacheManager;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(CacheAutoConfiguration.class)
@ConditionalOnProperty(name = "youkol.shiro.spring-cache.enabled", matchIfMissing = true)
public class ShiroSpringCacheAutoConfiguration {
    
    /**
     * Use Spring CacheManager for Shiro CacheManager
     *
     * @param cacheManager spring cacheManager
     * @return A wapper SpringCacheManager for shiro-cache
     */
    @Bean(name = "springCacheManager")
    @ConditionalOnMissingBean
    @ConditionalOnBean(CacheManager.class)
    public SpringCacheManager springCacheManager(CacheManager cacheManager) {
        SpringCacheManager springCacheManager = new SpringCacheManager();
        springCacheManager.setCacheManager(cacheManager);
        return springCacheManager;
    }
}
