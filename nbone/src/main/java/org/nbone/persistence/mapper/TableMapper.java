package org.nbone.persistence.mapper;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

/**
 * 描述java对象的数据库映射信息（数据库表的映射、字段的映射）<br>
 * 
 * @author thinking
 * @since 2015-12-12
 * 
 */
public class TableMapper<E> {
    /**
     * 当使用注解定义实体Bean时使用
     */
    private Annotation tableMapperAnnotation;
    /**
     * 数据库表主键类型：（单个字段的唯一键）（几个字段组合起来的唯一键）
     */
    private String[] primaryKeys;
    
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
    private Class<E> entityClazz;
    
    /**
     * 以数据库字段为Key 
     */
    private Map<String, FieldMapper> fieldMapperCache;
    /**
     * 数据库表字段映射列表
     */
    private List<FieldMapper> fieldMapperList;
    
    

	public TableMapper(Class<E> entityClazz) {
		this.entityClazz = entityClazz;
		this.entityName = entityClazz.getName();
	}

	
	
	public Annotation getTableMapperAnnotation() {
		return tableMapperAnnotation;
	}

	public void setTableMapperAnnotation(Annotation tableMapperAnnotation) {
		this.tableMapperAnnotation = tableMapperAnnotation;
	}

	public String[] getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(String[] primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public String getEntityName() {
		return entityName;
	}

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
		}
		
		return dbTableName;
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}

	public Class<E> getEntityClazz() {
		return entityClazz;
	}

	public void setEntityClazz(Class<E> entityClazz) {
		this.entityClazz = entityClazz;
	}

	public Map<String, FieldMapper> getFieldMapperCache() {
		return fieldMapperCache;
	}

	public void setFieldMapperCache(Map<String, FieldMapper> fieldMapperCache) {
		this.fieldMapperCache = fieldMapperCache;
	}

	public List<FieldMapper> getFieldMapperList() {
		return fieldMapperList;
	}

	public void setFieldMapperList(List<FieldMapper> fieldMapperList) {
		this.fieldMapperList = fieldMapperList;
	}
    
    
    

   

}
