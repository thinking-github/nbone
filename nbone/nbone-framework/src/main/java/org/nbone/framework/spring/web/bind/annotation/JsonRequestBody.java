package org.nbone.framework.spring.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author thinking
 * @version 1.0 
 * @since 2015-12-12
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonRequestBody {
	
    /**
	 * 是否必须，默认是
	 */
    boolean required() default true;

}
