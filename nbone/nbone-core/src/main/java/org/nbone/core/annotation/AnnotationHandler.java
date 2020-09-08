package org.nbone.core.annotation;

import java.lang.annotation.Annotation;

/**
 * @author thinking
 * @version 1.0
 * @since 2018-12-26
 * @see org.springframework.aop.support.annotation.AnnotationMethodMatcher
 * // @see org.apache.shiro.aop.AnnotationHandler
 */
public abstract class AnnotationHandler {

    /**
     * The type of annotation this handler will process.
     */
    protected Class<? extends Annotation> annotationClass;

    /**
     * Constructs an <code>AnnotationHandler</code> who processes annotations of the
     * specified type.  Immediately calls {@link #setAnnotationClass(Class)}.
     *
     * @param annotationClass the type of annotation this handler will process.
     */
    public AnnotationHandler(Class<? extends Annotation> annotationClass) {
        setAnnotationClass(annotationClass);
    }


    /**
     * Sets the type of annotation this handler will inspect and process.
     *
     * @param annotationClass the type of annotation this handler will process.
     * @throws IllegalArgumentException if the argument is <code>null</code>.
     */
    protected void setAnnotationClass(Class<? extends Annotation> annotationClass)
            throws IllegalArgumentException {
        if (annotationClass == null) {
            String msg = "annotationClass argument cannot be null";
            throw new IllegalArgumentException(msg);
        }
        this.annotationClass = annotationClass;
    }

    /**
     * Returns the type of annotation this handler inspects and processes.
     *
     * @return the type of annotation this handler inspects and processes.
     */
    public Class<? extends Annotation> getAnnotationClass() {
        return this.annotationClass;
    }


}
