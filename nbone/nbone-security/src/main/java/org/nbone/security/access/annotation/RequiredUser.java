package org.nbone.security.access.annotation;

import java.lang.annotation.*;

/**
 * @author thinking
 * @version 1.0
 * @since 2017-12-25
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequiredUser {

    /**
     * true 默认必须登录 ,false 可以非登录访问
     *
     * @return
     */
    boolean required() default true;

    /**
     * 版本控制访问
     *
     * @return
     */
    String version() default "";

}
