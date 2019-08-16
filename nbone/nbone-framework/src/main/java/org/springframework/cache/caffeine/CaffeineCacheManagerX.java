package org.springframework.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持多个cache 个性化配置
 *
 * @author thinking
 * @version 1.0
 * @since 2019-08-08
 */
public class CaffeineCacheManagerX extends CaffeineCacheManager {

    private Map<String, Caffeine<Object, Object>> cacheBuilderMap;
    private Map<String, CacheLoader<Object, Object>> cacheLoaderMap;

    @Override
    protected Cache<Object, Object> createNativeCaffeineCache(String name) {
        if (!ObjectUtils.isEmpty(cacheBuilderMap) && cacheBuilderMap.containsKey(name)) {
            Caffeine<Object, Object> cacheBuilder = cacheBuilderMap.get(name);

            CacheLoader<Object, Object> cacheLoader = cacheLoaderMap.get(name);
            if (cacheLoader != null) {
                return cacheBuilder.build(cacheLoader);
            }

            return cacheBuilder.build();
        }

        return super.createNativeCaffeineCache(name);
    }

    public CaffeineCacheManagerX addCacheBuilder(String name, Caffeine caffeine, CacheLoader<Object, Object> cacheLoader) {
        if (cacheBuilderMap == null) {
            cacheBuilderMap = new HashMap<>();
        }
        if (cacheLoaderMap == null) {
            cacheLoaderMap = new HashMap<>();
        }

        cacheBuilderMap.put(name, caffeine);
        if (cacheLoader != null) {
            cacheLoaderMap.put(name, cacheLoader);
        }
        return this;
    }

    public CaffeineCacheManagerX addCacheBuilder(String name, Caffeine cacheBuilder) {
        return addCacheBuilder(name, cacheBuilder, null);
    }


}
