package org.nbone.component.logger.advice;

import org.aspectj.lang.JoinPoint;

public interface IAfterAdvice {
	
	public void after(JoinPoint jp);

}
