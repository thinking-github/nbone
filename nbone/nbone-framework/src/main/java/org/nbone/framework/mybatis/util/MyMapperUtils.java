package org.nbone.framework.mybatis.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.CaseFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.nbone.constants.CaseName;
import org.nbone.framework.mybatis.MySqlSessionTemplate;
import org.nbone.framework.spring.dao.config.JdbcComponentConfig;
import org.nbone.framework.spring.dao.core.EntityPropertyRowMapper;
import org.nbone.framework.spring.support.ComponentFactory;
import org.nbone.persistence.JdbcOptions;
import org.nbone.persistence.annotation.QueryOperation;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.MapperUtils;
import org.nbone.persistence.mapper.EntityMapper;
import org.nbone.persistence.util.JpaAnnotationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.ReflectionUtils;

/**
 * @author thinking
 * @version 1.0
 */
public class MyMapperUtils {
	public static Log logger  =  LogFactory.getLog(MyMapperUtils.class);
	static Configuration configuration;

	static CaseName caseName;


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

		//java.util.Map map = ComponentFactory.getContext().getBeansOfType(JdbcComponentConfig.class);

		JdbcComponentConfig componentConfig = ComponentFactory.getBean(JdbcComponentConfig.class);

		caseName = componentConfig.getDbCaseName();
	
	}

	/**
	 *  UPPER_CAMEL  --> LOWER_UNDERSCORE / UPPER_UNDERSCORE / LOWER_CAMEL
	 * @param name
	 * @param caseName
	 * @return
	 */
	public static String caseFormat(String name ,CaseName caseName){
		String tableName ;
		switch (caseName) {
			case LOWER_UNDERSCORE:
				tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);

				break;

			case UPPER_UNDERSCORE:
				tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name);

				break;

			case LOWER_CAMEL:
				tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
			
				break;


			default:
				tableName = name;
				break;
		}

		return  tableName;

	}

	/**
	 * 获取全部有效的 resultMap
 	 */
	public static  List<ResultMapping> getResultMappings(){
		List resultMaps = new ArrayList();
		for (Object resultMap : configuration.getResultMaps()) {
			if(resultMap instanceof  ResultMap && ((ResultMap)resultMap).getResultMappings().size() > 0){
				ResultMap mapping = (ResultMap) resultMap;
				if(mapping.getResultMappings().size() == mapping.getIdResultMappings().size()){
					//没有设置主键
					logger.warn("表结构信息没有设置主键");
				}
				resultMaps.add(resultMap);
			}
		}
		return resultMaps;
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
	 * 	sqlMapping 中需添加   <sql id="tableName">ts_project</sql><br/>
	 * 	使用顺序,优先使用配置文件，JPA次之,类名最次之。
	 * @param namespace
	 * @param entityClass
	 * @return
	 */
	public static String getTableName(String namespace, Class<?> entityClass) {
		try {
			XNode xNode = configuration.getSqlFragments().get(namespace+".tableName");
			if(xNode != null){
				return xNode.getStringBody();
			}
		} catch (Exception e) {
			logger.warn(">>>>>"+namespace+".tableName"+" not setting.");

			String tableName  = JpaAnnotationUtils.getTableName(entityClass);

			if(tableName == null || tableName.length() == 0){
				logger.warn(">>>>>"+entityClass.getName()+" Jpa Annotation tableName"+" not setting, use className format.");
				String className = entityClass.getSimpleName();
				tableName = caseFormat(className,caseName);
			}

			return  tableName;

		}

		return null;
	}

	/**
	 *
	 * @param entityClass 可为空，当为空时使用 ResultMap type
	 * @param namespace
	 * @param id
	 * @param <E>
	 * @return
	 */
	public static <E> EntityMapper<E> resultMap2TableMapper(Class<E> entityClass, String namespace, String id) {
		ResultMap resultMap = getResultMap(namespace,id);
		if(entityClass == null){
			entityClass = (Class<E>) resultMap.getType();
		}
		String tableName  = getTableName(namespace,entityClass);
		
		if(tableName == null){
			//logger.warn("mybatis resultMap not has table name mapping.--thinking");
			logger.error(entityClass.getName() +" tableName is null.");
		}

		List<ResultMapping> idsResultMapping  = resultMap.getIdResultMappings();
		List<ResultMapping>  attrsResultMapping =   resultMap.getResultMappings();
		
		EntityMapper<E> entityMapper = new EntityMapper<E>(entityClass,attrsResultMapping.size());
		
		//primary key
		List<String> primaryList = new ArrayList<String>(1);
		for (ResultMapping resultMapping : idsResultMapping) {
			String dbFieldName  = resultMapping.getColumn();
			primaryList.add(dbFieldName);
		}
		if(primaryList.size() >2){
			logger.warn(">>>>>联合主键数量超过2个,请注意检查Class: "+entityClass.getName());
		}
		entityMapper.setTableName(tableName);
		entityMapper.setSelectStar(JdbcOptions.ENABLE_STAR);
		
		//field 
		for (ResultMapping resultMapping : attrsResultMapping) {
			String fieldName  = resultMapping.getProperty();
			String dbFieldName  = resultMapping.getColumn();
			Class<?> javaType = resultMapping.getJavaType();
			
			PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(entityClass, fieldName);
			if(propertyDescriptor == null){
				throw new NullPointerException(fieldName + " property is not exist.");

			}
			FieldMapper fieldMapper = new FieldMapper(fieldName, javaType, propertyDescriptor);
			Field field = ReflectionUtils.findField(entityClass, fieldName);
			FieldMapper.setFieldProperty(field, fieldMapper);

			fieldMapper.setDbFieldName(dbFieldName);
			for (String string : primaryList) {
				if(string != null && string.equals(dbFieldName)){
            		fieldMapper.setPrimaryKey(true);
            		break;
            	}
			}
			entityMapper.addFieldMapper(dbFieldName, fieldMapper);
		}

		Map<String, QueryOperation> extFields = MapperUtils.getExtFieldsMap(entityClass);
		entityMapper.setExtFields(extFields);

		  //Spring Jdbc
        RowMapper<E> rowMapper = new EntityPropertyRowMapper<E>(entityMapper);
        entityMapper.setRowMapper(rowMapper);
        
        entityMapper.setFieldPropertyLoad(true);
		
		return entityMapper;
	}

	public static <E> EntityMapper<E> resultMap2TableMapper(String namespace, String id) {
		return  resultMap2TableMapper(null,namespace,id);
	}

}
