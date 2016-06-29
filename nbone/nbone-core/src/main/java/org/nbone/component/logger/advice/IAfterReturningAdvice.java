package org.nbone.component.logger.advice;

import org.aspectj.lang.JoinPoint;

public interface IAfterReturningAdvice {
	
	public void afterReturning(JoinPoint jp);

}
