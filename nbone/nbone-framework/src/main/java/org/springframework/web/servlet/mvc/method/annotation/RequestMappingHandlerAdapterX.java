package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

/**
 *  RequestMappingHandlerAdapter 扩展, <br/>
 *  使用方式如下：
 *  <li> WebConfig extend  {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport}
 *       Override method createRequestMappingHandlerAdapter()
 *  <li> spring boot WebConfig extend  {@link org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter}
 *       or  implements WebMvcRegistrations  Override method getRequestMappingHandlerAdapter
 * @author thinking
 * @version 1.0
 * @since 2019/1/25
 * @see  org.springframework.web.method.support.HandlerMethodArgumentResolverComposite  入参组合代理处理器
 * @see  org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite 出参组合代理处理器
 * @see  org.springframework.web.servlet.mvc.WebContentInterceptor                       Web缓存拦截器
 */
@SuppressWarnings("unused")
public class RequestMappingHandlerAdapterX extends RequestMappingHandlerAdapter {

    /**
     * 增加调用拦截器执行链
     */
    private InvokeExecutionChain invokeExecutionChain;
    /**
     * 防止 ServletInputStream 只能读取一次,故缓存下来供 Controller method 处理完后继续使用，例如详细日志信息
     */
    private boolean bodyCache;

    @Override
    protected ServletInvocableHandlerMethodX createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        return new ServletInvocableHandlerMethodX(handlerMethod, invokeExecutionChain);
    }


    /**
     * 添加 Controller Method  拦截器
     *
     * @param invokeInterceptor
     * @return
     */
    public RequestMappingHandlerAdapterX addInvokeInterceptor(InvokeInterceptor invokeInterceptor) {
        return addInvokeInterceptor(invokeInterceptor, null);
    }

    public RequestMappingHandlerAdapterX addInvokeInterceptors(InvokeInterceptor... invokeInterceptor) {
        if (invokeExecutionChain == null) {
            invokeExecutionChain = new InvokeExecutionChain(null);
        }
        invokeExecutionChain.addInterceptors(invokeInterceptor);
        return this;
    }


    public RequestMappingHandlerAdapterX addInvokeInterceptor(InvokeInterceptor invokeInterceptor,String ...patterns) {
        return addInvokeInterceptor(invokeInterceptor,patterns,null);
    }


    public RequestMappingHandlerAdapterX addInvokeInterceptor(InvokeInterceptor invokeInterceptor, String patterns, RequestMethod... requestMethods) {
        if(ObjectUtils.isEmpty(patterns)){
            return addInvokeInterceptor(invokeInterceptor);
        }
        return addInvokeInterceptor(invokeInterceptor,new String[]{patterns},requestMethods);
    }
    /**
     * 添加拦截器同时设置 拦截映射
     * @param invokeInterceptor 拦截器
     * @param patterns  匹配映射路径 参数可为空， 当为为空时全局拦截
     * @param requestMethods http 请求方法  {@link RequestMethod} 可为空
     * @return
     */
    public RequestMappingHandlerAdapterX addInvokeInterceptor(InvokeInterceptor invokeInterceptor, String[] patterns, RequestMethod... requestMethods) {
        if (invokeExecutionChain == null) {
            invokeExecutionChain = new InvokeExecutionChain(null);
        }
        if(ObjectUtils.isEmpty(patterns)){
            invokeExecutionChain.addInterceptor(invokeInterceptor);
        }else {
            invokeExecutionChain.addInterceptor(new MappedInvokeInterceptor(patterns,invokeInterceptor).methods(requestMethods));
        }
        return this;
    }


    public boolean isBodyCache() {
        return bodyCache;
    }

    public void setBodyCache(boolean bodyCache) {
        this.bodyCache = bodyCache;
    }
}
