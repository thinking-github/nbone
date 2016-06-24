package org.nbone.framework.mybatis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.nbone.framework.mybatis.MySqlSessionTemplate;
import org.nbone.framework.spring.support.ComponentFactory;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.TableMapper;

/**
 * 
 * @author thinking
 *
 */
public class MyMapperUtils {
	public static Log logger  =  LogFactory.getLog(MyMapperUtils.class);
	static MySqlSessionTemplate mySqlSessionTemplate = ComponentFactory.getBean(MySqlSessionTemplate.class);
	
	
	public static  ResultMap getResultMap(String id) {
		return mySqlSessionTemplate.getConfiguration().getResultMap(id);
	}
	
	public static <E> TableMapper<E> resultMap2TableMapper(Class<E> entityClass,String id) {
		ResultMap resultMap = getResultMap(id);
		Class<?>  type  = resultMap.getType();
		List<ResultMapping> idsResultMapping  = resultMap.getIdResultMappings();
		List<ResultMapping>  attrsResultMapping =   resultMap.getResultMappings();
		
		TableMapper<E> tableMapper = new TableMapper<E>(entityClass);
		
		logger.warn("mybatis resultMap not has table name mapping.--thinking");
		//primary key
		List<String> primaryList = new ArrayList<String>(1);
		for (ResultMapping resultMapping : idsResultMapping) {
			String fieldName  = resultMapping.getProperty();
			String dbFieldName  = resultMapping.getColumn();
			primaryList.add(dbFieldName);
			
		}
		String[] primaryKeys = new String[primaryList.size()];
		primaryKeys = primaryList.toArray(primaryKeys);
		tableMapper.setPrimaryKeys(primaryKeys);
		//field 
		for (ResultMapping resultMapping : attrsResultMapping) {
			FieldMapper fieldMapper = new FieldMapper(null);
			String fieldName  = resultMapping.getProperty();
			String dbFieldName  = resultMapping.getColumn();
			fieldMapper.setFieldName(fieldName);
			fieldMapper.setDbFieldName(dbFieldName);
				for (String string : primaryKeys) {
					if(string != null && string.equals(dbFieldName)){
                		fieldMapper.setPrimaryKey(true);
                		break;
                	}
				}
				tableMapper.addFieldMapper(dbFieldName, fieldMapper);
		}
		
		return tableMapper;
	}
	
	
	
	
	
	
	

}
