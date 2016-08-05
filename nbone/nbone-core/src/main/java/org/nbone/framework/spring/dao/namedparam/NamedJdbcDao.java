package org.nbone.framework.spring.dao.namedparam;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nbone.framework.spring.dao.BaseJdbcDao;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.BaseSqlSession;
import org.nbone.persistence.BatchSqlSession;
import org.nbone.persistence.SqlBuilder;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.SqlSession;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.mapper.DbMappingBuilder;
import org.nbone.persistence.mapper.TableMapper;
import org.nbone.persistence.model.SqlModel;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * 批量保存使用SimpleJdbcDao
 * @author thinking
 * @version 1.0 
 * @see org.nbone.framework.spring.dao.BaseJdbcDao
 * @see org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
 */
@Repository("namedJdbcDao")
@Primary
@Lazy
@SuppressWarnings("unchecked")
public class NamedJdbcDao extends BaseSqlSession implements SqlSession,BatchSqlSession,InitializingBean{
	
	@Resource(name="baseJdbcDao")
	private BaseJdbcDao baseJdbcDao;
	
	@Resource(name="simpleJdbcDao")
	private BatchSqlSession simpleJdbcDao;
	
	
	private JdbcTemplate dao;
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private NamedJdbcTemplate namedJdbcTemplate;
	
	
	private SqlBuilder sqlBuilder = new BaseSqlBuilder(JdbcFrameWork.SPRING_JDBC) {
		
	}; 
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.dao =  baseJdbcDao.getJdbcTemplate();
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dao);
		this.namedJdbcTemplate = new NamedJdbcTemplate(dao, sqlBuilder);
	}
	

	@Override
	public int insert(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.buildInsertSelectiveSql(object);
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		return jdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public Serializable save(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.buildInsertSelectiveSql(object);
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		KeyHolder generatedKeyHolder =  new  GeneratedKeyHolder();
		jdbcTemplate.update(sqlModel.getSql(), paramSource, generatedKeyHolder);
		return generatedKeyHolder.getKey();
	}

	@Override
	public Object add(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.buildInsertSelectiveSql(object);
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
		
		SqlModel<Object> sqlModel = sqlBuilder.buildUpdateSelectiveSql(object);
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return jdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public int updateSelective(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.buildUpdateSelectiveSql(object);
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
	public int deleteByEntityParams(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.buildDeleteSqlByEntityParams(object, false);
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return jdbcTemplate.update(sqlModel.getSql(), paramSource);
	}


	@Override
	public int delete(Class<?> clazz, Serializable id) {
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.buildDeleteSqlById(clazz, id);
		int row = 0;
		checkSqlModel(sqlModel);
		
		row = jdbcTemplate.update(sqlModel.getSql(),sqlModel.getParameter());
		
		return row;
	}
	
	@Override
	public <T> int delete(Class<T> clazz, Object[] ids) {
		SqlModel<T> sqlModel = sqlBuilder.buildDeleteSqlByIds(clazz, ids);
		int row = 0;
		checkSqlModel(sqlModel);
		row = dao.update(sqlModel.getSql(),sqlModel.getParameterArray());
		return row;
	}


	@Override
	public <T> T get(Class<T> clazz, Serializable id) {
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.buildSelectSqlById(clazz, id);
		checkSqlModel(sqlModel);
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
		return null;
	}
	
	public <T> T get(Object object){
		SqlModel<Object> sqlModel = sqlBuilder.buildSelectSqlById(object);
		checkSqlModel(sqlModel);
			
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = jdbcTemplate.query(sqlModel.getSql(),new BeanPropertySqlParameterSource(object),rowMapper);
		int row ;
		if(list != null && (row = list.size()) > 0){
			if(row > 1){
				logger.warn("Primary Key query result return multiple lines ["+row+"].thinking");
			}
			return list.get(0);
		}
		return null;
	}
	
	
	
	
	@Override
	public long count(Class<?> clazz) {
		SqlModel<?> sqlModel =  sqlBuilder.buildCountSql(clazz);
		return dao.queryForLong(sqlModel.getSql());
	}


	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		SqlModel<T> sqlModel = sqlBuilder.buildSelectAllSql(clazz);
		checkSqlModel(sqlModel);
		RowMapper<T> rowMapper =    (RowMapper<T>) sqlModel.getRowMapper();
		List<T> result  = dao.query(sqlModel.getSql(),rowMapper);
		return result;
	}
	
	@Override
	public <T> List<T> getAll(Class<T> clazz,Collection<?> ids) {
		return getAll(clazz, ids.toArray());
	}

	public <T> List<T> getAll(Class<T> clazz,Object[] ids){
		SqlModel<T> sqlModel = sqlBuilder.buildSelectSqlByIds(clazz, ids);
		checkSqlModel(sqlModel);
		RowMapper<T> rowMapper =    (RowMapper<T>) sqlModel.getRowMapper();
		List<T> result  = dao.query(sqlModel.getSql(),ids,rowMapper);
		return result;
	}
	@Override
	public <T> List<T> getForList(Object object) {
		
		SqlModel<Object> sqlModel = sqlBuilder.buildSelectSql(object);
		checkSqlModel(sqlModel);
			
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = jdbcTemplate.query(sqlModel.getSql(),new BeanPropertySqlParameterSource(object),rowMapper);
		return list;
	}	

	@Override
	public <T> List<T> queryForList(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.buildSimpleSelectSql(object);
		checkSqlModel(sqlModel);
			
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = jdbcTemplate.query(sqlModel.getSql(),new BeanPropertySqlParameterSource(object),rowMapper);
		return list;
	}
	
	public  <T> List<T> queryForList(Object object,SqlConfig sqlConfig){
		
		SqlModel<Map<String,Object>> sqlModel = sqlBuilder.buildObjectModeSelectSql(object, sqlConfig);
		checkSqlModel(sqlModel);
			
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = jdbcTemplate.query(sqlModel.getSql(),sqlModel.getParameter(),rowMapper);
		return list;
	}

	@Override
	public <T> List<T> findForList(Object object) {
		return null;
	}


	
	/**
	 * 暂时使用simpleJdbcDao的批量更新
	 * @param objects
	 * @return
	 */
	@Override
	public int[] batchInsert(Object[] objects) {
		return simpleJdbcDao.batchInsert(objects);
	}

	@Override
	public int[] batchInsert(Collection<?> objects) {
		return simpleJdbcDao.batchInsert(objects);
	}

	@Override
	public int[] batchUpdate(Object[] objects) {
		//XXX: thinking 共享第一实体的sql
		SqlModel<Object> sqlModel = sqlBuilder.buildUpdateSql(objects[0]);
		checkSqlModel(sqlModel);
			
		SqlParameterSource[] batchArgs = new BeanPropertySqlParameterSource[objects.length];
		for (int i = 0; i < objects.length; i++) {
			batchArgs[i] = new BeanPropertySqlParameterSource(objects[i]);
		}
		int[] rows = jdbcTemplate.batchUpdate(sqlModel.getSql(), batchArgs);
		return rows;
		
	}
	
	@Override
	public int[] batchUpdate(Collection<?> objects) {
		SqlParameterSource[] batchArgs = new BeanPropertySqlParameterSource[objects.size()];
		String sql = null;
		int index = 0;
		for (Object object : objects) {
			if(index == 0 ){
				//XXX: thinking 共享第一实体的sql
				SqlModel<Object> sqlModel = sqlBuilder.buildUpdateSql(object);
				sql = sqlModel.getSql();
			}
			batchArgs[index] = new BeanPropertySqlParameterSource(object);
			
			index ++;
		}
		int[] rows = jdbcTemplate.batchUpdate(sql, batchArgs);
		return rows;
	}

	@Override
	public <T> int[] batchDelete(Class<T> clazz, List<Serializable> ids) {
		//XXX: thinking 共享第一实体的sql
		 TableMapper<T> tableMapper =  DbMappingBuilder.ME.getTableMapper(clazz);
		 String sql  = tableMapper.getDeleteSqlWithId().toString();
		 final List<Serializable> tempids = ids;
		 final int batchSize = ids.size();
		 int[] rows = dao.batchUpdate(sql,new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setObject(1, tempids.get(i));
			}
			
			@Override
			public int getBatchSize() {
				return batchSize;
			}
		});
		return rows;	
	}

	@Override
	public <T> int[] batchDelete(Class<T> clazz, Serializable[] ids) {
		 TableMapper<T> tableMapper =  DbMappingBuilder.ME.getTableMapper(clazz);
		 String sql  = tableMapper.getDeleteSqlWithId().toString();
		 final Serializable[] tempids = ids;
		 final int batchSize = ids.length;
		 int[] rows = dao.batchUpdate(sql,new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setObject(1, tempids[i]);
			}
			
			@Override
			public int getBatchSize() {
				return batchSize;
			}
		});
		return rows;
	}

	
	
	@Override
	public  <T> Page<T> findForPage(Object object, int pageNum, int pageSize) {
		
		return namedJdbcTemplate.findByPage(object, pageNum, pageSize);
	}

	
}
