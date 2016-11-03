package org.nbone.framework.spring;

/**
 * 
 * @author  <a href="mailto: chenyicheng43@camelotchina.com">ChenYiCheng</a>
 * @version 1.0 
 * @see org.springframework.core.env.Environment
 */
public interface SpringEnv {
	
	String SystemProperties = "#!{@systemProperties ?: java.util.Properties()}";

}
