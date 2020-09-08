package org.nbone.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Defines an AOP-framework-independent way of determining if an Annotation exists on a Method.
 *
 * @author thinking
 * @version 1.0
 * @since 2018-12-26
 * @see org.springframework.aop.support.annotation.AnnotationMethodMatcher
 */
public interface AnnotationMethodResolver {

    /**
     * Returns an {@link Annotation} instance of the specified type based on the given
     * {@link MethodInvocation MethodInvocation} argument, or {@code null} if no annotation
     * of that type could be found. First checks the invoked method itself and if not found,
     * then the class for the existence of the same annotation.
     *
     * @param method the intercepted method to be invoked.
     * @param clazz  the annotation class of the annotation to find.
     * @return the method's annotation of the specified type or {@code null} if no annotation of
     * that type could be found.
     */
    Annotation getAnnotation(Method method, Class<? extends Annotation> clazz);
}
