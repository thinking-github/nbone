package org.nbone.framework.mybatis.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.nbone.framework.mybatis.MySqlSessionTemplate;
import org.nbone.framework.spring.dao.core.EntityPropertyRowMapper;
import org.nbone.framework.spring.support.ComponentFactory;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.TableMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author thinking
 * @version 1.0
 */
public class MyMapperUtils {
	public static Log logger  =  LogFactory.getLog(MyMapperUtils.class);
	static Configuration configuration; 
	static{
		try {
			 MySqlSessionTemplate	mySqlSessionTemplate = ComponentFactory.getBean(MySqlSessionTemplate.class);
			 configuration = mySqlSessionTemplate.getConfiguration();
		} catch (Exception e) {
			try {
				SqlSessionFactory	sessionFactory = ComponentFactory.getBean(SqlSessionFactory.class);
				configuration = sessionFactory.getConfiguration();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		
			
		}
	
	}
	
	public static  ResultMap getResultMap(String namespace,String id) {
		StringBuilder fullId = new StringBuilder();
		if(namespace != null){
			fullId.append(namespace).append(".").append(id);
		}else{
			fullId.append(id);
		}
		return configuration.getResultMap(fullId.toString());
	}
	/**
	 * 	sqlMapping 中需添加   <sql id="tableName">ts_project</sql>
	 * @param namespace
	 * @return
	 */
	public static String getTableName(String namespace) {
		XNode xNode = configuration.getSqlFragments().get(namespace+".tableName");
		if(xNode != null){
			return xNode.getStringBody();
		}
		return null;
	}

	
	public static <E> TableMapper<E> resultMap2TableMapper(Class<E> entityClass,String namespace,String id) {
		String tableName  = getTableName(namespace);
		
		if(tableName == null){
			//logger.warn("mybatis resultMap not has table name mapping.--thinking");
			logger.error(entityClass.getName() +" tableName is null.");
		}
		ResultMap resultMap = getResultMap(namespace,id);
		Class<?>  type  = resultMap.getType();
		List<ResultMapping> idsResultMapping  = resultMap.getIdResultMappings();
		List<ResultMapping>  attrsResultMapping =   resultMap.getResultMappings();
		
		TableMapper<E> tableMapper = new TableMapper<E>(entityClass,attrsResultMapping.size());
		
		//primary key
		List<String> primaryList = new ArrayList<String>(1);
		for (ResultMapping resultMapping : idsResultMapping) {
			String dbFieldName  = resultMapping.getColumn();
			primaryList.add(dbFieldName);
		}
		tableMapper.setDbTableName(tableName);
		tableMapper.setPrimaryKeys(primaryList);
		
		//field 
		for (ResultMapping resultMapping : attrsResultMapping) {
			String fieldName  = resultMapping.getProperty();
			String dbFieldName  = resultMapping.getColumn();
			Class<?> javaType = resultMapping.getJavaType();
			
			PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(entityClass, fieldName);
			FieldMapper fieldMapper = new FieldMapper(propertyDescriptor);
			Field field;
			try {
				field = entityClass.getDeclaredField(fieldName);
				FieldMapper.setFieldProperty(field, fieldMapper);
			} catch (NoSuchFieldException e) {
				logger.error(e.getMessage(), e);
			} catch (SecurityException e) {
				logger.error(e.getMessage(), e);
			}
			
			fieldMapper.setFieldName(fieldName);
			fieldMapper.setDbFieldName(dbFieldName);
			fieldMapper.setPropertyType(javaType);
			
			for (String string : primaryList) {
				if(string != null && string.equals(dbFieldName)){
            		fieldMapper.setPrimaryKey(true);
            		break;
            	}
			}
			tableMapper.addFieldMapper(dbFieldName, fieldMapper);
		}
		  //Spring Jdbc
        RowMapper<E> rowMapper = new EntityPropertyRowMapper<E>(tableMapper);
        tableMapper.setRowMapper(rowMapper);
        
        tableMapper.setFieldPropertyLoad(true);
		
		return tableMapper;
	}
	
	
	
	
	
	
	

}
