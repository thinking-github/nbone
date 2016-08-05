package org.nbone.persistence.mapper;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Table;

import org.nbone.persistence.exception.PrimaryKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

/**
 * 描述java对象的数据库映射信息（数据库表的映射、字段的映射）<br>
 * 
 * @author thinking
 * @since 2015-12-12
 * @see javax.persistence.Entity
 * @see javax.persistence.Table
 * @see org.springframework.jdbc.core.RowMapper
 * 
 */
public class TableMapper<T> {
    /**
     * 当使用注解定义实体Bean时使用
     */
    private Annotation tableMapperAnnotation;
    /**
     * 数据库表主键类型：（单个字段的唯一键）（几个字段组合起来的唯一键）
     */
    private List<String> primaryKeys = new ArrayList<String>(1);
    
    /**
     * 映射实体Bean的全名称(包含包名称)<br>
     * 请使用 entityClazz
     */
    @Deprecated 
    private String  entityName;
    /**
     * 数据库表的名称
     */
    private String  dbTableName;
    /**
     * 映射实体Bean classs
     */
    private Class<T> entityClazz;
    
    /**
     * 以数据库字段为Key , 最好使用LinkedHashMap保证key的顺序
     */
    private Map<String, FieldMapper> fieldMappers = new LinkedHashMap<String, FieldMapper>();
    
    /**
     * 以JavaBean属性为Key
     */
    private Map<String, PropertyDescriptor> mappedPropertys= new HashMap<String, PropertyDescriptor>();
    
    /**
     * 数据库表字段映射列表
     */
    private List<FieldMapper> fieldMapperList =  new  ArrayList<FieldMapper>();
    /**
     * 数据库列名称数组
     */
    private String[] columnNames;
    /**
     * id,name,age
     */
    private String   commaDelimitedColumns;
    
    private StringBuilder selectAllSql;
    
    private StringBuilder deleteAllSql;
    
    private StringBuilder countSql;
    /**
     * 根据Id查询 使用?占位符
     */
    private StringBuilder selectSqlWithId;
    /**
     * 根据Id删除 使用?占位符
     */
    private StringBuilder deleteSqlWithId;
    
    
    /**
     * Spring Jdbc
     */
    private RowMapper<T> rowMapper;
    
    

	public TableMapper(Class<T> entityClazz) {
		this.entityClazz = entityClazz;
		this.entityName = entityClazz.getName();
	}
	
	/**
	 * 设定预定的容量  XXX： thinking 2016-08-02
	 * @param entityClazz
	 * @param initialCapacity
	 */
	public TableMapper(Class<T> entityClazz,int initialCapacity) {
		this(entityClazz);
		fieldMapperList =  new ArrayList<FieldMapper>(initialCapacity);
		fieldMappers =  new LinkedHashMap<String, FieldMapper>(initialCapacity);
		mappedPropertys =  new HashMap<String, PropertyDescriptor>(initialCapacity);
	}
	
	public Annotation getTableMapperAnnotation() {
		return tableMapperAnnotation;
	}

	public void setTableMapperAnnotation(Annotation tableMapperAnnotation) {
		this.tableMapperAnnotation = tableMapperAnnotation;
	}

	public String[] getPrimaryKeys() {
		List<String> primaryKeys = getPrimaryKeyList();
		String[] pks = new String[primaryKeys.size()];
		pks = primaryKeys.toArray(pks);
		return pks;
	}
	
	public List<String> getPrimaryKeyList() {
		if(primaryKeys == null || primaryKeys.size() == 0){
			for (FieldMapper fieldMapper : fieldMapperList) {
				if(fieldMapper.isPrimaryKey()){
					primaryKeys.add(fieldMapper.getDbFieldName());
				}
			}
		}
		
		return primaryKeys;
	}
	
	public String getPrimaryKey(){
		List<String> primaryKeys = getPrimaryKeyList();
		
		if(primaryKeys == null || primaryKeys.size() <= 0 ){
			return null;
		}
		
		return primaryKeys.get(0);
	}

	public void setPrimaryKeys(String[] primaryKeys) {
		this.primaryKeys = Arrays.asList(primaryKeys);
	}
	public void setPrimaryKeys(List<String> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public String getEntityName() {
		return entityName;
	}
    
	/**
	 * <li> 优先使用 dbTableName设置的表名称
	 * <li> 次之使用 注解映射的表名称
	 * <li> 次之最后使用 entityClazz的短名称
	 * @return TableName
	 */
	public String getDbTableName() {
		//当没有设置dbTableName时 使用注解映射的名称
		//1.
		if(dbTableName == null){
			//2.
			if(tableMapperAnnotation == null){
				Annotation[] anns  = entityClazz.getDeclaredAnnotations();
				for (Annotation annotation : anns) {
					if(annotation instanceof Table){
						Table table  = (Table) annotation;
						dbTableName = table.name();
						
						return dbTableName;
					}
					
				}
			}
			//3.
			if(tableMapperAnnotation instanceof Table){
				Table table  = (Table) tableMapperAnnotation;
				dbTableName = table.name();
			}
			//4.
			if(dbTableName == null){
				dbTableName = entityClazz.getSimpleName();
			}
		}
		
		return dbTableName;
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}

	public Class<T> getEntityClazz() {
		return entityClazz;
	}

	public void setEntityClazz(Class<T> entityClazz) {
		this.entityClazz = entityClazz;
	}
    /**
     * @see #fieldMapperCache
     */
	public Map<String, FieldMapper> getFieldMappers() {
		
		return fieldMappers;
	}

	public void setFieldMappers(Map<String, FieldMapper> fieldMappers) {
		this.fieldMappers.putAll(fieldMappers);
		for (Map.Entry<String,FieldMapper> entry : fieldMappers.entrySet()) {
			this.fieldMapperList.add(entry.getValue());
		}
	}
	
	public FieldMapper getFieldMapper(String dbFieldName) {
		return fieldMappers.get(dbFieldName);
	}
	/**
	 * 根据数据库字段名称查询Java字段名称
	 * @param dbFieldName
	 * @return
	 */
	public String getFieldName(String dbFieldName) {
		return fieldMappers.get(dbFieldName).getFieldName();
	}
	/**
	 * 根据Java字段名称查询数据库字段名称
	 * @param fieldName
	 * @return
	 */
	public String getDbFieldName(String fieldName) {
		if(fieldName == null){
			return null;
		}
		for (FieldMapper fieldMapper : fieldMapperList) {
			if(fieldName.equals(fieldMapper.getFieldName())){
				return fieldMapper.getDbFieldName();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param dbFieldName
	 * @param fieldMapper
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public TableMapper addFieldMapper(String dbFieldName,FieldMapper fieldMapper) {
		this.fieldMappers.put(dbFieldName, fieldMapper);
		this.fieldMapperList.add(fieldMapper);
		
		return this;
	}

	
	
    public Map<String, PropertyDescriptor> getMappedPropertys() {
		return mappedPropertys;
	}

	public void setMappedPropertys(Map<String, PropertyDescriptor> mappedPropertys) {
		this.mappedPropertys = mappedPropertys;
	}
	
	public PropertyDescriptor getPropertyDescriptor(String fieldName) {
		return mappedPropertys.get(fieldName);
	}
	@SuppressWarnings("rawtypes")
	public TableMapper addPropertyDescriptor(String fieldName,PropertyDescriptor pd) {
		this.mappedPropertys.put(fieldName, pd);
		return this;
	}

	
	
	public List<FieldMapper> getFieldMapperList() {
		return Collections.unmodifiableList(fieldMapperList);
	}
	
	public FieldMapper getFieldMapper(int index) {
		return fieldMapperList.get(index);
	}


	/**
     * @see #columnNames
     */
	public String[] getColumnNames() {
		if(columnNames == null || columnNames.length == 0){
			Set<String> keys = fieldMappers.keySet();
			columnNames =  new String[keys.size()];
			columnNames = keys.toArray(columnNames);
			return columnNames;
		}
		return columnNames;
	}


	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

    /**
     * @see #commaDelimitedColumns
     */
	public String getCommaDelimitedColumns() {
		if(commaDelimitedColumns == null){
			String[] names = getColumnNames();
			commaDelimitedColumns = StringUtils.arrayToCommaDelimitedString(names);
			return commaDelimitedColumns;
		}
		
		return commaDelimitedColumns;
	}

	public void setCommaDelimitedColumns(String commaDelimitedColumns) {
		this.commaDelimitedColumns = commaDelimitedColumns;
	}

	
	public StringBuilder getSelectAllSql() {
		if(selectAllSql == null){
			selectAllSql = new StringBuilder();
			selectAllSql.append("SELECT ");
	        
		    selectAllSql.append(this.getCommaDelimitedColumns());
		        
		    selectAllSql.append(" FROM ").append(this.getDbTableName());
		}
		
		return selectAllSql;
	}


	public StringBuilder getDeleteAllSql() {
		if(deleteAllSql == null ){
			deleteAllSql  = new StringBuilder();
			deleteAllSql.append("delete from ").append(this.getDbTableName());
		}
		
		return deleteAllSql;
	}

	public StringBuilder getCountSql() {
		if(countSql == null){
			countSql = new StringBuilder();
			countSql.append("select count(1) from ").append(this.getDbTableName());
		}
		
		return countSql;
	}

	
	public StringBuilder getSelectSqlWithId() {
		if(selectSqlWithId == null){
			selectSqlWithId = new StringBuilder(getSelectAllSql());
			
			String  primaryKey=  getPrimaryKey();
			if(primaryKey == null){
				throw new PrimaryKeyException("table name "+this.dbTableName +" not have primaryKey");
			}
			
			selectSqlWithId.append(" where ").append(primaryKey).append(" = ?");
		}
		
		return selectSqlWithId;
	}

	public StringBuilder getDeleteSqlWithId() {
		if(deleteSqlWithId == null){
			deleteSqlWithId = new StringBuilder(getDeleteAllSql());
			
			String  primaryKey=  getPrimaryKey();
			if(primaryKey == null){
				throw new PrimaryKeyException("table name "+this.dbTableName +" not have primaryKey");
			}
			
			deleteSqlWithId.append(" where ").append(primaryKey).append(" = ?");
		}
		return deleteSqlWithId;
	}

	public  RowMapper<T> getRowMapper() {
		return  rowMapper;
	}
	

	public void setRowMapper(RowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}
	

}
