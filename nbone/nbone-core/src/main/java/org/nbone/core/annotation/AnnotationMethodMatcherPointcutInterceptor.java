package org.nbone.core.annotation;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.annotation.Annotation;

/**
 * 方法AOP自定义拦截器，使用切入点和 通知 解耦的方式
 * <pre>
 * {@code
 * @Component
 * public class MonitorMetricInterceptor extends AnnotationMethodMatcherPointcutInterceptor {
 *
 *     public MonitorMetricInterceptor() {
 *         super(MonitorMetric.class, true);
 *     }
 *
 *     @Override
 *     public Object invoke(MethodInvocation invocation) throws Throwable {
 *         System.out.println("==============MonitorMetricInterceptor=================");
 *         return invocation.proceed();
 *     }
 *
 *     @PostConstruct
 *     public void init() {
 *         System.out.println("MonitorMetricInterceptor init");
 *     }
 * }
 *
 * }
 * </pre>
 *
 * @author thinking
 * @version 1.0
 * @see org.springframework.aop.support.DefaultPointcutAdvisor
 * @see org.springframework.aop.support.annotation.AnnotationMatchingPointcut
 * @see org.springframework.aop.interceptor.ExposeInvocationInterceptor
 * see  org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
 * @since 2018-12-27
 */
@SuppressWarnings("unused")
public abstract class AnnotationMethodMatcherPointcutInterceptor extends AnnotationMethodMatcherPointcutAdvisor
        implements MethodInterceptor {

    public AnnotationMethodMatcherPointcutInterceptor(Class<? extends Annotation> annotationType, boolean checkClass) {
        super(annotationType, checkClass);
        setAdvice(this);
    }


    @Override
    public Advice getAdvice() {
        return this;
    }
}
