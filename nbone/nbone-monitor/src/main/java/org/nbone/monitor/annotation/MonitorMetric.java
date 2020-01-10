package org.nbone.monitor.annotation;

import java.lang.annotation.*;

/**
 * 被这个注解标记的方法，在执行时会同时统计方法执行次数及方法执行时间。
 * <p>
 * 在执行失败时会上报异常信息到报警系统。
 *
 * @author thinking
 * @version 1.0
 * @since 2019-10-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface MonitorMetric {


    /**
     * @return 可作为统计数据的key。
     */
    String value() default "";

    /**
     * 监控指标类型
     *
     * @return
     */
    MonitorMetricType type() default MonitorMetricType.TIMED;

    /**
     * 和 method 配合使用组装监控key [prefix.method]
     *
     * @return
     */
    String prefix() default "";

    /**
     * 和 prefix 配合使用组装监控key [prefix.method] 不设置默认使用目标方法名称
     * 默认值使用方法名称
     *
     * @return
     */
    String method() default "";

    /**
     * ignores exception
     *
     * @return
     */
    Class<? extends Throwable>[] ignores() default {};
}
