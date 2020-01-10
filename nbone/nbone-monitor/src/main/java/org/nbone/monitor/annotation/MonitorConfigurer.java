package org.nbone.monitor.annotation;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-14
 */
public interface MonitorConfigurer {

    /**
     * Global ignores exception
     *
     * @return
     */
    Class<? extends Throwable>[] ignoresException();
}
