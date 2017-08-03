package org.nbone.framework.spring.web.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description
 * @author thinking
 * @version 1.0 
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLog {
	
	/**
	 * 模块名称
	 */
	 String module() default "";
	/**
	 * 日志信息描述
	 */
	 String value() default "";
	 /**
	  * 日志信息描述 当指定 desc覆盖value
	  */
	 String desc()  default "";
	
	

}
