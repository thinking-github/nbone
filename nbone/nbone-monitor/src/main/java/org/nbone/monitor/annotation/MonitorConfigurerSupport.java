package org.nbone.monitor.annotation;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-14
 */
public class MonitorConfigurerSupport implements MonitorConfigurer {

    @Override
    public Class<? extends Throwable>[] ignoresException() {
        return new Class[]{IllegalArgumentException.class, IllegalStateException.class};
    }
}
