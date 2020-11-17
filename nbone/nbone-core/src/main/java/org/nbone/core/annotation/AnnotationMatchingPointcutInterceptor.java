package org.nbone.core.annotation;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.core.Ordered;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * 自定义切入点连接 和 AOP 拦截器
 * <pre>
 * {@code
 *
 *   @Component
 *   public class LogInterceptor extends AnnotationMatchingPointcutInterceptor {
 *
 *     public LogInterceptor() {
 *          // 当classAnnotationType 和 methodAnnotationType 注解都存在时 是 切的关系 (and)
 *          super(FeignClient.class, MonitorMetric.class,true);
 *     }
 *
 *     @Override
 *     public Object invoke(MethodInvocation invocation) throws Throwable {
 *
 *          System.out.println("==============LogInterceptor=================");
 *          return invocation.proceed();
 *     }
 *
 *    }
 * </pre>
 *
 * @author thinking
 * @version 1.0
 * @see org.springframework.aop.support.DefaultPointcutAdvisor
 * @see org.springframework.aop.support.annotation.AnnotationMatchingPointcut
 * @see org.springframework.aop.interceptor.ExposeInvocationInterceptor
 * @see org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor
 * @since 2020-09-11
 */
@SuppressWarnings("unused")
public abstract class AnnotationMatchingPointcutInterceptor extends AnnotationMatchingPointcut
        implements MethodInterceptor, PointcutAdvisor, Ordered, Serializable {

    // Default this
    private Advice advice = this;
    private int order = Integer.MAX_VALUE;


    public AnnotationMatchingPointcutInterceptor(Class<? extends Annotation> annotationType) {
        super(annotationType);
    }

    /**
     * @param annotationType 注解类型
     * @param checkInherited 是否向上遍历父类 和接口
     */
    public AnnotationMatchingPointcutInterceptor(Class<? extends Annotation> annotationType, boolean checkInherited) {
        super(annotationType, checkInherited);
    }


    public AnnotationMatchingPointcutInterceptor(Class<? extends Annotation> classAnnotationType,
                                                 Class<? extends Annotation> methodAnnotationType) {
        super(classAnnotationType, methodAnnotationType);
    }


    /**
     * classAnnotationType 和 methodAnnotationType 注解 至少存在一个 <br>
     * 当classAnnotationType 和 methodAnnotationType 注解都存在时 判断逻辑是且的关系 (and)
     *
     * @param classAnnotationType
     * @param methodAnnotationType
     * @param checkInherited
     */
    public AnnotationMatchingPointcutInterceptor(
            Class<? extends Annotation> classAnnotationType,
            Class<? extends Annotation> methodAnnotationType, boolean checkInherited) {
        super(classAnnotationType, methodAnnotationType, checkInherited);

    }


    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public boolean isPerInstance() {
        return true;
    }

    @Override
    public Pointcut getPointcut() {
        return this;
    }
}
