package org.nbone.framework.spring.dao.core;

import java.util.Map;

import org.nbone.persistence.mapper.MappingBuilder;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.EntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(EntityPropertySqlParameterSource.class);
	private final BeanWrapper beanWrapper;
	
	private final EntityMapper<?> entityMapper;
	private final  Object object;
	
	private Map<String, FieldMapper> dbFieldNameMap;
	
	
	public EntityPropertySqlParameterSource(Object object) {
		this.entityMapper = MappingBuilder.ME.getTableMapper(object.getClass());
		this.dbFieldNameMap = entityMapper.getFieldMappers();
		this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
		this.object = object;
		
	}
    /***
     * @param paramName 数据库字段名称
     * @return
     */
	@Override
	public boolean hasValue(String paramName) {
		FieldMapper fieldMapper = dbFieldNameMap.get(paramName);
		check(fieldMapper,paramName);
		String propertyName  = fieldMapper.getFieldName();
		return this.beanWrapper.isReadableProperty(propertyName);
	}
	/***
     * @param paramName 数据库字段名称
     * @return
     */
	@Override
	public Object getValue(String paramName) throws IllegalArgumentException {
		FieldMapper fieldMapper = dbFieldNameMap.get(paramName);
		check(fieldMapper,paramName);
		String propertyName  = fieldMapper.getFieldName();
		return this.beanWrapper.getPropertyValue(propertyName);
	}

	/***
     * @param paramName 数据库字段名称
     * @return
     */
	@Override
	public int getSqlType(String paramName) {
		FieldMapper fieldMapper = dbFieldNameMap.get(paramName);
		check(fieldMapper,paramName);
		String propertyName  = fieldMapper.getFieldName();
		int sqlType = super.getSqlType(propertyName);
		if (sqlType != TYPE_UNKNOWN) {
			return sqlType;
		}
		Class<?> propType = this.beanWrapper.getPropertyType(propertyName);
		return StatementCreatorUtils.javaTypeToSqlParameterType(propType);
	}

	public Object getObject() {
		return object;
	}

	private FieldMapper getFieldMapper(String paramName){
		FieldMapper fieldMapper = dbFieldNameMap.get(paramName);
		//partition -> `partition`; key -> `key`
		if (fieldMapper == null && paramName.charAt(0) != '`') {
			String escapeName = '`' + paramName + '`';
			fieldMapper = dbFieldNameMap.get(escapeName);
		}
		return fieldMapper;
	}

	private  void check(FieldMapper fieldMapper,String paramName){
		if(fieldMapper == null){
			logger.error("table name '{}',columns : {}",entityMapper.getTableName(object),entityMapper.getCommaDelimitedColumns());
			throw new IllegalArgumentException("["+object.getClass().getName()+"] Cannot resolve field: " +paramName);
		}
	}
}
