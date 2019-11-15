package org.springframework.web.servlet.mvc.method.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller Method  方法拦截器执行链
 * @author thinking
 * @version 1.0
 * @see org.springframework.web.servlet.HandlerExecutionChain;
 * @see org.springframework.web.servlet.HandlerInterceptor
 * @since 2019/7/12
 */
public class InvokeExecutionChain {

    private static final Log logger = LogFactory.getLog(InvokeExecutionChain.class);

    private final Object handler;

    private InvokeInterceptor[] interceptors;

    private List<InvokeInterceptor> interceptorList;

    private int interceptorIndex = -1;

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    private PathMatcher pathMatcher = new AntPathMatcher();


    /**
     * Create a new HandlerExecutionChain.
     *
     * @param handler the handler object to execute
     */
    public InvokeExecutionChain(Object handler) {
        this(handler, (InvokeInterceptor[]) null);
    }

    /**
     * Create a new HandlerExecutionChain.
     *
     * @param handler      the handler object to execute
     * @param interceptors the array of interceptors to apply
     *                     (in the given order) before the handler itself executes
     */
    public InvokeExecutionChain(Object handler, InvokeInterceptor... interceptors) {
        if (handler instanceof InvokeExecutionChain) {
            InvokeExecutionChain originalChain = (InvokeExecutionChain) handler;
            this.handler = originalChain.getHandler();
            this.interceptorList = new ArrayList<InvokeInterceptor>();
            CollectionUtils.mergeArrayIntoCollection(originalChain.getInterceptors(), this.interceptorList);
            CollectionUtils.mergeArrayIntoCollection(interceptors, this.interceptorList);
        } else {
            this.handler = handler;
            this.interceptors = interceptors;
        }
    }


    /**
     * Return the handler object to execute.
     *
     * @return the handler object (may be {@code null})
     */
    public Object getHandler() {
        return this.handler;
    }

    public void addInterceptor(InvokeInterceptor interceptor) {
        initInterceptorList().add(interceptor);
    }

    public void addInterceptors(InvokeInterceptor... interceptors) {
        if (!ObjectUtils.isEmpty(interceptors)) {
            CollectionUtils.mergeArrayIntoCollection(interceptors, initInterceptorList());
        }
    }

    private List<InvokeInterceptor> initInterceptorList() {
        if (this.interceptorList == null) {
            this.interceptorList = new ArrayList<InvokeInterceptor>();
            if (this.interceptors != null) {
                // An interceptor array specified through the constructor
                CollectionUtils.mergeArrayIntoCollection(this.interceptors, this.interceptorList);
            }
        }
        this.interceptors = null;
        return this.interceptorList;
    }

    /**
     * Return the array of interceptors to apply (in the given order).
     *
     * @return the array of HandlerInterceptors instances (may be {@code null})
     */
    public InvokeInterceptor[] getInterceptors() {
        if (this.interceptors == null && this.interceptorList != null) {
            this.interceptors = this.interceptorList.toArray(new InvokeInterceptor[this.interceptorList.size()]);
        }
        return this.interceptors;
    }


    /**
     * Apply preHandle methods of registered interceptors.
     *
     * @return {@code true} if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, DispatcherServlet assumes
     * that this interceptor has already dealt with the response itself.
     */
    boolean applyPreHandle(NativeWebRequest webRequest, Object... args) throws Exception {
        InvokeInterceptor[] interceptors = getInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
            for (int i = 0; i < interceptors.length; i++) {
                InvokeInterceptor interceptor = interceptors[i];
                if (interceptor instanceof MappedInvokeInterceptor) {
                    MappedInvokeInterceptor mappedInterceptor = (MappedInvokeInterceptor) interceptor;
                    if (mappedInterceptor.matches(lookupPath, this.pathMatcher,request)) {
                        mappedInterceptor.preInvoke(webRequest,args);
                    }

                } else {
                    interceptor.preInvoke(webRequest, args);
                }
                this.interceptorIndex = i;
            }
        }
        return true;
    }

    /**
     * Apply postHandle methods of registered interceptors.
     */
    void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        InvokeInterceptor[] interceptors = getInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            for (int i = interceptors.length - 1; i >= 0; i--) {
                InvokeInterceptor interceptor = interceptors[i];
                //interceptor.postHandle(request, response, this.handler, mv);
            }
        }
    }

    // invoke finally code block
    void applyAfterFinally(NativeWebRequest webRequest, Object... args) throws Exception {
        InvokeInterceptor[] interceptors = getInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            for (int i = interceptors.length - 1; i >= 0; i--) {
                InvokeInterceptor interceptor = interceptors[i];
                interceptor.afterFinally(webRequest, args);
            }
        }
    }

    /**
     * Delegates to the handler's {@code toString()}.
     */
    @Override
    public String toString() {
        Object handler = getHandler();
        if (handler == null) {
            return "InvokeExecutionChain with no handler";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("InvokeExecutionChain with handler [").append(handler).append("]");
        InvokeInterceptor[] interceptors = getInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            sb.append(" and ").append(interceptors.length).append(" interceptor");
            if (interceptors.length > 1) {
                sb.append("s");
            }
        }
        return sb.toString();
    }
}
