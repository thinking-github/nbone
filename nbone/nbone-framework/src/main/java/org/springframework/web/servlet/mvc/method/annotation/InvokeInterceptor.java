package org.springframework.web.servlet.mvc.method.annotation;


import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author thinking
 * @version 1.0
 * @since 2019/7/12
 * @see org.springframework.web.servlet.HandlerInterceptor
 */
public interface InvokeInterceptor {

    /**
     *  HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
     * @param args
     * @throws Exception
     *
     */
    void preInvoke(NativeWebRequest request, Object... args) throws Exception;

    /**
     * invoke finally code block
     *
     * @param request
     * @param args
     * @throws Exception
     */
    void afterFinally(NativeWebRequest request, Object[] args) throws Exception;



}
