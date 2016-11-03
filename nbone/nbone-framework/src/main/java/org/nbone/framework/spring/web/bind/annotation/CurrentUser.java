/*
 * ${copyright}
 * ${license}
 */

package org.nbone.framework.spring.web.bind.annotation;


import java.lang.annotation.*;

/**
 * <p>绑定当前登录的用户
 * <p>不同于@ModelAttribute
 *  ItemRequestParam
 *  
 * @author thinking
 * @version 1.0
 * @since 2013-01-12 下午5:01
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    /**
     * 当前用户在request中的名字 默认 user
     *
     * @return
     */
    String value() default "user";

}
