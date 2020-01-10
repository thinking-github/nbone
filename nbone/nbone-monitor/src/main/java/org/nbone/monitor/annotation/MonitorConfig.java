package org.nbone.monitor.annotation;

import java.lang.annotation.*;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-12
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MonitorConfig {

    /**
     * 与 method name  配合使用组装监控key [prefix.method]
     *
     * @return
     */
    String prefix() default "";

    Class<? extends Throwable>[] ignores() default {};
}
