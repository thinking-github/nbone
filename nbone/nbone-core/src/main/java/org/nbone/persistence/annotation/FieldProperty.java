package org.nbone.persistence.annotation;

import org.nbone.persistence.enums.QueryType;

import java.lang.annotation.*;

/**
 * @author thinking
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldProperty {


    /**
     * 映射的持久化目标字段
     *
     * @return
     */
    String name() default "";

    /**
     * 字段级别 分为10个级别（像线程一样分优先级）
     *
     * @return
     */
    FieldLevel value() default FieldLevel.ALL;

    /**
     * 字段查询类型
     *
     * @since 2019/6/19
     */
    QueryType queryType() default QueryType.EQ;

}
