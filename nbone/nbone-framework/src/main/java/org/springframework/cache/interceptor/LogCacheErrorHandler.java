package org.springframework.cache.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * spring cache 出现异常时记录错误日志，不抛出异常
 *
 * @author thinking
 * @version 1.0
 * @since 2019-10-09
 */
public class LogCacheErrorHandler implements CacheErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogCacheErrorHandler.class);

    private boolean outStackTrace = false;

    public LogCacheErrorHandler(boolean outStackTrace) {
        this.outStackTrace = outStackTrace;
    }

    public LogCacheErrorHandler() {
    }

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        logError("Get", exception, cache, key);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        logError("Put", exception, cache, key);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        logError("Evict", exception, cache, key);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        logError("Clear", exception, cache, null);
    }

    private void logError(String method, RuntimeException exception, Cache cache, Object key) {
        String nameKey = " [" + method + " cacheName:" + cache.getName() + ",key:" + key + "]";
        if (outStackTrace) {
            logger.error(exception.getMessage() + nameKey, exception);
        } else {
            String className = exception.getClass().getName();
            logger.error(className + " : " + exception.getMessage() + nameKey);
        }

    }
}
