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
    private Map<String, FieldMapper> fieldMappers;
    
    /**
     * 以JavaBean属性为Key
     */
    private Map<String, PropertyDescriptor> mappedPropertys;
    
    /**
     * 数据库表字段映射列表
     */
    private  List<FieldMapper> fieldMapperList ;
    /**
     * 数据库列名称数组
     */
    private String[] columnNames;
    /**
     * id,name,age
     */
    private String   commaDelimitedColumns;
    
    private String selectAllSql;
    
    private String deleteAllSql;
    
    private String countSql;
    /**
     * 根据Id查询 使用?占位符
     */
    private String selectSqlWithId;
    /**
     * 根据Id删除 使用?占位符
     */
    private String deleteSqlWithId;
    
    
    /**
     * Spring Jdbc
     */
    private RowMapper<T> rowMapper;
    
    

	public TableMapper(Class<T> entityClazz) {
		this(entityClazz, 10);
	}
	
	/**
	 * 设定预定的容量  XXX： thinking 2016-08-02
	 * @param entityClazz
	 * @param initialCapacity
	 */
	public TableMapper(Class<T> entityClazz,int fieldInitialCapacity) {
		fieldInitialCapacity = fieldInitialCapacity +5;
		
		if(fieldInitialCapacity < 10){
			fieldInitialCapacity = 10;
		}
		
		this.entityClazz = entityClazz;
		this.entityName = entityClazz.getName();
		this.fieldMapperList =  new ArrayList<FieldMapper>(fieldInitialCapacity);
		this.fieldMappers =  new LinkedHashMap<String, FieldMapper>(fieldInitialCapacity);
		this.mappedPropertys =  new HashMap<String, PropertyDescriptor>(fieldInitialCapacity);
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
	
	public FieldMapper getFieldMapperByPropertyName(String propertyName) {
		if(propertyName == null){
			return null;
		}
		for (FieldMapper fieldMapper : fieldMapperList) {
			if(propertyName.equals(fieldMapper.getFieldName())){
				return fieldMapper;
			}
		}
		return null;
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
		FieldMapper fieldMapper = getFieldMapperByPropertyName(fieldName);
		if(fieldMapper == null){
			return null;
		}
		return fieldMapper.getDbFieldName();
	
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

	/**
	 * 查询全部sql (返回副本)
	 * @return
	 */
	public String getSelectAllSql() {
		if(selectAllSql == null){
			StringBuilder selectAllSqlTemp = new StringBuilder();
			selectAllSqlTemp.append("SELECT ");
			selectAllSqlTemp.append(this.getCommaDelimitedColumns());
			selectAllSqlTemp.append(" FROM ").append(this.getDbTableName());

			
		    this.selectAllSql = selectAllSqlTemp.toString();
		}
		
		return selectAllSql;
	}
	/**
	 * 根字段返回查询sql 用于按需字段查询
	 * @param fieldNames
	 * @param isDbFieldName true为数据库字段名称,false 为Java字段名称
	 * @return
	 */
	public String getSelectAllSql(String[] fieldNames,boolean isDbFieldName) {
		if(fieldNames == null || fieldNames.length == 0){
			return getSelectAllSql();
		}
	    StringBuilder selectAllSql = new StringBuilder();
		selectAllSql.append("SELECT ");
		
		if(isDbFieldName){
				selectAllSql.append(StringUtils.arrayToDelimitedString(fieldNames, ","));
			
		}else{
			for (int i = 0; i < fieldNames.length; i++) {
				String dbFieldName = getDbFieldName(fieldNames[i]);
				if(i > 0){
					selectAllSql.append(",");
				}
				selectAllSql.append(dbFieldName);
			}
		}
		
	    selectAllSql.append(" FROM ").append(this.getDbTableName());
		
		return selectAllSql.toString();
	}

   /**
    * 返回副本
    * @return
    */
	public String getDeleteAllSql() {
		if(deleteAllSql == null ){
			StringBuilder deleteAllSqlTemp  = new StringBuilder();
			deleteAllSqlTemp.append("delete from ").append(this.getDbTableName());
			
			this.deleteAllSql = deleteAllSqlTemp.toString();
		}
		
		return deleteAllSql;
	}

	public String getCountSql() {
		if(countSql == null){
			StringBuilder countSqlTemp = new StringBuilder();
			countSqlTemp.append("select count(1) from ").append(this.getDbTableName());
			
			this.countSql = countSqlTemp.toString();
		}
		
		return countSql;
	}

	
	public String getSelectSqlWithId() {
		if(selectSqlWithId == null){
			StringBuilder selectSqlWithIdTemp = new StringBuilder(getSelectAllSql());
			
			String  primaryKey=  getPrimaryKey();
			if(primaryKey == null){
				throw new PrimaryKeyException("table name "+this.dbTableName +" not have primaryKey");
			}
			
			selectSqlWithIdTemp.append(" where ").append(primaryKey).append(" = ?");
			
			this.selectSqlWithId = selectSqlWithIdTemp.toString();
		}
		
		return selectSqlWithId;
	}

	public String getDeleteSqlWithId() {
		if(deleteSqlWithId == null){
			StringBuilder deleteSqlWithId = new StringBuilder(getDeleteAllSql());
			
			String  primaryKey=  getPrimaryKey();
			if(primaryKey == null){
				throw new PrimaryKeyException("table name "+this.dbTableName +" not have primaryKey");
			}
			
			deleteSqlWithId.append(" where ").append(primaryKey).append(" = ?");
			
			this.deleteSqlWithId = deleteSqlWithId.toString();
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
