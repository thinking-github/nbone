package org.nbone.component.logger;

import org.aspectj.lang.JoinPoint;

public interface IAdvice {
	
	public void before(JoinPoint jp);
	public void after(JoinPoint jp);
	
	public void afterReturning(JoinPoint jp);
	public void afterThrowing(JoinPoint jp, Throwable throwable);

}
