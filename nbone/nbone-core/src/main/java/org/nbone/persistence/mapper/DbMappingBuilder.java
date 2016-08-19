package org.nbone.persistence.mapper;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.nbone.framework.spring.dao.core.EntityPropertyRowMapper;
import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.annotation.FieldProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
/**
 * Builder Object Relational Mapping
 * @author thinking
 * @version 1.0 
 */
@SuppressWarnings("unchecked")
public class DbMappingBuilder {
	
	/**
     * 缓存TableMapper
     */
    private  Map<Class<?>, TableMapper<? extends Object>> tableMapperCache = new ConcurrentHashMap<Class<?>, TableMapper<? extends Object>>(32);
    /**
     * 全局应用程序实例
     */
    public final static DbMappingBuilder ME = new DbMappingBuilder();
    
    
    /**
     * 返回缓存副本
     * @return
     */
    public synchronized Map<Class<?>, TableMapper<? extends Object>>  getTableMappers() {
		return new HashMap<Class<?>, TableMapper<? extends Object>>(tableMapperCache);
	}
    
    
    public <E> TableMapper<E> getTableMapper(Class<E> entityClass) {
    	TableMapper<E> tm = (TableMapper<E>) tableMapperCache.get(entityClass);
    	if(tm == null){
    		tm = buildTableMapper(entityClass);
    	}
    	
		return tm;
	}
    
    /**是否已经映射
     * @param entityClass
     * @return 
     */
	public <E> boolean isTableMappered(Class<E> entityClass) {
    	TableMapper<E> tm = (TableMapper<E>) tableMapperCache.get(entityClass);
    	
		return tm == null ? false : true;
	}
	
	public <E> DbMappingBuilder addTableMapper(Class<E> entityClass,TableMapper<E> tableMapper) {
		tableMapperCache.put(entityClass, tableMapper);
    	
		return this;
	}

	/**
     * 由传入的entity对象的class构建TableMapper对象，构建好的对象存入缓存中，以后使用时直接从缓存中获取
     * @param <E>
     * 
     * @param dtoClass
     * @return TableMapper
     */
    public  <E> TableMapper<E> buildTableMapper(Class<E> entityClass) {

        
        TableMapper<E> tableMapper = null;
        synchronized (tableMapperCache) {
            tableMapper = (TableMapper<E>) tableMapperCache.get(entityClass);
            if (tableMapper != null) {
                return tableMapper;
            }
            
            //Column Field mapper
            Field[] fields = entityClass.getDeclaredFields();
            tableMapper = new TableMapper<E>(entityClass,fields.length);
            //table Entity mapper
            Annotation[] classAnnotations = entityClass.getDeclaredAnnotations();
            if (classAnnotations.length == 0) {
                throw new RuntimeException("Class " + entityClass.getName() + " has no annotation, I can't build 'TableMapper' for it.");
            }
            
            if(entityClass.isAnnotationPresent(Table.class) && entityClass.isAnnotationPresent(Entity.class)){
            	Table an  = entityClass.getAnnotation(Table.class);
            	tableMapper.setTableMapperAnnotation(an);
            	tableMapper.setDbTableName(an.name() != null ? an.name() : entityClass.getSimpleName());
            	
            }
            if (tableMapper.getTableMapperAnnotation() == null) {
                throw new RuntimeException("Class " + entityClass.getName() + " has no 'TableMapperAnnotation', "
                        + "which has the database table information," + " I can't build 'TableMapper' for it.");
            }
            
            List<String> primaryKeys = new ArrayList<String>(1);
            for (Field field : fields) {
                PropertyDescriptor pd  = BeanUtils.getPropertyDescriptor(entityClass, field.getName());
                if(pd == null){
                	//eg: serialVersionUID
                	continue;
                }
            	  FieldMapper fieldMapper = new FieldMapper(pd);
                  fieldMapper.setFieldName(field.getName());
                  fieldMapper.setPropertyType(field.getType());
                  Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
                  if (fieldAnnotations.length == 0) {
                      //continue;
                  	
                  }else{
                  	//Transient
                  	if(field.isAnnotationPresent(Transient.class)){
                  		continue;
                  	}
                  	
                  	FieldMapper.setFieldProperty(field, fieldMapper);
                  	
                    Column fieldMapperAnnotation = field.getAnnotation(Column.class);
                    if(fieldMapperAnnotation != null){
                        String dbFieldName = fieldMapperAnnotation.name();
                        fieldMapper.setDbFieldName(dbFieldName);
                        
                        //primary key
                        //List<String> primary;
                         //主键设置
                        if(field.isAnnotationPresent(Id.class)){
                        	fieldMapper.setPrimaryKey(true);
                        	primaryKeys.add(fieldMapper.getDbFieldName());
                        }
                        //fieldMapper.setJdbcType(fieldMapperAnnotation.jdbcType());
                        	
                        }else{
                  	      if(field.isAnnotationPresent(Id.class)){
                            	fieldMapper.setPrimaryKey(true);
                            	primaryKeys.add(fieldMapper.getDbFieldName());
                            }
                      	  
                        }
                  }
                  
                  
                  tableMapper.addFieldMapper(fieldMapper.getDbFieldName(), fieldMapper);
                  tableMapper.addPropertyDescriptor(field.getName(), pd);
                	
              
            }
            
            tableMapper.setPrimaryKeys(primaryKeys);
            tableMapper.setFieldPropertyLoad(true);
            //Spring Jdbc
            RowMapper<E> rowMapper = new EntityPropertyRowMapper<E>(tableMapper);
            tableMapper.setRowMapper(rowMapper);
            
            tableMapperCache.put(entityClass, tableMapper);
            return tableMapper;
        }
    }

    public static  <E> TableMapper<E> getEntityMapper(Class<E> entityClass) {
    	return ME.getTableMapper(entityClass);
    }
    
    
    
}
