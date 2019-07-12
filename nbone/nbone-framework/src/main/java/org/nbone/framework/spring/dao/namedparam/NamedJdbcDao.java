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
import org.springframework.jdbc.core.ColumnMapRowMapper;
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
		return updateSelective(object,(String) null);
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
	public int updateSelective(Object object, String whereString) {

		SqlModel<Object> sqlModel = sqlBuilder.updateSql(null,object,true,null,whereString);
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
	public long count(Class<?> clazz) {
		SqlModel<?> sqlModel =  sqlBuilder.countSql(clazz);
		checkSqlModel(sqlModel);
		Number number = jdbcTemplate.queryForObject(sqlModel.getSql(), Long.class);
		
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
	public <T> List<T> getForList(Object object,String... afterWhere) {
		return getForList(object, (FieldLevel)null,afterWhere);
	}
	@Override
	public <T> List<T> getForList(Object object, FieldLevel fieldLevel,String... afterWhere) {
		return list(object,null,fieldLevel,-1,afterWhere);
	}
	@Override
	public <T> List<T> getForList(Object object, GroupQuery group, FieldLevel fieldLevel, String... afterWhere) {
		return list(object,group,fieldLevel,-1,afterWhere);
	}


	@Override
	public <T> List<T> queryForList(Object object,String... afterWhere) {
		return queryForList(object, (FieldLevel)null,afterWhere);
	}
	
	@Override
	public <T> List<T> queryForList(Object object, FieldLevel fieldLevel,String... afterWhere) {
		return list(object,null,fieldLevel,SqlConfig.PrimaryMode,afterWhere);
	}

	private <T> List<T> list(Object object, GroupQuery group, FieldLevel fieldLevel, int model, String... afterWhere){
		SqlModel<Object> sqlModel = sqlBuilder.sqlConfigSelectSql(object,group,fieldLevel,model,afterWhere);
		checkSqlModel(sqlModel);

		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();

		RowMapper<T> rowMapperGroup =  sqlBuilder.getRowMapper(group);
		if(rowMapperGroup != null){
			rowMapper = rowMapperGroup;
		}

		List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(),new BeanPropertySqlParameterSource(object),rowMapper);
		return list;

	}
	@Override
	public  <T> List<T> queryForList(Object object,SqlConfig sqlConfig){
		
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.objectModeSelectSql(object, sqlConfig);
		checkSqlModel(sqlModel);
			
		RowMapper<T> rowMapper =   (RowMapper<T>) sqlModel.getRowMapper();
		List<T> list = namedPjdbcTemplate.query(sqlModel.getSql(),sqlModel.getParameter(),rowMapper);
		return list;
	}
    /**
     * XXX：预留方法目前实现采用 {@link #queryForList(Object, String...)}
     */
	@Override
	public <T> List<T> findForList(Object object) {
		return this.queryForList(object);
	}
	 /**
     * XXX：预留方法目前实现采用 {@link #queryForList(Object, FieldLevel,String...)}
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
	public int[] batchInsert(Object[] objects,boolean jdbcBatch) {
		return simpleJdbcDao.batchInsert(objects,jdbcBatch);
	}

	@Override
	public int[] batchInsert(Collection<?> objects,boolean jdbcBatch) {
		return simpleJdbcDao.batchInsert(objects,jdbcBatch);
	}

	@Override
	public int[] batchUpdate(Object[] objects,String...propertys) {
		return batchUpdate(objects,propertys,null);
	}
	
	@Override
	public int[] batchUpdate(Collection<?> objects,String...propertys) {
		return batchUpdate(objects,propertys,null);
	}

	@Override
	public int[] batchUpdate(Object[] objects, String[] propertys, String... conditionPropertys) {
		if(objects == null || objects.length <= 0){
			return new int[] {0};
		}
		//XXX: thinking 共享第一实体的sql
		SqlModel<Object> sqlModel = sqlBuilder.updateSql(objects[0],propertys,conditionPropertys);
		checkSqlModel(sqlModel);

		SqlParameterSource[] batchArgs = new BeanPropertySqlParameterSource[objects.length];
		for (int i = 0; i < objects.length; i++) {
			batchArgs[i] = new BeanPropertySqlParameterSource(objects[i]);
		}
		int[] rows = namedPjdbcTemplate.batchUpdate(sqlModel.getSql(), batchArgs);
		return rows;
	}

	@Override
	public int[] batchUpdate(Collection<?> objects, String[] propertys, String... conditionPropertys) {
		if(objects == null || objects.size() <= 0){
			return new int[] {0};
		}
		SqlParameterSource[] batchArgs = new BeanPropertySqlParameterSource[objects.size()];
		String sql = null;
		int index = 0;
		for (Object object : objects) {
			if(index == 0 ){
				//XXX: thinking 共享第一实体的sql
				SqlModel<Object> sqlModel = sqlBuilder.updateSql(object,propertys,conditionPropertys);
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
		 TableMapper<T> tableMapper =  DbMappingBuilder.ME.getTableMapper(clazz);
		 String sql  = tableMapper.getDeleteSqlWithId().toString();
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
		 TableMapper<T> tableMapper =  DbMappingBuilder.ME.getTableMapper(clazz);
		 String sql  = tableMapper.getDeleteSqlWithId().toString();
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
	public <T> Page<T> getForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		return namedJdbcTemplate.getForPage(object, pageNum, pageSize,afterWhere);
	}

	@Override
	public <T> Page<T> queryForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		return namedJdbcTemplate.queryForPage(object, pageNum, pageSize,afterWhere);
	}

	@Override
	public <T> Page<T> queryForPage(Object object, int pageNum, int pageSize, SqlConfig sqlConfig) {
		return namedJdbcTemplate.queryForPage(object,pageNum,pageSize,sqlConfig);
	}

	@Override
	public  <T> Page<T> findForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		
		return namedJdbcTemplate.findForPage(object, pageNum, pageSize,afterWhere);
	}

	@Override
	public <T> List<T> getForLimit(Object object,GroupQuery group, int limit, String... afterWhere) {
		return namedJdbcTemplate.getForLimit(object,group,limit,afterWhere);
	}

	@Override
	public <T> List<T> queryForLimit(Object object,GroupQuery group, int limit, String... afterWhere) {
		return namedJdbcTemplate.queryForLimit(object,group,limit,afterWhere);
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
