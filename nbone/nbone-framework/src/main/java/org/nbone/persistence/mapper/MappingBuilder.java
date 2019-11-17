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

import org.nbone.framework.spring.dao.core.EntityPropertyRowMapper;
import org.nbone.persistence.mapper.EntityMapper.ExcludeTransientFieldFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

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
    private  Map<Class<?>, EntityMapper<? extends Object>> entityMapperCache = new ConcurrentHashMap<Class<?>, EntityMapper<? extends Object>>(32);
    /**
     * 全局应用程序实例
     */
    public final static MappingBuilder ME = new MappingBuilder();

    /**
     *  @see javax.persistence.Transient
     */
    private static ExcludeTransientFieldFilter excludeFieldFilter = new ExcludeTransientFieldFilter();
    
    
    /**
     * 返回缓存副本
     * @return
     */
    public synchronized Map<Class<?>, EntityMapper<? extends Object>>  getTableMappers() {
		return new HashMap<Class<?>, EntityMapper<? extends Object>>(entityMapperCache);
	}
    
    
    public <E> EntityMapper<E> getTableMapper(Class<E> entityClass) {
    	EntityMapper<E> tm = (EntityMapper<E>) entityMapperCache.get(entityClass);
    	if(tm == null){
    		tm = buildEntityMapper(entityClass);
    	}
    	
		return tm;
	}
    
    /**是否已经映射
     * @param entityClass
     * @return 
     */
	public <E> boolean isTableMappered(Class<E> entityClass) {
    	EntityMapper<E> tm = (EntityMapper<E>) entityMapperCache.get(entityClass);
    	
		return tm == null ? false : true;
	}
	
	public <E> MappingBuilder addTableMapper(Class<E> entityClass, EntityMapper<E> entityMapper) {
        entityMapperCache.put(entityClass, entityMapper);
    	
		return this;
	}

	/**
     * 由传入的entity对象的class构建TableMapper对象，构建好的对象存入缓存中，以后使用时直接从缓存中获取
     * @param <E>
     * 
     * @param entityClass
     * @return TableMapper
     */
    public  <E> EntityMapper<E> buildEntityMapper(Class<E> entityClass) {

        synchronized (entityMapperCache) {
            //Column Field mapper
            Field[] fields = entityClass.getDeclaredFields();
            EntityMapper<E> entityMapper = new EntityMapper<E>(entityClass, fields.length);
            //table Entity mapper
            Table table = entityClass.getAnnotation(Table.class);
            if (table != null) {
                entityMapper.setTableAnnotation(table);
            }
            String tableName = EntityMapper.getTableName(entityClass);
            entityMapper.setTableName(tableName);

            List<FieldMapper> primaryKeys = new ArrayList<FieldMapper>(1);
            //serialVersionUID
            ReflectionUtils.doWithFields(entityClass, new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    String fieldName = field.getName();
                    PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(entityClass, fieldName);

                    FieldMapper fieldMapper = new FieldMapper(fieldName, field.getType(), pd);
                    Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
                    FieldMapper.setFieldProperty(field, fieldMapper);

                    Column column = field.getAnnotation(Column.class);
                    String dbFieldName;
                    if (column != null) {
                        dbFieldName = column.name();
                    } else {
                        dbFieldName = fieldName;
                    }
                    fieldMapper.setDbFieldName(dbFieldName);
                    //primary key
                    //List<String> primary;
                    //主键设置
                    if (field.isAnnotationPresent(Id.class)) {
                        fieldMapper.setPrimaryKey(true);
                        primaryKeys.add(fieldMapper);
                    }
                    //fieldMapper.setJdbcType(fieldMapperAnnotation.jdbcType());

                    entityMapper.addFieldMapper(fieldMapper.getDbFieldName(), fieldMapper);
                }

            },excludeFieldFilter);

            entityMapper.setPrimaryKeyFields(primaryKeys);
            entityMapper.setFieldPropertyLoad(true);
            //Spring Jdbc
            RowMapper<E> rowMapper = new EntityPropertyRowMapper<E>(entityMapper);
            entityMapper.setRowMapper(rowMapper);

            entityMapperCache.put(entityClass, entityMapper);
            return entityMapper;
        }
    }

    public static  <E> EntityMapper<E> getEntityMapper(Class<E> entityClass) {
    	return ME.getTableMapper(entityClass);
    }
    
    
    
}
