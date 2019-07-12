package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.NativeWebRequest;

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

    @Override
    public void preInvoke(NativeWebRequest request, Object... args) throws Exception {
        this.interceptor.preInvoke(request,args);
    }




}
