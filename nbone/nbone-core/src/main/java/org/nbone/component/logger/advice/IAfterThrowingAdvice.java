package org.nbone.component.logger.advice;

import org.aspectj.lang.JoinPoint;

public interface IAfterThrowingAdvice {
	
	public void afterThrowing(JoinPoint jp, Throwable throwable);

}
