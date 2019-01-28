package org.nbone.framework.spring.web.mvc;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019/1/25
 *
 *  @see  org.springframework.web.method.support.HandlerMethodArgumentResolverComposite  入参组合代理处理器
 *  @see  org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite 出参组合代理处理器
 */
public class RequestMappingHandlerAdapterX extends RequestMappingHandlerAdapter {


    /**
     * 防止 ServletInputStream 只能读取一次,故缓存下来供 Controller method 处理完后继续使用，例如详细日志信息
     */
    private boolean bodyCache;

    public boolean isBodyCache() {
        return bodyCache;
    }

    public void setBodyCache(boolean bodyCache) {
        this.bodyCache = bodyCache;
    }


    @Override
    protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

        return  super.invokeHandlerMethod(request,response,handlerMethod);

    }
}
