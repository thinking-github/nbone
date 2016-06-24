package org.nbone.framework.spring.dao.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nbone.framework.spring.dao.core.EntityPropertySqlParameterSource;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

/**
 * <p> spring TableMetaDataContext 方便但效率不高故重新实现 TableMetaDataContext
 * 
 * @author thinking
 * @version 1.0 
 * @see org.springframework.jdbc.core.metadata.TableMetaDataContext
 * @since spring 2.5
 * 
 */
public class EntityTableMetaDataContext extends TableMetaDataContext {

	
	
	@Override
	public List<Object> matchInParameterValuesWithInsertColumns(SqlParameterSource parameterSource) {
		/**
		 * 优先使用 EntityPropertySqlParameterSource
		 */
		if(parameterSource instanceof EntityPropertySqlParameterSource){
			List<Object> values = new ArrayList<Object>();
			for (String column : this.getTableColumns()) {
				if (parameterSource.hasValue(column)) {
					values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, column));
				}else{
					values.add(null);
				}
			}
			return values;
		}
		
		
		return super.matchInParameterValuesWithInsertColumns(parameterSource);
	}

	
	@Override
	public List<Object> matchInParameterValuesWithInsertColumns(Map<String, Object> inParameters) {
		return super.matchInParameterValuesWithInsertColumns(inParameters);
	}

	
	
	
}
