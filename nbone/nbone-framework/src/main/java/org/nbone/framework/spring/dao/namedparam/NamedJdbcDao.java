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
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.BaseSqlSession;
import org.nbone.persistence.BatchSqlSession;
import org.nbone.persistence.SqlBuilder;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.SqlSession;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.mapper.MappingBuilder;
import org.nbone.persistence.mapper.EntityMapper;
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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
	
	
	private JdbcTemplate jdbcTemplate;
	
	private NamedParameterJdbcTemplate namedPjdbcTemplate;
	
	private NamedJdbcTemplate namedJdbcTemplate;
	
	
	private SqlBuilder sqlBuilder = new BaseSqlBuilder(JdbcFrameWork.SPRING_JDBC) {

	}; 
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//代理有时存在问题取不到值
		this.jdbcTemplate =  baseJdbcDao.getJdbcTemplate();
		if(jdbcTemplate == null ){
			jdbcTemplate = baseJdbcDao.getSuperJdbcTemplate();
		}
		this.namedPjdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		this.namedJdbcTemplate = new NamedJdbcTemplate(jdbcTemplate, sqlBuilder);
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
		
		SqlModel<Object> sqlModel = sqlBuilder.updateSql(object,null,null);
		checkSqlModel(sqlModel);
		
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return namedPjdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public int updateSelective(Object object) {
		//SqlModel<Object> sqlModel = sqlBuilder.updateSelectiveSql(object,null);
		return updateSelective(object,null,null);
	}

	
	@Override
	public int updateSelective(Object object, Object whereEntity) {
		return 0;
	}

	@Override
	public int updateSelective(Object object, Map<String, Object> whereMap) {
		return 0;
	}

	@Override
	public int updateSelective(Object object, String[] properties, String whereString) {
		return updateSelective(object,properties,null,whereString);
	}

	@Override
	public int updateSelective(Object object, String[] properties, String[] conditionFields, String whereString) {

		SqlModel<Object> sqlModel = sqlBuilder.updateSql(object,properties,true,conditionFields,whereString);
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
		row = jdbcTemplate.update(sqlModel.getSql(),sqlModel.getParameterArray());
		return row;
	}


	@Override
	public <T> T get(Class<T> clazz, Serializable id) {
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.selectSqlById(clazz, id);
		checkSqlModel(sqlModel);
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();

		//List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(),sqlModel.getParameter() ,rowMapper);
		return getOne(sqlModel.getSql(), new MapSqlParameterSource(sqlModel.getParameter()),rowMapper);
	}

	@Override
	public <T> T get(Object object){
		SqlModel<Object> sqlModel = sqlBuilder.selectSqlById(object);
		checkSqlModel(sqlModel);
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();

		return getOne(sqlModel.getSql(),new BeanPropertySqlParameterSource(object),rowMapper);
	}

	private <T> T getOne(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper){
		List<T> list = namedPjdbcTemplate.query(sql,paramSource,rowMapper);
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
	public long count(Class<?> clazz,String afterWhere) {
		SqlModel<?> sqlModel =  sqlBuilder.countSql(clazz,afterWhere);
		checkSqlModel(sqlModel);
		Number number = jdbcTemplate.queryForObject(sqlModel.getSql(), Long.class);
		
		return (number != null ? number.longValue() : 0);
	}

	@Override
	public long count(Object object,SqlConfig sqlConfig) {
		SqlModel<Object> sqlModel = sqlBuilder.countSql(object,sqlConfig);
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
		List<T> result  = jdbcTemplate.query(sqlModel.getSql(),rowMapper);
		return result;
	}
	
	@Override
	public <T> List<T> getAll(Class<T> clazz,Collection<?> ids) {
		return getAll(clazz, ids.toArray());
	}

	@Override
	public <T> List<T> getAll(Class<T> clazz,Object[] ids){
		SqlModel<T> sqlModel = sqlBuilder.selectSqlByIds(clazz, ids);
		checkSqlModel(sqlModel);
		RowMapper<T> rowMapper =    (RowMapper<T>) sqlModel.getRowMapper();
		List<T> result  = jdbcTemplate.query(sqlModel.getSql(),ids,rowMapper);
		return result;
	}
	@Override
	public <T> List<T> getForList(Object object,SqlConfig sqlConfig) {
		if(sqlConfig == null){
			sqlConfig = SqlConfig.EMPTY;
		}
		return list(object, sqlConfig);
	}

	@Override
	public <T> List<T> getForList(Map<String, ?> columnMap,SqlConfig sqlConfig) {
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.selectSql(columnMap,sqlConfig);
		checkSqlModel(sqlModel);
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();

		RowMapper<T> rowMapperGroup =  sqlBuilder.getRowMapper(sqlConfig.getGroupQuery());
		if(rowMapperGroup != null){
			rowMapper = rowMapperGroup;
		}
		List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(),columnMap,rowMapper);
		return list;
	}

	@Override
	public  <T> List<T> queryForList(Object object,SqlConfig sqlConfig){
		if(sqlConfig == null){
			sqlConfig = SqlConfig.EMPTY;
		}
		return list(object,sqlConfig);
	}

	private <T> List<T> list(Object object,SqlConfig sqlConfig){
		SqlModel<Object> sqlModel = sqlBuilder.selectSql(object,sqlConfig);
		checkSqlModel(sqlModel);

		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();

		RowMapper<T> rowMapperGroup =  sqlBuilder.getRowMapper(sqlConfig.getGroupQuery());
		if(rowMapperGroup != null){
			rowMapper = rowMapperGroup;
		}

		List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(),new BeanPropertySqlParameterSource(object),rowMapper);
		return list;
	}
    /**
     * XXX：预留方法目前实现采用 {@link #queryForList(Object, SqlConfig)}
     */
	@Override
	public <T> List<T> findForList(Object object) {
		return this.queryForList(object,null);
	}

	@Override
	public <T> List<T> findForList(Object object, SqlConfig sqlConfig) {
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.objectModeSelectSql(object, sqlConfig);
		checkSqlModel(sqlModel);

		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(),sqlModel.getParameter(),rowMapper);
		return list;
	}


	/**
	 * 暂时使用simpleJdbcDao的批量更新
	 * @param objects
	 * @return
	 */
	@Override
	public int[] batchInsert(Object[] objects,boolean jdbcBatch) {
		return simpleJdbcDao.batchInsert(objects,jdbcBatch);
	}

	@Override
	public int[] batchInsert(Collection<?> objects,boolean jdbcBatch) {
		return simpleJdbcDao.batchInsert(objects,jdbcBatch);
	}

	@Override
	public int[] batchInsert(Object[] objects, String[] insertProperties, boolean jdbcBatch) {
		return simpleJdbcDao.batchInsert(objects,insertProperties,jdbcBatch);
	}

	@Override
	public int[] batchInsert(Collection<?> objects, String[] insertProperties, boolean jdbcBatch) {
		return simpleJdbcDao.batchInsert(objects,insertProperties,jdbcBatch);
	}

	@Override
	public int[] batchUpdate(Object[] objects,String...properties) {
		return batchUpdate(objects,properties,null);
	}
	
	@Override
	public int[] batchUpdate(Collection<?> objects,String...properties) {
		return batchUpdate(objects,properties,null);
	}

	@Override
	public int[] batchUpdate(Object[] objects, String[] properties, String... conditionProperties) {
		if(objects == null || objects.length <= 0){
			return new int[] {0};
		}
		//XXX: thinking 共享第一实体的sql
		SqlModel<Object> sqlModel = sqlBuilder.updateSql(objects[0],properties,conditionProperties);
		checkSqlModel(sqlModel);

		SqlParameterSource[] batchArgs = new BeanPropertySqlParameterSource[objects.length];
		for (int i = 0; i < objects.length; i++) {
			batchArgs[i] = new BeanPropertySqlParameterSource(objects[i]);
		}
		int[] rows = namedPjdbcTemplate.batchUpdate(sqlModel.getSql(), batchArgs);
		return rows;
	}

	@Override
	public int[] batchUpdate(Collection<?> objects, String[] properties, String... conditionProperties) {
		if(objects == null || objects.size() <= 0){
			return new int[] {0};
		}
		SqlParameterSource[] batchArgs = new BeanPropertySqlParameterSource[objects.size()];
		String sql = null;
		int index = 0;
		for (Object object : objects) {
			if(index == 0 ){
				//XXX: thinking 共享第一实体的sql
				SqlModel<Object> sqlModel = sqlBuilder.updateSql(object,properties,conditionProperties);
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
		 if(ids == null || ids.size() <= 0){
			return new int[] {0};
		 }
		//XXX: thinking 共享第一实体的sql
		 EntityMapper<T> entityMapper =  MappingBuilder.ME.getTableMapper(clazz);
		 String sql  = entityMapper.getDeleteSqlWithId().toString();
		 final List<Serializable> tempids = ids;
		 final int batchSize = ids.size();
		 int[] rows = jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {
			
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
		if(ids == null || ids.length <= 0){
			return new int[] {0};
		}
		 EntityMapper<T> entityMapper =  MappingBuilder.ME.getTableMapper(clazz);
		 String sql  = entityMapper.getDeleteSqlWithId().toString();
		 final Serializable[] tempids = ids;
		 final int batchSize = ids.length;
		 int[] rows = jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {
			
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
	public <T> Page<T> getForPage(Object object, SqlConfig sqlConfig, int pageNum, int pageSize) {
		return namedJdbcTemplate.getForPage(object, sqlConfig, pageNum, pageSize);
	}
	@Override
	public <T> Page<T> getForPage(Map<String, ?> paramMap, SqlConfig sqlConfig, int pageNum, int pageSize) {
		return namedJdbcTemplate.getForPage(paramMap, sqlConfig, pageNum, pageSize);
	}

	@Override
	public <T> Page<T> queryForPage(Object object,SqlConfig sqlConfig, int pageNum, int pageSize) {
		return namedJdbcTemplate.queryForPage(object,sqlConfig, pageNum, pageSize);
	}

	@Override
	public  <T> Page<T> findForPage(Object object, int pageNum, int pageSize,SqlConfig sqlConfig) {
		
		return namedJdbcTemplate.findForPage(object, pageNum, pageSize,sqlConfig);
	}

	@Override
	public <T> List<T> getForLimit(Object object,SqlConfig sqlConfig, int limit) {
		return namedJdbcTemplate.getForLimit(object,sqlConfig,limit);
	}
	@Override
	public <T> List<T> getForLimit(Object object, Map<String, String> operationMap,GroupQuery group, int limit,String... afterWhere) {
		SqlConfig sqlConfig = new SqlConfig(-1);
		sqlConfig.addOperationMapString(operationMap).groupQuery(group).afterWhere(afterWhere);
		return namedJdbcTemplate.getForLimit(object, sqlConfig, limit);
	}

	@Override
	public <T> List<T> queryForLimit(Object object,SqlConfig sqlConfig, int limit) {
		return namedJdbcTemplate.queryForLimit(object,sqlConfig,limit);
	}


	@Override
	public <T> List<T> getForList(Object object, String fieldName, final Class<T> requiredType,String... afterWhere) {
		SqlConfig sqlConfig = new SqlConfig(-1);
		sqlConfig.setFieldNames(new String[]{fieldName});
		sqlConfig.setAfterWhere(afterWhere);

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

    /**
     * MathOperation is null MathOperation set +
     */
	@Override
	public  int updateMathOperation(Object object,String property, MathOperation mathOperation) {
		if(mathOperation == null ){
			mathOperation = MathOperation.ADD;
		}
		SqlModel<Object> sqlModel = sqlBuilder.updateMathOperationSql(object,property, mathOperation);
		checkSqlModel(sqlModel);
		
		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		
		return namedPjdbcTemplate.update(sqlModel.getSql(), paramSource);
	}
	

	
	
	
}
