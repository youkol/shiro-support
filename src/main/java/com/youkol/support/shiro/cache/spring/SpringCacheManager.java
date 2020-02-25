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
package com.youkol.support.shiro.cache.spring;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用Spring管理cache
 * 
 * @author jackiea
 */
public class SpringCacheManager implements CacheManager {

    private static final Logger logger = LoggerFactory.getLogger(SpringCacheManager.class);

    protected org.springframework.cache.CacheManager cacheManager;

    public SpringCacheManager() {
    }

    public org.springframework.cache.CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(org.springframework.cache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public <K,V> Cache<K,V> getCache(String name) throws CacheException {
        if (logger.isTraceEnabled()) {
            logger.trace("Acquiring SpringCache instance named [" + name + "]");
        }

        try {
            org.springframework.cache.Cache springCache = this.cacheManager.getCache(name);
        
            if (logger.isInfoEnabled()) {
                logger.info("Using Spring Cache named [" + springCache.getName() + "]");
            }

            return new SpringCache<K, V>(springCache);
        } catch (Exception ex) {
            throw new CacheException(ex);
        }
    }

}
