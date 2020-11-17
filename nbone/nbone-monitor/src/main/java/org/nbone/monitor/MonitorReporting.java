package org.nbone.monitor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.nbone.monitor.annotation.MonitorMetric;

/**
 * 统一埋点数据上报切面。
 *
 * @author thinking
 * @version 1.0
 * @since 2019-10-12
 */
public interface MonitorReporting {

    String PUBLIC_EXECUTION = "execution(public * *(..))";

    //String feign = "@within(org.springframework.cloud.netflix.feign.FeignClient)";

    //String feign = "within(feign.Client+)";

    //String feign_execute = "execution(public * feign.Client+.execute(..))";


    String MONITOR_EXECUTION = "execution(public * org.nbone.monitor.annotation.Monitor+.*(..))";
    String OR_MONITOR_EXECUTION = " || " + MONITOR_EXECUTION;

    String PUBLIC_MONITOR_ANNOTATION = "@annotation(org.nbone.monitor.annotation.MonitorMetric)";


   /* @Pointcut(value = "execution(public * *(..))")
    public void publicMethods();*/

    @Around("@annotation(monitorMetric)")
    public Object reportMetric(ProceedingJoinPoint joinPoint, MonitorMetric monitorMetric) throws Throwable;


    @Around(MONITOR_EXECUTION)
    public Object monitor(ProceedingJoinPoint joinPoint) throws Throwable;

    @AfterThrowing(pointcut = PUBLIC_MONITOR_ANNOTATION, throwing = "throwable")
    public void reportException(JoinPoint joinPoint, Throwable throwable);


}
