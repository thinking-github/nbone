package org.nbone.persistence.mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.nbone.persistence.enums.JdbcType;

/**
 * 字段映射类，用于描述java对象字段和数据库表字段之间的对应关系<br>
 * 
 * @author thinking
 * @since 2015-12-12
 * 
 */
public class FieldMapper {
    /**
     * Java对象字段名
     */
    private String fieldName;
    /**
     * 数据库表字段名
     */
    private String dbFieldName;
    /**
     * 是否是数据库表主键
     */
    private boolean primaryKey;
    /**
     * 实体属性类型
     */
    private Class<?> propertyType;
    /**
     * 数据库字段对应的jdbc类型
     */
    private JdbcType jdbcType;
    
    
     
    private  boolean nullable   = true;
    private  boolean insertable = true;
    private  boolean updatable  =true;
    
    @SuppressWarnings("rawtypes")
    private Class enumClass = null;
    
    private PropertyDescriptor propertyDescriptor;
    
    
    
    public FieldMapper(PropertyDescriptor propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
		Method getter = propertyDescriptor.getReadMethod();
    	this.enumClass = getter.getReturnType().isEnum() ? getter.getReturnType() : null;
	}

    
	public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDbFieldName() {
    	if(dbFieldName == null || dbFieldName.trim().length()==0){
    		this.dbFieldName = this.fieldName;
    	}
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Class<?> getPropertyType() {
		if(propertyType == null){
			propertyType = propertyDescriptor.getPropertyType();
		}
		
		return propertyType;
	}


	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
	}


	public JdbcType getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public Class getEnumClass() {
		return enumClass;
	}

	public void setEnumClass(Class enumClass) {
		this.enumClass = enumClass;
	}

	public PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	public void setPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
	}
	
	
	@SuppressWarnings("unchecked")
	public Object get(Object target) throws Exception {
		Method getter = propertyDescriptor.getReadMethod();
	    Object r = getter.invoke(target);
        return enumClass==null ? r : Enum.valueOf(enumClass, (String) r);
	}

	@SuppressWarnings("unchecked")
	public void set(Object target, Object value) throws Exception {
        if (enumClass != null && value != null) {
            value = Enum.valueOf(enumClass, (String) value);
        }
        Method setter = propertyDescriptor.getWriteMethod();
        setter.invoke(target, value);
    
	}
    
}
