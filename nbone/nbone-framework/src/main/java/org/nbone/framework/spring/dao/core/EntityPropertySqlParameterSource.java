package org.nbone.framework.spring.dao.core;

import java.util.Map;

import org.nbone.persistence.mapper.DbMappingBuilder;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.TableMapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

/**
 * 使用数据库字段名映射
 * <p> 扩展spring BeanPropertySqlParameterSource
 * 
 * @author thinking
 * @version 1.0 
 * @see org.springframework.jdbc.core.namedparam.SqlParameterSource
 * @see org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource
 * @see org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
 * @see org.springframework.jdbc.core.StatementCreatorUtils
 * @since spring 2.0
 * 
 */
public class EntityPropertySqlParameterSource  extends AbstractSqlParameterSource{

	private final BeanWrapper beanWrapper;
	
	private final TableMapper<?> tableMapper;
	
	private Map<String, FieldMapper> dbFieldNameMap;
	
	
	public EntityPropertySqlParameterSource(Object object) {
		this.tableMapper = DbMappingBuilder.ME.getTableMapper(object.getClass());
		this.dbFieldNameMap = tableMapper.getFieldMappers();
		this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
		
	}
    /***
     * @param paramName 数据库字段名称
     * @return
     */
	@Override
	public boolean hasValue(String paramName) {
		String propertyName  = dbFieldNameMap.get(paramName).getFieldName();
		return this.beanWrapper.isReadableProperty(propertyName);
	}
	/***
     * @param paramName 数据库字段名称
     * @return
     */
	@Override
	public Object getValue(String paramName) throws IllegalArgumentException {
		String propertyName  = dbFieldNameMap.get(paramName).getFieldName();
		return this.beanWrapper.getPropertyValue(propertyName);
	}

	/***
     * @param paramName 数据库字段名称
     * @return
     */
	@Override
	public int getSqlType(String paramName) {
		String propertyName  = dbFieldNameMap.get(paramName).getFieldName();
		int sqlType = super.getSqlType(propertyName);
		if (sqlType != TYPE_UNKNOWN) {
			return sqlType;
		}
		Class<?> propType = this.beanWrapper.getPropertyType(propertyName);
		return StatementCreatorUtils.javaTypeToSqlParameterType(propType);
	}

}
