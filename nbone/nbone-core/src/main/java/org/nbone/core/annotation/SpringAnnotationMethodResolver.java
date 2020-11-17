package org.nbone.core.annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * use spring AnnotationUtils
 *
 * @author thinking
 * @version 1.0
 * @since 2018-12-26
 * @see org.springframework.scheduling.annotation.AnnotationAsyncExecutionInterceptor
 * @see org.springframework.transaction.interceptor.TransactionInterceptor
 * //@see org.apache.shiro.spring.aop.SpringAnnotationResolver
 */
public class SpringAnnotationMethodResolver implements AnnotationMethodResolver {

    /**
     * The 'method' parameter could be from an interface that doesn't have the annotation.
     * Check to see if the implementation has it.
     * class level Annotation check
     */
    private boolean checkClass = true;

    public Annotation getAnnotation(Method m, Class<? extends Annotation> clazz) {
        Annotation a = AnnotationUtils.findAnnotation(m, clazz);
        if (a != null) return a;

        //The MethodInvocation's method object could be a method defined in an interface.
        //However, if the annotation existed in the interface's implementation (and not
        //the interface itself), it won't be on the above method object.  Instead, we need to
        //acquire the method representation from the targetClass and check directly on the
        //implementation itself:

        Class<?> targetClass = m.getDeclaringClass();
        m = ClassUtils.getMostSpecificMethod(m, targetClass);
        a = AnnotationUtils.findAnnotation(m, clazz);

        if (a != null) return a;
        // See if the class has the same annotation
        return checkClass ? AnnotationUtils.findAnnotation(targetClass, clazz) : null;
    }
}
