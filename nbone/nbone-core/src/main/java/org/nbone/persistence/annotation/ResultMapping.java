package org.nbone.persistence.annotation;

import java.lang.annotation.*;

/**
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2019/6/28
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResultMapping {

    String id() default "";
}
