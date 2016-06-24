package org.nbone.framework.spring.dao.namedparam;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nbone.framework.spring.dao.BaseJdbcDao;
import org.nbone.framework.spring.dao.core.CachedEntityPropertyRowMapper;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.SqlBuilder;
import org.nbone.persistence.SqlSession;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.model.SqlModel;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author thinking
 * @version 1.0 
 * @see org.nbone.framework.spring.dao.BaseJdbcDao
 * @see org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
 */
@Repository("namedJdbcDao")
@Primary
@Lazy
public class NamedJdbcDao implements SqlSession,InitializingBean{
	
	@Resource(name="baseJdbcDao")
	private BaseJdbcDao baseJdbcDao;
	
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	
	
	private SqlBuilder sqlBuilder = new BaseSqlBuilder(JdbcFrameWork.SPRING_JDBC) {
		
	}; 
	
	@Override
	public void afterPropertiesSet() throws Exception {
		JdbcTemplate jdbcTemplate =  baseJdbcDao.getJdbcTemplate();
		this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}
	

	@Override
	public int insert(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.buildInsertSql(object);
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		return jdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public Serializable save(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.buildInsertSql(object);
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		KeyHolder generatedKeyHolder =  new  GeneratedKeyHolder();
		jdbcTemplate.update(sqlModel.getSql(), paramSource, generatedKeyHolder);
		return generatedKeyHolder.getKey();
	}

	@Override
	public Object add(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.buildInsertSql(object);
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		KeyHolder generatedKeyHolder =  new  GeneratedKeyHolder();
		jdbcTemplate.update(sqlModel.getSql(), paramSource, generatedKeyHolder);
		Number num = generatedKeyHolder.getKey();
		
		String[] primaryKeys = sqlModel.getPrimaryKeys();
		if(primaryKeys != null && primaryKeys.length > 0){
			BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
			beanWrapper.setPropertyValue(primaryKeys[0], num);
		}
		
		return object;
	}

	@Override
	public int update(Object object) {
		
		SqlModel<Object> sqlModel = sqlBuilder.buildUpdateSql(object);
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return jdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public int updateSelective(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.buildUpdateSql(object);
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return jdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public void saveOrUpdate(Object object) {
		
	}

	@Override
	public int delete(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.buildDeleteSql(object);
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return jdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public int delete(Class<?> clazz, Serializable id) {
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.buildDeleteSqlById(clazz, id);
		int row = 0;
		if(SqlModel.checkSqlModel(sqlModel)){
			row = jdbcTemplate.update(sqlModel.getSql(),sqlModel.getParameter());
		}
		
		return row;
	}
	@Override
	@SuppressWarnings({ "unchecked" })
	public <T> T get(Class<T> clazz, Serializable id) {
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.buildSelectSqlById(clazz, id);
		
		if(SqlModel.checkSqlModel(sqlModel)){
			//RowMapper<T> rowMapper = DbMappingBuilder.ME.getTableMapper(clazz).getRowMapper();
			
			RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
			List<T> list = jdbcTemplate.query(sqlModel.getSql(), sqlModel.getParameter(),rowMapper);
			int row ;
			if(list != null && (row = list.size()) > 0){
				if(row > 1){
					logger.warn("Primary Key query result return multiple lines ["+row+"].thinking");
				}
				return list.get(0);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Object object){
		SqlModel<Object> sqlModel = sqlBuilder.buildSelectSqlById(object);
		if(SqlModel.checkSqlModel(sqlModel)){
			
			RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
			List<T> list = jdbcTemplate.query(sqlModel.getSql(),new BeanPropertySqlParameterSource(object),rowMapper);
			int row ;
			if(list != null && (row = list.size()) > 0){
				if(row > 1){
					logger.warn("Primary Key query result return multiple lines ["+row+"].thinking");
				}
				return list.get(0);
			}
		}
		return null;
	}
	
	
	
	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		return null;
	}

	@Override
	public List<?> getForList(Object object) {
		return null;
	}

	@Override
	public List<?> queryForList(Object object) {
		return null;
	}

	@Override
	public List<?> findForList(Object object) {
		return null;
	}


	
}
