package org.nbone.framework.spring.dao.namedparam;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nbone.framework.spring.dao.BaseJdbcDao;
import org.nbone.lang.MathOperation;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.BaseSqlSession;
import org.nbone.persistence.BatchSqlSession;
import org.nbone.persistence.SqlBuilder;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.SqlSession;
import org.nbone.persistence.annotation.FieldLevel;
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
	
	private NamedParameterJdbcTemplate namedPjdbcTemplate;
	
	private NamedJdbcTemplate namedJdbcTemplate;
	
	
	private SqlBuilder sqlBuilder = new BaseSqlBuilder(JdbcFrameWork.SPRING_JDBC) {

	}; 
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.dao =  baseJdbcDao.getJdbcTemplate();
		this.namedPjdbcTemplate = new NamedParameterJdbcTemplate(dao);
		this.namedJdbcTemplate = new NamedJdbcTemplate(dao, sqlBuilder);
	}
	

	@Override
	public int insert(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.insertSelectiveSql(object);
		checkSqlModel(sqlModel);
		
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		return namedPjdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public int insert(Class<?> entityClass, Map<String, Object> fieldMap) {
		return simpleJdbcDao.insert(entityClass, fieldMap);
	}


	@Override
	public Serializable save(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.insertSelectiveSql(object);
		checkSqlModel(sqlModel);
		
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		KeyHolder generatedKeyHolder =  new  GeneratedKeyHolder();
		namedPjdbcTemplate.update(sqlModel.getSql(), paramSource, generatedKeyHolder);
		return generatedKeyHolder.getKey();
	}

	@Override
	public Object add(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.insertSelectiveSql(object);
		checkSqlModel(sqlModel);
		
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		KeyHolder generatedKeyHolder =  new  GeneratedKeyHolder();
		namedPjdbcTemplate.update(sqlModel.getSql(), paramSource, generatedKeyHolder);
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
		
		SqlModel<Object> sqlModel = sqlBuilder.updateSql(object);
		checkSqlModel(sqlModel);
		
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return namedPjdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public int updateSelective(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.updateSelectiveSql(object);
		checkSqlModel(sqlModel);
		
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return namedPjdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public void saveOrUpdate(Object object) {
		
	}

	@Override
	public int delete(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.deleteSqlByEntityParams(object, true);
		checkSqlModel(sqlModel);
		
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return namedPjdbcTemplate.update(sqlModel.getSql(), paramSource);
	}
	

	@Override
	public int deleteByEntityParams(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.deleteSqlByEntityParams(object, false);
		checkSqlModel(sqlModel);
		
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return namedPjdbcTemplate.update(sqlModel.getSql(), paramSource);
	}


	@Override
	public int delete(Class<?> clazz, Serializable id) {
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.deleteSqlById(clazz, id);
		int row = 0;
		checkSqlModel(sqlModel);
		
		row = namedPjdbcTemplate.update(sqlModel.getSql(),sqlModel.getParameter());
		
		return row;
	}
	
	@Override
	public <T> int delete(Class<T> clazz, Object[] ids) {
		SqlModel<T> sqlModel = sqlBuilder.deleteSqlByIds(clazz, ids);
		int row = 0;
		checkSqlModel(sqlModel);
		row = dao.update(sqlModel.getSql(),sqlModel.getParameterArray());
		return row;
	}


	@Override
	public <T> T get(Class<T> clazz, Serializable id) {
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.selectSqlById(clazz, id);
		checkSqlModel(sqlModel);
		//RowMapper<T> rowMapper = DbMappingBuilder.ME.getTableMapper(clazz).getRowMapper();
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(), sqlModel.getParameter(),rowMapper);
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
		SqlModel<Object> sqlModel = sqlBuilder.selectSqlById(object);
		checkSqlModel(sqlModel);
			
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(),new BeanPropertySqlParameterSource(object),rowMapper);
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
		SqlModel<?> sqlModel =  sqlBuilder.countSql(clazz);
		checkSqlModel(sqlModel);
		Number number = dao.queryForObject(sqlModel.getSql(), Long.class);
		
		return (number != null ? number.longValue() : 0);
	}

	@Override
	public long count(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.countSql(object);
		checkSqlModel(sqlModel);
		SqlParameterSource parameterSource =  new BeanPropertySqlParameterSource(object);
		
		Number number = namedPjdbcTemplate.queryForObject(sqlModel.getSql(),parameterSource, Long.class);
		return (number != null ? number.longValue() : 0);
	}


	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		SqlModel<T> sqlModel = sqlBuilder.selectAllSql(clazz);
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
		SqlModel<T> sqlModel = sqlBuilder.selectSqlByIds(clazz, ids);
		checkSqlModel(sqlModel);
		RowMapper<T> rowMapper =    (RowMapper<T>) sqlModel.getRowMapper();
		List<T> result  = dao.query(sqlModel.getSql(),ids,rowMapper);
		return result;
	}
	@Override
	public <T> List<T> getForList(Object object) {
		return getForList(object, (FieldLevel)null);
	}	
	

	@Override
	public <T> List<T> getForList(Object object, FieldLevel fieldLevel) {
		
		SqlModel<Object> sqlModel = sqlBuilder.selectSql(object,fieldLevel);
		checkSqlModel(sqlModel);
			
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(),new BeanPropertySqlParameterSource(object),rowMapper);
		return list;
	}


	@Override
	public <T> List<T> queryForList(Object object) {
		return queryForList(object, (FieldLevel)null);
	}
	
	@Override
	public <T> List<T> queryForList(Object object, FieldLevel fieldLevel) {
		SqlModel<Object> sqlModel = sqlBuilder.simpleSelectSql(object,fieldLevel);
		checkSqlModel(sqlModel);
			
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(),new BeanPropertySqlParameterSource(object),rowMapper);
		return list;
	}


	public  <T> List<T> queryForList(Object object,SqlConfig sqlConfig){
		
		SqlModel<Map<String,Object>> sqlModel = sqlBuilder.objectModeSelectSql(object, sqlConfig);
		checkSqlModel(sqlModel);
			
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(),sqlModel.getParameter(),rowMapper);
		return list;
	}
    /**
     * XXX：预留方法目前实现采用 {@link #queryForList(Object)}
     */
	@Override
	public <T> List<T> findForList(Object object) {
		return this.queryForList(object);
	}
	 /**
     * XXX：预留方法目前实现采用 {@link #queryForList(Object, FieldLevel)}
     */
	@Override
	public <T> List<T> findForList(Object object, FieldLevel fieldLevel) {
		return this.queryForList(object, fieldLevel);
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
		SqlModel<Object> sqlModel = sqlBuilder.updateSql(objects[0]);
		checkSqlModel(sqlModel);
			
		SqlParameterSource[] batchArgs = new BeanPropertySqlParameterSource[objects.length];
		for (int i = 0; i < objects.length; i++) {
			batchArgs[i] = new BeanPropertySqlParameterSource(objects[i]);
		}
		int[] rows = namedPjdbcTemplate.batchUpdate(sqlModel.getSql(), batchArgs);
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
				SqlModel<Object> sqlModel = sqlBuilder.updateSql(object);
				sql = sqlModel.getSql();
			}
			batchArgs[index] = new BeanPropertySqlParameterSource(object);
			
			index ++;
		}
		int[] rows = namedPjdbcTemplate.batchUpdate(sql, batchArgs);
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
	public <T> Page<T> getForPage(Object object, int pageNum, int pageSize) {
		return namedJdbcTemplate.getForPage(object, pageNum, pageSize);
	}

	@Override
	public <T> Page<T> queryForPage(Object object, int pageNum, int pageSize) {
		return namedJdbcTemplate.queryForPage(object, pageNum, pageSize);
	}

	@Override
	public  <T> Page<T> findForPage(Object object, int pageNum, int pageSize) {
		
		return namedJdbcTemplate.findForPage(object, pageNum, pageSize);
	}


	@Override
	public <T> List<T> getForList(Object object, String fieldName) {
		 //TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(object.getClass());
		 //FieldMapper fieldMapper = tableMapper.getFieldMapperByPropertyName(fieldName);
		 
		return (List<T>) getForList(object, fieldName, Object.class);
	}
	

	@Override
	public <T> List<T> getForList(Object object, String fieldName, final Class<T> requiredType) {
		SqlConfig sqlConfig = new SqlConfig(-1);
		sqlConfig.setFieldNames(new String[]{fieldName});
		
		SqlModel<Object> sqlModel= sqlBuilder.selectSql(object, sqlConfig);
		BeanPropertySqlParameterSource beanSqlparam = new BeanPropertySqlParameterSource(object);
		List<T> reuslt  = namedPjdbcTemplate.query(sqlModel.getSql(),beanSqlparam,new RowMapper<T>() {

			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				//JDK 1.7 add
				//rs.getObject(1, requiredType);
				
				T object = (T) rs.getObject(1);
				return object;
			}
		});
		
		
		return reuslt;
	}


	@Override
	public <T> List<T> getForListWithFieldNames(Object object, String[] fieldNames,boolean dbFieldMode) {
		SqlConfig sqlConfig = new SqlConfig(-1);
		sqlConfig.setFieldNames(fieldNames);
		sqlConfig.setDbFieldMode(dbFieldMode);
		
		SqlModel<Object> sqlModel= sqlBuilder.selectSql(object, sqlConfig);
		checkSqlModel(sqlModel);
		
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(),new BeanPropertySqlParameterSource(object),rowMapper);
		return list;
	}

    /**
     * MathOperation is null MathOperation set +
     */
	@Override
	public  int updateMathOperation(Object object, MathOperation mathOperation) {
		if(mathOperation == null ){
			mathOperation = MathOperation.ADD;
		}
		SqlModel<Object> sqlModel = sqlBuilder.updateMathOperationSql(object, mathOperation);
		checkSqlModel(sqlModel);
		
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return namedPjdbcTemplate.update(sqlModel.getSql(), paramSource);
	}
	

	
	
	
}
