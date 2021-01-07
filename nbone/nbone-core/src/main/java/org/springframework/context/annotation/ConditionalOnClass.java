package org.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * {@link Conditional} that only matches when the specified classes are on the classpath.
 *
 * @author thinking
 * @version 1.0
 * @see ProfileCondition
 * @since 2019-01-04
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnClassCondition.class)
public @interface ConditionalOnClass {

    /**
     * The classes that must be present. Since this annotation is parsed by loading class
     * bytecode, it is safe to specify classes here that may ultimately not be on the
     * classpath, only if this annotation is directly on the affected component and
     * <b>not</b> if this annotation is used as a composed, meta-annotation. In order to
     * use this annotation as a meta-annotation, only use the {@link #name} attribute.
     *
     * @return the classes that must be present
     */
    Class<?>[] value() default {};

    /**
     * The classes names that must be present.
     *
     * @return the class names that must be present.
     */
    String[] name() default {};

}
