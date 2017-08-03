package org.nbone.framework.spring.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> Class Name auto mapping 
 * <p> Method Name auto mapping
 * @author  thining
 * @version 1.0 
 * @since spring 3.1
 * @see  DefaultRequestMapping
 */
@Deprecated  
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClassMethodNameRequestMapping {

}
