package org.nbone.framework.spring.web.log.config;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.nbone.framework.spring.aop.AopUtils;
import org.nbone.framework.spring.web.log.annotation.AccessLog;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
/**
 * Web Access Log  Config
 * @author thinking
 * @version 1.0 
 */
@Configuration
//@ComponentScan(basePackages="org.nbone.framework.spring.web.log")
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class AccessLogConfig implements ApplicationContextAware,InitializingBean{
	protected Log logger  =  LogFactory.getLog(AccessLogConfig.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("========================================================================");
		logger.info("nbone AccessLogConfig starting ....");
		logger.info("========================================================================");
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
	}

    @Pointcut("execution(* com.camelot.ts.controller.seller.PrimerController.*(..))")
    public  void accessLogPointcutExecution() {
    }	
	  
    /**
     * accessLogAspect层切入点
     */
    @Pointcut("@annotation(org.nbone.framework.spring.web.log.annotation.AccessLog)")
    public  void accessLogPointcut() {
    }	
    
    
    @Before(value = "accessLogPointcut()")
    public  void doBefore(JoinPoint joinPoint) {
    	proceed(joinPoint);
    	System.out.println("---------------doBefore---------------------");
    	
    }
    
    @After(value = "accessLogPointcutExecution()")
    public  void doAfter(JoinPoint joinPoint) {
    	
    	System.out.println("---------------doAfter---------------------");
    	
    }

    
    
    public void proceed(JoinPoint joinPoint){
    	
    	Object targetObject = joinPoint.getTarget();
    	Object[] args = joinPoint.getArgs();
    	Method method = AopUtils.getMethod(joinPoint);
    	AccessLog accessLog = AopUtils.getMethodAnnotation(joinPoint, AccessLog.class);
    	AccessLog accessLogObj = targetObject.getClass().getAnnotation(AccessLog.class);
    	
    }
    
	

}
