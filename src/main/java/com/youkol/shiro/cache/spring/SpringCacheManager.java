package com.youkol.shiro.cache.spring;

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