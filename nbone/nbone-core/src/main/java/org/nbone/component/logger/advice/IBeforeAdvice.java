package org.nbone.component.logger.advice;

import org.aspectj.lang.JoinPoint;

public interface IBeforeAdvice {
	
	public void before(JoinPoint jp);

}
