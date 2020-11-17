package org.nbone.core.annotation;

import org.aopalliance.aop.Advice;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 自定义AOP 拦截器，使用切入点和 通知 解耦的方式
 * <pre>
 * {@code
 *
 *     //class level pointcut
 *     @Bean
 *     public PointcutAdvisor pointcutAdvisor() {
 *         return new DefaultPointcutAdvisor(new AnnotationMatchingPointcut(Demo.class,true),new DemoInterceptor());
 *     }
 *
 *     //class level pointcut
 *     @Bean
 *     public PointcutAdvisor PointcutAdvisor() {
 *         return new DefaultPointcutAdvisor(new AnnotationMatchingPointcut(null, MonitorMetric.class),
 *                 new DemoInterceptor());
 *     }
 *
 *     //method level pointcut
 *     @Bean
 *     public PointcutAdvisor pointcutAdvisor() {
 *      return new AnnotationMethodMatcherPointcutAdvisor(MonitorMetric.class,new DemoInterceptor());
 *     }
 * }
 * </pre>
 *
 * @author thinking
 * @version 1.0
 * @see org.springframework.aop.support.DefaultPointcutAdvisor
 * @see org.springframework.aop.support.annotation.AnnotationMatchingPointcut
 * see  org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
 * @since 2018-12-27
 */
@SuppressWarnings("unused")
public class AnnotationMethodMatcherPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {

    /**
     * Annotation Method Matcher
     */
    private AnnotationMethodMatcher annotationMethodMatcher;
    /**
     * Annotation Method Matcher
     */
    private final Class<? extends Annotation> annotationType;
    /**
     * The 'method' parameter could be from an interface that doesn't have the annotation.
     * Check to see if the implementation has it.
     * class level Annotation check
     */
    private boolean checkClass;

    public AnnotationMethodMatcherPointcutAdvisor(Class<? extends Annotation> annotationType, boolean checkClass) {
        this.annotationMethodMatcher = new AnnotationMethodMatcher(annotationType);
        this.annotationType = annotationType;
        this.checkClass = checkClass;
    }

    public AnnotationMethodMatcherPointcutAdvisor(Class<? extends Annotation> annotationType, boolean checkClass,
                                                  Advice advice) {
        super(advice);
        this.annotationMethodMatcher = new AnnotationMethodMatcher(annotationType);
        this.annotationType = annotationType;
        this.checkClass = checkClass;
    }

    public AnnotationMethodMatcherPointcutAdvisor(Class<? extends Annotation> annotationType,
                                                  Advice advice) {
        super(advice);
        this.annotationMethodMatcher = new AnnotationMethodMatcher(annotationType);
        this.annotationType = annotationType;
        this.checkClass = false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (annotationMethodMatcher.matches(method, targetClass)) {
            return true;
        }
        // See if the class has the same annotation
        // targetClass.isAnnotationPresent(annotationType)
        //AnnotationUtils.findAnnotation(targetClass, annotationType)[findAnnotation 父类递归查询和注解向上递归查询 都会向上递归]
        return checkClass ? AnnotationUtils.findAnnotation(targetClass, annotationType) != null : false;
    }
}
