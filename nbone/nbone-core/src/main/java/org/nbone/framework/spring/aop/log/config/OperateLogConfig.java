package org.nbone.framework.spring.aop.log.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@Aspect
@EnableAspectJAutoProxy
public class OperateLogConfig  implements ApplicationContextAware,InitializingBean{
	protected Log logger  =  LogFactory.getLog(OperateLogConfig.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("========================================================================");
		logger.info("nbone OperateLogConfig starting ....");
		logger.info("========================================================================");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	}

	
    /**
     * accessLogAspect层切入点
     */
    @Pointcut("@annotation(org.nbone.framework.spring.aop.log.annotation.OperateLog)")
    public  void operateLogPointcut() {
    }	
    
    
    
    
    @AfterReturning(value = "operateLogPointcut()")
    public  void afterReturning(JoinPoint joinPoint) {
    	
    	System.out.println("---------------afterReturning---------------------");
    	
    }
    @AfterThrowing(value = "operateLogPointcut()")
    public  void afterThrowsAdvice(JoinPoint joinPoint,Throwable throwable) {
    	
    	System.out.println("---------------afterThrowsAdvice---------------------");
    	
    }
    
}
