package org.nbone.monitor.annotation;

import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MonitorConfiguration.class)
public @interface EnableMonitor {

    /**
     * Global ignores exception
     *
     * @return
     */
    Class<? extends Throwable>[] ignores() default {};
}
