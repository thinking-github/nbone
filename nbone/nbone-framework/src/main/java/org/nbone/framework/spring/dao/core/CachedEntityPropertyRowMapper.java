package org.nbone.framework.spring.dao.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * @author thinking
 * @version 1.0 
 * @see EntityPropertyRowMapper
 */
public class CachedEntityPropertyRowMapper {
	
	@SuppressWarnings("rawtypes")
	private Map<Class,RowMapper> cachedEntityPropertyRowMapper =  new ConcurrentHashMap<Class,RowMapper>(32);
	
	
	@SuppressWarnings("unchecked")
	public <T> RowMapper<T> getEntityPropertyRowMapper(Class<T> entityClass){
		RowMapper<T> rowMapper =  cachedEntityPropertyRowMapper.get(entityClass);
		
		if(rowMapper ==  null){
			rowMapper = createEntityPropertyRowMapper(entityClass);
		}
		
		return rowMapper;
	}
	
	
	public <T> CachedEntityPropertyRowMapper addEntityPropertyRowMapper(Class<T> entityClass,RowMapper<T> rowMapper){
		if(entityClass != null && rowMapper != null){
			cachedEntityPropertyRowMapper.put(entityClass, rowMapper);
		}
		return this;
	}
	
	
	private <T> EntityPropertyRowMapper<T> createEntityPropertyRowMapper(Class<T> entityClass){
		EntityPropertyRowMapper<T> rowMapper = new EntityPropertyRowMapper<T>(entityClass);
		cachedEntityPropertyRowMapper.put(entityClass, rowMapper);
		return rowMapper;
	}
	
	
	
	
	

}
