package org.nbone.core.annotation;

import org.aopalliance.aop.Advice;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author thinking
 * @version 1.0
 * @since 2018-12-27
 */
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
        //AnnotationUtils.findAnnotation(targetClass, annotationType)
        return checkClass ? targetClass.isAnnotationPresent(annotationType) : false;
    }
}
