/*
 * ${copyright}
 * ${license}
 */

package org.nbone.framework.spring.web.bind.annotation;


import java.lang.annotation.*;

/**
 * <p>该注解用于绑定Json请求参数到javaObject</p>
 *
 * @author thinking
 * @version 1.0
 * @since 2015-12-12
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonRequestParam {

    /**
     * 当前用户在request中的名字 默认""
     *
     * @return
     */
    String value() default "";
    
    /**
	 * 是否必须，默认是
	 */
    boolean required() default true;

}
