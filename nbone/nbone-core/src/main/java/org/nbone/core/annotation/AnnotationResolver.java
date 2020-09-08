package org.nbone.core.annotation;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

/**
 * Defines an AOP-framework-independent way of determining if an Annotation exists on a Method.
 *
 * @author thinking
 * @version 1.0
 * @since 2019-12-26
 */
public interface AnnotationResolver {

    /**
     * Returns an {@link Annotation} instance of the specified type based on the given
     * {@link MethodInvocation MethodInvocation} argument, or {@code null} if no annotation
     * of that type could be found. First checks the invoked method itself and if not found,
     * then the class for the existence of the same annotation.
     *
     * @param mi    the intercepted method to be invoked.
     * @param clazz the annotation class of the annotation to find.
     * @return the method's annotation of the specified type or {@code null} if no annotation of
     * that type could be found.
     */
    Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz);
}
