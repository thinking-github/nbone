package org.nbone.persistence.annotation;

import org.nbone.persistence.enums.QueryType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author thinking
 *
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldProperty {

	/**
	 * 字段级别 分为10个级别（像线程一样分优先级）
	 * @return
	 */
	FieldLevel value()  default FieldLevel.ALL;

	/**
	 * 字段查询类型
	 * @since 2019/6/19
	 */
	QueryType queryType() default QueryType.EQ;

}
