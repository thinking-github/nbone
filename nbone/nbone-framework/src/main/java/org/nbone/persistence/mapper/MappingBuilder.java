package org.nbone.persistence.mapper;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.nbone.framework.spring.dao.core.EntityPropertyRowMapper;
import org.nbone.persistence.annotation.CreatedTime;
import org.nbone.persistence.annotation.UpdateTime;
import org.nbone.persistence.mapper.EntityMapper.ExcludeTransientFieldFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.persistence.*;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Builder Object Relational Mapping
 * @author thinking
 * @version 1.0 
 */
@SuppressWarnings("unchecked")
public class MappingBuilder {

    private static final String HIBERNATE_ANNOTATIONS_CLASS = "org.hibernate.annotations.CreationTimestamp";
    private static final boolean hibernatePresent = ClassUtils.isPresent(HIBERNATE_ANNOTATIONS_CLASS,
            MappingBuilder.class.getClassLoader());
	/**
     * 缓存TableMapper
     */
    private  Map<Class<?>, EntityMapper<?>> entityMapperCache = new ConcurrentHashMap<Class<?>, EntityMapper<?>>(32);
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
     * @return map
     */
    public synchronized Map<Class<?>, EntityMapper<?>>  getEntityMappers() {
		return new HashMap<Class<?>, EntityMapper<?>>(entityMapperCache);
	}
    
    
    public <E> EntityMapper<E> getTableMapper(Class<E> entityClass) {
    	EntityMapper<E> tm = (EntityMapper<E>) entityMapperCache.get(entityClass);
    	if(tm == null){
    		tm = buildEntityMapper(entityClass);
    	}
    	
		return tm;
	}
    
    /**是否已经映射
     * @param entityClass  entityClass to mapping
     * @return  true false
     */
	public <E> boolean isEntityMapped(Class<E> entityClass) {
		return entityMapperCache.containsKey(entityClass);
	}

    public <E> EntityMapper<E> getEntityCache(Class<E> entityClass) {
        EntityMapper<E> tm = (EntityMapper<E>) entityMapperCache.get(entityClass);
        return tm;
    }
	
	public <E> MappingBuilder addEntityMapper(Class<E> entityClass, EntityMapper<E> entityMapper) {
        entityMapperCache.put(entityClass, entityMapper);
    	
		return this;
	}

	/**
     * 由传入的entity对象的class构建TableMapper对象，构建好的对象存入缓存中，以后使用时直接从缓存中获取
     * @param <E>
     * 
     * @param entityClass entityClass to mapping
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
            Map<String,Column> columnOverride = buildColumnOverride(entityClass);
            //serialVersionUID
            ReflectionUtils.doWithFields(entityClass, new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    String fieldName = field.getName();
                    PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(entityClass, fieldName);

                    FieldMapper fieldMapper = new FieldMapper(fieldName, field.getType(), pd);
                    Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
                    FieldMapper.setFieldProperty(field, fieldMapper);
                    Column column = null;
                    if (columnOverride != null) {
                        column = columnOverride.get(fieldName);
                    }
                    if (column == null) {
                        column = field.getAnnotation(Column.class);
                    }
                    String dbFieldName;
                    if (column != null) {
                        dbFieldName = column.name();
                    } else {
                        dbFieldName = fieldName;
                    }
                    fieldMapper.setColumnName(dbFieldName);
                    //primary key
                    //List<String> primary;
                    //主键设置
                    if (field.isAnnotationPresent(Id.class)) {
                        fieldMapper.setPrimaryKey(true);
                        primaryKeys.add(fieldMapper);
                    } else if (isCreatedTime(field)) {
                        entityMapper.setCreateTime(fieldMapper);
                    } else if (isUpdateTime(field)) {
                        entityMapper.setUpdateTime(fieldMapper);
                    }
                    
                    if (isVersion(field)) {
                        entityMapper.setVersion(fieldMapper);
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


    public static boolean isCreatedTime(Field field) {
        if (field.isAnnotationPresent(CreatedTime.class)) {
            return true;
        }
        if (field.isAnnotationPresent(CreatedDate.class)) {
            return true;
        }
        if (hibernatePresent && field.isAnnotationPresent(CreationTimestamp.class)) {
            return true;
        }
        return false;
    }

    public static boolean isUpdateTime(Field field) {
        if (field.isAnnotationPresent(UpdateTime.class)) {
            return true;
        }
        if (field.isAnnotationPresent(LastModifiedDate.class)) {
            return true;
        }
        if (hibernatePresent && field.isAnnotationPresent(UpdateTimestamp.class)) {
            return true;
        }
        return false;
    }

    public static boolean isVersion(Field field) {
        if (field.isAnnotationPresent(Version.class)) {
            return true;
        }
        return false;
    }

    public static <T> Map<String, Column> buildColumnOverride(Class<T> entityClass) {
        boolean attributeOverride = entityClass.isAnnotationPresent(AttributeOverride.class)
                || entityClass.isAnnotationPresent(AttributeOverrides.class);
        if (attributeOverride) {
            Map<String, Column> columnOverride = new HashMap<String, Column>();
            AttributeOverride singleOverride = entityClass.getAnnotation(AttributeOverride.class);
            AttributeOverrides multipleOverrides = entityClass.getAnnotation(AttributeOverrides.class);
            AttributeOverride[] overrides;
            if (singleOverride != null) {
                overrides = new AttributeOverride[]{singleOverride};
            } else if (multipleOverrides != null) {
                overrides = multipleOverrides.value();
            } else {
                overrides = null;
            }

            //fill overridden columns
            if (overrides != null) {
                for (AttributeOverride depAttr : overrides) {
                    columnOverride.put(depAttr.name(), depAttr.column());
                }
            }
            return columnOverride;
        }
        return null;
    }
    
}
