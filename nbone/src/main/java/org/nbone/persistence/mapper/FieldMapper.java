package org.nbone.persistence.mapper;

import org.apache.ibatis.type.JdbcType;

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
     * 数据库字段对应的jdbc类型
     */
    @Deprecated
    private JdbcType jdbcType;
    
    
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDbFieldName() {
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

	public JdbcType getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }
}
