package org.nbone.monitor.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-12
 */
public class MonitorAnnotationParser {

    public static final String DATA_KEY_DELIMITER_DOT = ".";

    private static final Logger logger = LoggerFactory.getLogger(MonitorAnnotationParser.class);

    private Set<Class<? extends Throwable>> ignoresException;


    public boolean ignoresException(Throwable throwable, Object target, Method method) {
        Class<? extends Throwable>[] classIgnores = getMonitorIgnoresException(target);
        // class level ignores exception
        if (ignoresException(throwable, classIgnores)) {
            return true;
        }
        Class<? extends Throwable>[] methodIgnores = getMonitorIgnoresException(method);
        // method level ignores exception
        if (ignoresException(throwable, methodIgnores)) {
            return true;
        }
        return false;
    }

    public Class<? extends Throwable>[] getMonitorIgnoresException(Object target) {
        Class<?> targetClass = this.getTargetClass(target);
        MonitorConfig monitorConfig = targetClass.getAnnotation(MonitorConfig.class);
        if (monitorConfig != null) {
            return monitorConfig.ignores();
        }
        return null;
    }

    public Class<? extends Throwable>[] getMonitorIgnoresException(Method method) {
        MonitorMetric monitorMetric = method.getAnnotation(MonitorMetric.class);
        if (monitorMetric != null) {
            return monitorMetric.ignores();
        }
        return null;
    }

    public String getMonitorMetricKey(Object target, Method method) {
        MonitorMetric monitorMetric = method.getAnnotation(MonitorMetric.class);
        if (monitorMetric != null) {
            return getMonitorMetricKey(monitorMetric, target, method);
        }
        Class<?> targetClass = this.getTargetClass(target);
        return targetClass.getName() + DATA_KEY_DELIMITER_DOT + method.getName();
    }

    public String getMonitorMetricKey(MonitorMetric monitorMetric, Object target, Method method) {
        String value = monitorMetric.value();
        String prefix = monitorMetric.prefix();
        String methodName = monitorMetric.method();
        return getMetricKey(value, prefix, methodName, target, method);
    }
    /**
     * <li> 优先使用value 作为全限定名key
     * <li> prefix + methodName 次之
     * <li> className + methodName 再次之
     *
     * @param value      监控指标全限定名称
     * @param prefix     监控指标前缀
     * @param methodName 监控指标目标方法名称
     * @param method     监控调用的目标方法
     * @param target     监控调用的目标对象
     * @return
     */
    protected String getMetricKey(String value, String prefix, String methodName, Object target, Method method) {
        logger.debug("monitorMetric-->value={},prefix={},methodName={}", value, prefix, methodName);
        if (StringUtils.hasLength(value)) {
            return value;
        }

        String methodNameUse;
        if (StringUtils.hasLength(methodName)) {
            methodNameUse = methodName;
        } else {
            methodNameUse = method.getName();
        }

        if (StringUtils.hasLength(prefix)) {
            return prefix + DATA_KEY_DELIMITER_DOT + methodNameUse;
        }
        //use method target class @MonitorConfig  prefix
        Class<?> targetClass = this.getTargetClass(target);
        MonitorConfig monitorConfig = targetClass.getAnnotation(MonitorConfig.class);
        if (monitorConfig != null && StringUtils.hasLength(monitorConfig.prefix())) {
            prefix = monitorConfig.prefix();
            return prefix + DATA_KEY_DELIMITER_DOT + methodNameUse;

        }
        // default className.methodName
        return targetClass.getName() + DATA_KEY_DELIMITER_DOT + methodNameUse;
    }


    public boolean ignoresGlobalException(Throwable throwable) {
        if (CollectionUtils.isEmpty(ignoresException)) {
            return false;
        }
        for (Class<? extends Throwable> ignores : ignoresException) {
            if (ignores.isAssignableFrom(throwable.getClass())) {
                return true;
            }
        }
        return false;
    }

    public boolean ignoresException(Throwable throwable, Class<? extends Throwable>[] ignoresException) {
        if (ObjectUtils.isEmpty(ignoresException)) {
            return false;
        }
        for (Class<? extends Throwable> ignores : ignoresException) {
            if (ignores.isAssignableFrom(throwable.getClass())) {
                return true;
            }
        }
        return false;
    }

    private Class<?> getTargetClass(Object target) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        if (targetClass == null && target != null) {
            targetClass = target.getClass();
        }
        return targetClass;
    }


    public Set<Class<? extends Throwable>> getIgnoresException() {
        return ignoresException;
    }

    public void setIgnoresException(Set<Class<? extends Throwable>> ignoresException) {
        this.ignoresException = ignoresException;
    }
}
