package org.nbone.framework.spring.web.util;

import org.springframework.http.CacheControl;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.WebContentGenerator;

import javax.servlet.http.HttpServletResponse;

/**
 * http Cache-Control
 *
 * @author thinking
 * @version 1.0
 * @see org.springframework.web.servlet.support.WebContentGenerator
 * @see org.springframework.http.HttpHeaders#CACHE_CONTROL
 * @since 2019-07-23
 */
@SuppressWarnings("unused")
public class CacheControlUtils extends WebContentGenerator {

    private final static CacheControlUtils CACHE_CONTROL = new CacheControlUtils();


    public static void applyCache(HttpServletResponse response, int cacheSeconds) {
        CACHE_CONTROL.applyCacheSeconds(response, cacheSeconds);
    }

    public static void applyCache(HttpServletResponse response, CacheControl cacheControl) {
        CACHE_CONTROL.applyCacheControl(response, cacheControl);
    }


    public static void applyCache(int cacheSeconds) {
        HttpServletResponse response = getResponse();
        if (response == null) {
            return;
        }
        CACHE_CONTROL.applyCacheSeconds(response, cacheSeconds);
    }

    public static void applyCache(CacheControl cacheControl) {
        HttpServletResponse response = getResponse();
        if (response == null) {
            return;
        }
        CACHE_CONTROL.applyCacheControl(response, cacheControl);
    }

    /**
     * spring 4.1 add   HttpServletResponse Response 属性
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletResponse response = servletRequest.getResponse();

        return response;
    }


}
