package org.nbone.framework.spring.web.mvc;

import org.nbone.framework.spring.web.filter.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2018/6/22
 */
public class AccessLogHandlerInterceptor implements HandlerInterceptor {

    protected Logger logger = LoggerFactory.getLogger(AccessLogHandlerInterceptor.class);
    //private static ThreadLocal<Long> time = new ThreadLocal<>();
    private boolean outHeader = false;

    private static final String METHOD_START_TS = "REQUEST_METHOD_START_TS";
    private static final String METHOD_END_TS = "REQUEST_METHOD_END_TS";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //time.set(System.currentTimeMillis());
        request.setAttribute(METHOD_START_TS, System.currentTimeMillis());
        String requestId = (String) request.getAttribute(RequestIdFilter.REQUEST_ID);
        //记录请求的内容
        String message = String.format("Request URL: %s %s %s ContentType: %s Content-Length: %s User-Agent: %s requestId: %s",
                request.getMethod(), getUri(request), request.getProtocol(), request.getContentType(),
                request.getContentLength(), request.getHeader("User-Agent"),requestId);
        logger.info(message);

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            logger.info("targetMethod: " + handlerMethod.getMethod());
        }


        //logger.info("Class Method: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

        /*if(request.getCookies() != null){
            logger.info("Cookies: " + request.getCookies().toString());
        }
*/

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        //System.out.println(modelAndView);


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception e) throws Exception {
        long end = System.currentTimeMillis();
        request.setAttribute(METHOD_END_TS, end);
        Long start = (Long) request.getAttribute(METHOD_START_TS);
        long xx = end - start;
        String requestId = (String) request.getAttribute(RequestIdFilter.REQUEST_ID);

        logger.info(request.getRequestURI() + " status:" + response.getStatus() + " 耗时 : " + (xx) + "ms requestId: "+requestId);

    }

    private String getUri(HttpServletRequest request) {
        String path = request.getRequestURL().toString();
        if (request.getQueryString() != null) {
            path = path +"?"+ request.getQueryString();
        }
        return path;
    }


}
