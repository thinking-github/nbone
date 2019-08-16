package org.springframework.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持多个cache 个性化配置
 *
 * @author thinking
 * @version 1.0
 * @since 2019-08-06
 */
public class GuavaCacheManagerX extends GuavaCacheManager {

    private Map<String, CacheBuilder<Object, Object>> cacheBuilderMap;
    private Map<String, CacheLoader<Object, Object>> cacheLoaderMap;

    @Override
    protected Cache<Object, Object> createNativeGuavaCache(String name) {
        if (!ObjectUtils.isEmpty(cacheBuilderMap) && cacheBuilderMap.containsKey(name)) {
            CacheBuilder<Object, Object> cacheBuilder = cacheBuilderMap.get(name);

            CacheLoader<Object, Object> cacheLoader = cacheLoaderMap.get(name);
            if (cacheLoader != null) {
                return cacheBuilder.build(cacheLoader);
            }

            return cacheBuilder.build();
        }

        return super.createNativeGuavaCache(name);
    }

    public GuavaCacheManagerX addCacheBuilder(String name, CacheBuilder cacheBuilder, CacheLoader<Object, Object> cacheLoader) {
        if (cacheBuilderMap == null) {
            cacheBuilderMap = new HashMap<>();
        }
        if (cacheLoaderMap == null) {
            cacheLoaderMap = new HashMap<>();
        }

        cacheBuilderMap.put(name, cacheBuilder);
        if (cacheLoader != null) {
            cacheLoaderMap.put(name, cacheLoader);
        }
        return this;
    }

    public GuavaCacheManagerX addCacheBuilder(String name, CacheBuilder cacheBuilder) {
        return addCacheBuilder(name, cacheBuilder, null);
    }


}
