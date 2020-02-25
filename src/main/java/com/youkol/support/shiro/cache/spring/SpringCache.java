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

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache.ValueWrapper;

/**
 * Shiro中cache的spring-cache实现
 * 
 * @author jackiea
 */
public class SpringCache<K, V> implements Cache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(SpringCache.class);

    protected org.springframework.cache.Cache cache;

    public SpringCache(org.springframework.cache.Cache cache) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        }
        this.cache = cache;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public V get(K key) throws CacheException {
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Getting object from cache [" + cache.getName() + "] for key [" + key + "]");
            }

            if (key == null) {
                return null;
            }
            ValueWrapper value = cache.get(key);
            if (value == null) {
                if (logger.isTraceEnabled()) {
                    logger.trace("ValueWrapper for [" + key + "] is null.");
                }

                return null;
            }
            //noinspection unchecked
            return (V) value.get();

        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V put(K key, V value) throws CacheException {
        if (logger.isTraceEnabled()) {
            logger.trace("Putting object in cache [" + cache.getName() + "] for key [" + key + "]");
        }
        try {
            V previous = get(key);
            cache.put(key, value);
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        if (logger.isTraceEnabled()) {
            logger.trace("Removing object from cache [" + cache.getName() + "] for key [" + key + "]");
        }
        try {
            V previous = get(key);
            cache.evict(key);
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        if (logger.isTraceEnabled()) {
            logger.trace("Clearing all objects from cache [" + cache.getName() + "]");
        }
        try {
            cache.clear();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        try {
            throw new UnsupportedOperationException("invoke spring cache size method not supported");
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Set<K> keys() {
        try {
            throw new UnsupportedOperationException("invoke spring cache keys method not supported");
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

}
