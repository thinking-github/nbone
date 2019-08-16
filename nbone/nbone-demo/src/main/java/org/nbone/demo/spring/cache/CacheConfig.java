package org.nbone.demo.spring.cache;

import com.google.common.cache.CacheBuilder;
import org.nbone.spring.boot.autoconfigure.cache.CaffeineMultipleConfiguration;
import org.nbone.spring.boot.autoconfigure.cache.GuavaMultipleConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManagerX;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-08-06
 */
@Configuration
@EnableCaching
@Import({GuavaMultipleConfiguration.class, CaffeineMultipleConfiguration.class})
public class CacheConfig {

    public final static String REDIS_CACHE_MANAGER = "redisCacheManager";

    @Resource
    private GuavaMultipleConfiguration multipleConfiguration;

    @Resource
    private CaffeineMultipleConfiguration caffeineConfiguration;


    @Primary
    @Bean
    public CacheManager cacheManager() {
        GuavaCacheManagerX cacheManager = new GuavaCacheManagerX();
        cacheManager.setCacheBuilder(
                CacheBuilder.newBuilder().
                        expireAfterWrite(10, TimeUnit.MINUTES).
                        maximumSize(1000));

        cacheManager.addCacheBuilder("classify",
                CacheBuilder.newBuilder()
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .maximumSize(1000));

        multipleConfiguration.processMultiple(cacheManager);

        return cacheManager;
    }

    @Bean
    public CacheManager caffeineCacheManager() {

        return caffeineConfiguration.createCacheManager();
    }


    @Resource
    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {

        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setUsePrefix(true);
        redisCacheManager.setDefaultExpiration(600L);

        Map<String, Long> expires = new HashMap<>();
        expires.put("pub.imlichao.CacheTest.cacheFunction", 100L);
        redisCacheManager.setExpires(expires);

        return redisCacheManager;
    }


}
