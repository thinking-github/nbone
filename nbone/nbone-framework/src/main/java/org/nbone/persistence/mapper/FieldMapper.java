package org.nbone.persistence.mapper;

import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.annotation.FieldProperty;
import org.nbone.persistence.enums.JdbcType;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
    private String columnName;
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
    
    private FieldLevel fieldLevel = FieldLevel.NORM_VALUE;

	private boolean unique     = false;
    private boolean nullable   = true;
    private boolean insertable = true;
    private boolean updatable  = true;
    
    @SuppressWarnings("rawtypes")
    private Class enumClass = null;
    
    private PropertyDescriptor propertyDescriptor;


	public FieldMapper(String fieldName, Class<?> propertyType, PropertyDescriptor propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
		this.fieldName = fieldName != null ? fieldName : propertyDescriptor.getName();
		this.propertyType = propertyType != null ? propertyType : propertyDescriptor.getPropertyType();
		Method getter = propertyDescriptor.getReadMethod();
		this.enumClass = getter.getReturnType().isEnum() ? getter.getReturnType() : null;
	}

	public FieldMapper(PropertyDescriptor propertyDescriptor) {
    	this(propertyDescriptor.getName(),propertyDescriptor.getPropertyType(),propertyDescriptor);
	}


    
	public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDbFieldName() {
		return getColumnName();
    }

	public String getColumnName() {
		if (columnName == null || columnName.trim().length() == 0) {
			this.columnName = this.fieldName;
		}
		return columnName;
	}

	public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
		if(primaryKey){
			fieldLevel = FieldLevel.ID;
		}
		
	}

	public Class<?> getPropertyType() {
		if (propertyType == null) {
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

    
	public FieldLevel getFieldLevel() {
		return fieldLevel;
	}
	public void setFieldLevel(FieldLevel fieldLevel) {
		this.fieldLevel = fieldLevel;
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

	public Class<?> getEnumClass() {
		return enumClass;
	}

	public void setEnumClass(Class<?> enumClass) {
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
	
	public static void setFieldProperty(Field field,FieldMapper fieldMapper){
		if(field == null){
			return ;
		}
     	FieldProperty fieldProperty = field.getAnnotation(FieldProperty.class);
      	if(fieldProperty == null){
      		fieldMapper.setFieldLevel(FieldLevel.NORM_VALUE);
      	}else{
      		fieldMapper.setFieldLevel(fieldProperty.value());
      	}
		
	}
	
	
    
}
