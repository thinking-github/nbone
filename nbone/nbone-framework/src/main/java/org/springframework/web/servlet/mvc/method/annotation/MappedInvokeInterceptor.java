package org.springframework.web.servlet.mvc.method.annotation;

import com.google.common.collect.Sets;
import org.springframework.http.HttpMethod;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 *  Controller Method  方法拦截器代理和映射器
 * @author thinking
 * @version 1.0
 * @since 2019/7/12
 * @see org.springframework.web.servlet.handler.MappedInterceptor
 */
public class MappedInvokeInterceptor implements InvokeInterceptor{

    private final String[] includePatterns;

    private final String[] excludePatterns;

    private final InvokeInterceptor interceptor;

    private PathMatcher pathMatcher;

    //new add thinking
    private Set<RequestMethod> methods;


    /**
     * Create a new MappedInterceptor instance.
     * @param includePatterns the path patterns to map with a {@code null} value matching to all paths
     * @param interceptor the HandlerInterceptor instance to map to the given patterns
     */
    public MappedInvokeInterceptor(String[] includePatterns, InvokeInterceptor interceptor) {
        this(includePatterns, null, interceptor);
    }

    /**
     * Create a new MappedInterceptor instance.
     * @param includePatterns the path patterns to map with a {@code null} value matching to all paths
     * @param excludePatterns the path patterns to exclude
     * @param interceptor the HandlerInterceptor instance to map to the given patterns
     */
    public MappedInvokeInterceptor(String[] includePatterns, String[] excludePatterns, InvokeInterceptor interceptor) {
        this.includePatterns = includePatterns;
        this.excludePatterns = excludePatterns;
        this.interceptor = interceptor;
    }



    /**
     * Configure a PathMatcher to use with this MappedInterceptor instead of the
     * one passed by default to the {@link #matches(String, org.springframework.util.PathMatcher)}
     * method. This is an advanced property that is only required when using custom
     * PathMatcher implementations that support mapping metadata other than the
     * Ant-style path patterns supported by default.
     */
    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    /**
     * The configured PathMatcher, or {@code null} if none.
     */
    public PathMatcher getPathMatcher() {
        return this.pathMatcher;
    }

    /**
     * The path into the application the interceptor is mapped to.
     */
    public String[] getPathPatterns() {
        return this.includePatterns;
    }

    /**
     * The actual Interceptor reference.
     */
    public InvokeInterceptor getInterceptor() {
        return this.interceptor;
    }

    /**
     * Returns {@code true} if the interceptor applies to the given request path.
     * @param lookupPath the current request path
     * @param pathMatcher a path matcher for path pattern matching
     */
    public boolean matches(String lookupPath, PathMatcher pathMatcher) {
        PathMatcher pathMatcherToUse = (this.pathMatcher != null) ? this.pathMatcher : pathMatcher;
        if (this.excludePatterns != null) {
            for (String pattern : this.excludePatterns) {
                if (pathMatcherToUse.match(pattern, lookupPath)) {
                    return false;
                }
            }
        }
        if (ObjectUtils.isEmpty(this.includePatterns)) {
            return true;
        }
        else {
            for (String pattern : this.includePatterns) {
                if (pathMatcherToUse.match(pattern, lookupPath)) {
                    return true;
                }
            }
            return false;
        }
    }


    /**
     *  请求路径匹配 and 请求方法匹配
     * @param lookupPath
     * @param pathMatcher
     * @param request
     * @return
     */
    public boolean matches(String lookupPath, PathMatcher pathMatcher, HttpServletRequest request) {
        return  matches(lookupPath,pathMatcher) && matchRequestMethod(request.getMethod());
    }

    /**
     *   当没有设置允许的方法时methods，全部放行, 如果设置了 method 只允许匹配的放行
     * @param httpMethodValue
     * @return
     */
    private boolean matchRequestMethod(String httpMethodValue) {
        if(ObjectUtils.isEmpty(getMethods())){
            return true;
        }
        HttpMethod httpMethod = HttpMethod.resolve(httpMethodValue);
        if (httpMethod != null) {
            for (RequestMethod method : getMethods()) {
                if (httpMethod.matches(method.name())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns all {@link RequestMethod}s contained in this condition.
     */
    public Set<RequestMethod> getMethods() {
        return this.methods;
    }
    public MappedInvokeInterceptor methods(RequestMethod... requestMethods) {
        methods =  (requestMethods != null ? Sets.newHashSet(requestMethods) : null);
        return this;
    }

    @Override
    public void preInvoke(NativeWebRequest request, Object... args) throws Exception {
        this.interceptor.preInvoke(request,args);
    }




}
