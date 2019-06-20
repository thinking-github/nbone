package org.nbone.persistence.annotation;

import org.nbone.persistence.enums.QueryType;

import java.lang.annotation.*;

/**
 * 此注解用于 扩展字段 （即非持久化字段） 主要用于数组类型
 * @author chenyicheng
 * @version 1.0
 * @since 2019/6/19
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MappedBy {


    /**
     *  映射的持久化目标字段
     * @return
     */
    String name() default "";
    /**
     *  只能是 in 查询 和  between 查询
     * 字段查询类型
     */
    QueryType queryType() default QueryType.IN;
}
