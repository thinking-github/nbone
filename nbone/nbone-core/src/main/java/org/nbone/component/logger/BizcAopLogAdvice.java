package org.nbone.component.logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.nbone.aop.AopUtils;


/**
 * AOP Log 前置后置处理通知
 * @author Thinking  2014-8-8
 * @see AopLogProcessor
 */
public class BizcAopLogAdvice implements IAdvice{
	
	private Log logger = LogFactory.getLog(BizcAopLogAdvice.class);
	private AbstractAopLogProcessor aopLogProcessor; 
	
	public AbstractAopLogProcessor getAopLogProcessor() {
		return aopLogProcessor;
	}
	public void setAopLogProcessor(AbstractAopLogProcessor aopLogProcessor) {
		this.aopLogProcessor = aopLogProcessor;
	}
	
    public void before(JoinPoint jp) {
    	process(jp, null);
	}
	public void after(JoinPoint jp){
		process(jp, null);
		
	}
	public void afterReturning(JoinPoint jp){
		process(jp, null);
		
	}
	public void afterThrowing(JoinPoint jp, Throwable throwable){
		process(jp, throwable);
		
	} 
	
    public void process(JoinPoint jp,Throwable throwable){
    	try {
			Object args[] = jp.getArgs();
			String fullClassName = AopUtils.getLongClassName(jp);
			String methodName = AopUtils.getMethodName(jp);
			String cnmn =  AopUtils.getClassNameAndMethodName(jp);
			//日志处理器
		    aopLogProcessor.process(fullClassName, methodName, args,throwable);
			 
			
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    	
		
	}
	private Map<String,String> proessMethodParams(Object[] objArgs,String[] configPlace) {
		Map<String , String>  configPlaceMap = new HashMap<String, String>();
		for(Object o:objArgs){
			if(o instanceof List){
				List<Map> list = (List<Map>)o;
				Map map = list.get(0);
			}else if(o instanceof String){
				
			}else{
				
			}
			break;
		}
		return configPlaceMap;
		
	}
	
}
