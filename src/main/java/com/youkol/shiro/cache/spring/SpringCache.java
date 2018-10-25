package com.youkol.shiro.cache.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.util.CollectionUtils;

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
            if (cache.getNativeCache() instanceof net.sf.ehcache.Ehcache) {
                net.sf.ehcache.Ehcache ehcache = (net.sf.ehcache.Ehcache) cache.getNativeCache();
                return ehcache.getSize();
            }

            throw new UnsupportedOperationException("invoke spring cache size method not supported");
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Set<K> keys() {
        try {
            if (cache.getNativeCache() instanceof net.sf.ehcache.Ehcache) {
                net.sf.ehcache.Ehcache ehcache = (net.sf.ehcache.Ehcache) cache.getNativeCache();
                List<K> keys = ehcache.getKeys();
                if (!CollectionUtils.isEmpty(keys)) {
                    return Collections.unmodifiableSet(new LinkedHashSet<K>(keys));
                } else {
                    return Collections.emptySet();
                }
            }

            throw new UnsupportedOperationException("invoke spring cache keys method not supported");
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Collection<V> values() {
        try {
            if (cache.getNativeCache() instanceof net.sf.ehcache.Ehcache) {
                net.sf.ehcache.Ehcache ehcache = (net.sf.ehcache.Ehcache) cache.getNativeCache();
                List<K> keys = ehcache.getKeys();
                if (!CollectionUtils.isEmpty(keys)) {
                    List<V> values = new ArrayList<V>(keys.size());
                    for (K key : keys) {
                        V value = get(key);
                        if (value != null) {
                            values.add(value);
                        }
                    }
                    return Collections.unmodifiableList(values);
                } else {
                    return Collections.emptyList();
                }
            }

            throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

}