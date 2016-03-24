package org.nbone.persistence;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.nbone.framework.mybatis.util.MyMapperUtils;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.TableMapper;
import org.nbone.persistence.model.SqlModel;

/**
 * 根据JPA注解构建sql
 * @author thinking
 * @since 2015-12-12
 * @see javax.persistence.Entity
 * @see javax.persistence.Table
 * @see javax.persistence.Id
 * @see javax.persistence.Column
 *
 */
public abstract class SqlBuilderBase implements SqlBuilder {
	
	public final static int oxm = 1;
	public final static int annotation = 2;
	
	public String placeholderPrefixMybatis = "#{";
	public String placeholderSuffixMybatis = "}";
	
	public final static String tableNameIsNullmsg = "table name  must not is null. --thinking";
	public final static String primaryKeyIsNullmsg = "primary Keys must not is null. --thinking";
	
	
	 /**
     * 缓存TableMapper
     */
    private static HashMap<Class<?>, TableMapper> tableMapperCache = new HashMap<Class<?>, TableMapper>(128);

    /**
     * 由传入的dto对象的class构建TableMapper对象，构建好的对象存入缓存中，以后使用时直接从缓存中获取
     * @param <E>
     * 
     * @param dtoClass
     * @return TableMapper
     */
    public static <E> TableMapper<E> buildTableMapper(Class<E> dtoClass) {

        Map<String, FieldMapper> fieldMapperCache = null;
        List<FieldMapper> fieldMapperList = null;
        Field[] fields = null;

        
        FieldMapper fieldMapper = null;
        TableMapper<E> tableMapper = null;
        synchronized (tableMapperCache) {
            tableMapper = tableMapperCache.get(dtoClass);
            if (tableMapper != null) {
                return tableMapper;
            }
            
            
            tableMapper = new TableMapper<E>(dtoClass);
            //table Entity mapper
            Annotation[] classAnnotations = dtoClass.getDeclaredAnnotations();
            if (classAnnotations.length == 0) {
                throw new RuntimeException("Class " + dtoClass.getName()
                        + " has no annotation, I can't build 'TableMapper' for it.");
            }
            
            if(dtoClass.isAnnotationPresent(Table.class)){
            	Table an  = dtoClass.getAnnotation(Table.class);
            	tableMapper.setTableMapperAnnotation(an);
            	tableMapper.setDbTableName(an.name());
            	
            }
            if (tableMapper.getTableMapperAnnotation() == null) {
                throw new RuntimeException("Class " + dtoClass.getName() + " has no 'TableMapperAnnotation', "
                        + "which has the database table information," + " I can't build 'TableMapper' for it.");
            }
            
            //Column Field mapper
            fields = dtoClass.getDeclaredFields();
            fieldMapperCache = new HashMap<String, FieldMapper>();
            fieldMapperList = new ArrayList<FieldMapper>();
            Annotation[] fieldAnnotations = null;
            List<String> primaryKeys = new ArrayList<String>(1);
            for (Field field : fields) {
                fieldAnnotations = field.getDeclaredAnnotations();
                if (fieldAnnotations.length == 0) {
                    continue;
                }
               //primary key
               //List<String> primary;
                //目前只支持单一主键,不支持联合主键
                if(fieldAnnotations.length >= 2){
                	
                    for (Annotation an : fieldAnnotations) {
                    	int count = 0;
                    	String dbFieldName = null ;
                    	if (an instanceof Id) {
                    		Id primaryid = (Id) an;
                    		count ++;
                    		
                        }
                    	else if(an instanceof Column){
                    		Column fieldColumn = (Column) an;
                    		dbFieldName = fieldColumn.name();
                    		count ++;
                    		
                    	}
                    	if(count == 2){
                    		primaryKeys.add(dbFieldName);
                    		//XXX：break thinking
                    		break;
                    	}
                    }
                	
                }
                String[] primaryKeysStr = new String[primaryKeys.size()];
                primaryKeysStr = primaryKeys.toArray(primaryKeysStr);
                tableMapper.setPrimaryKeys(primaryKeysStr);
                
                for (Annotation an : fieldAnnotations) {
                	
                	if (an instanceof Column) {
                		Column  fieldMapperAnnotation = (Column) an;
                        fieldMapper = new FieldMapper();
                        fieldMapper.setFieldName(field.getName());
                        
                        String dbFieldName = fieldMapperAnnotation.name();
                        fieldMapper.setDbFieldName(dbFieldName);
                        
                        for (String string : primaryKeysStr) {
                        	if(string != null && string.equals(dbFieldName)){
                        		fieldMapper.setPrimaryKey(true);
                        		break;
                        	}
						}
                        //fieldMapper.setJdbcType(fieldMapperAnnotation.jdbcType());
                        fieldMapperCache.put(dbFieldName, fieldMapper);
                        fieldMapperList.add(fieldMapper);
                        
                        //XXX：break thinking
                        break;
                    }
                }
            }
            tableMapper.setFieldMapperCache(fieldMapperCache);
            tableMapper.setFieldMapperList(fieldMapperList);
            tableMapperCache.put(dtoClass, tableMapper);
            return tableMapper;
        }
    }
    
    public static <E> TableMapper<E> buildTableMapper(Class<E> dtoClass,String id) {
    	TableMapper<E> tableMapper;
    	synchronized(tableMapperCache){
    		//get cache 
    		 tableMapper  = tableMapperCache.get(dtoClass);
             if (tableMapper != null) {
                 return tableMapper;
             }
             //load 
             tableMapper =  MyMapperUtils.resultMap2TableMapper(dtoClass, id);
             tableMapperCache.put(dtoClass, tableMapper);
    		
    	}
		return tableMapper;
    }
    public static <E> TableMapper<E> buildTablexMapper(Class<E> dtoClass) {
    	return buildTableMapper(dtoClass, "");
    }
    
}
