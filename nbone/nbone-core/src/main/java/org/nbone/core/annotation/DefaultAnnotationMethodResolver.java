package org.nbone.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author thinking
 * @version 1.0
 * @since 2018-12-27
 * //@see org.apache.shiro.aop.DefaultAnnotationResolver
 */
public class DefaultAnnotationMethodResolver implements AnnotationMethodResolver {

    /**
     * The 'method' parameter could be from an interface that doesn't have the annotation.
     * Check to see if the implementation has it.
     * class level Annotation check
     */
    private boolean checkClass = true;


    @Override
    public Annotation getAnnotation(Method method, Class<? extends Annotation> clazz) {
        if (method == null) {
            throw new IllegalArgumentException("method argument cannot be null");
        }
        Annotation annotation = method.getAnnotation(clazz);
        if (annotation == null && checkClass) {
            Class<?> targetClass = method.getDeclaringClass();
            //SHIRO-473 - miThis could be null for static methods, just return null
            annotation = targetClass.getAnnotation(clazz);
        }
        return annotation;
    }
}
