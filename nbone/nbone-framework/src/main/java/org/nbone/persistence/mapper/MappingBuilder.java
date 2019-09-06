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
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
/**
 * Builder Object Relational Mapping
 * @author thinking
 * @version 1.0 
 */
@SuppressWarnings("unchecked")
public class MappingBuilder {
	
	/**
     * 缓存TableMapper
     */
    private  Map<Class<?>, EntityMapper<? extends Object>> tableMapperCache = new ConcurrentHashMap<Class<?>, EntityMapper<? extends Object>>(32);
    /**
     * 全局应用程序实例
     */
    public final static MappingBuilder ME = new MappingBuilder();
    
    
    /**
     * 返回缓存副本
     * @return
     */
    public synchronized Map<Class<?>, EntityMapper<? extends Object>>  getTableMappers() {
		return new HashMap<Class<?>, EntityMapper<? extends Object>>(tableMapperCache);
	}
    
    
    public <E> EntityMapper<E> getTableMapper(Class<E> entityClass) {
    	EntityMapper<E> tm = (EntityMapper<E>) tableMapperCache.get(entityClass);
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
    	EntityMapper<E> tm = (EntityMapper<E>) tableMapperCache.get(entityClass);
    	
		return tm == null ? false : true;
	}
	
	public <E> MappingBuilder addTableMapper(Class<E> entityClass, EntityMapper<E> entityMapper) {
		tableMapperCache.put(entityClass, entityMapper);
    	
		return this;
	}

	/**
     * 由传入的entity对象的class构建TableMapper对象，构建好的对象存入缓存中，以后使用时直接从缓存中获取
     * @param <E>
     * 
     * @param entityClass
     * @return TableMapper
     */
    public  <E> EntityMapper<E> buildTableMapper(Class<E> entityClass) {

        
        EntityMapper<E> entityMapper = null;
        synchronized (tableMapperCache) {
            entityMapper = (EntityMapper<E>) tableMapperCache.get(entityClass);
            if (entityMapper != null) {
                return entityMapper;
            }
            
            //Column Field mapper
            Field[] fields = entityClass.getDeclaredFields();
            entityMapper = new EntityMapper<E>(entityClass,fields.length);
            //table Entity mapper
            Annotation[] classAnnotations = entityClass.getDeclaredAnnotations();
            if (classAnnotations.length == 0) {
                throw new RuntimeException("Class " + entityClass.getName() + " has no annotation, I can't build 'TableMapper' for it.");
            }
            
            if(entityClass.isAnnotationPresent(Table.class) && entityClass.isAnnotationPresent(Entity.class)){
            	Table an  = entityClass.getAnnotation(Table.class);
            	entityMapper.setTableMapperAnnotation(an);
            	entityMapper.setDbTableName(an.name() != null ? an.name() : entityClass.getSimpleName());
            	
            }
            if (entityMapper.getTableMapperAnnotation() == null) {
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
                  
                  
                  entityMapper.addFieldMapper(fieldMapper.getDbFieldName(), fieldMapper);
                  entityMapper.addPropertyDescriptor(field.getName(), pd);
                	
              
            }
            
            entityMapper.setPrimaryKeys(primaryKeys);
            entityMapper.setFieldPropertyLoad(true);
            //Spring Jdbc
            RowMapper<E> rowMapper = new EntityPropertyRowMapper<E>(entityMapper);
            entityMapper.setRowMapper(rowMapper);
            
            tableMapperCache.put(entityClass, entityMapper);
            return entityMapper;
        }
    }

    public static  <E> EntityMapper<E> getEntityMapper(Class<E> entityClass) {
    	return ME.getTableMapper(entityClass);
    }
    
    
    
}
