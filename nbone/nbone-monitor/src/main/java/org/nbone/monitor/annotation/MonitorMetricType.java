package org.nbone.monitor.annotation;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-05
 */
public enum MonitorMetricType {

    /**
     * A  counter is for numeric values that get incremented when some event occurs.
     * 使用此标记的方法，在执行时会统计方法执行次数。
     */
    COUNTER,

    /**
     * 使用此标记的方法，在执行时会统计方法执行时间。
     */
    TIMED

}
