package org.nbone.framework.spring.dao.namedparam;

import org.nbone.framework.spring.dao.BaseJdbcDao;
import org.nbone.framework.spring.dao.core.RowMapperWithMapExtractor;
import org.nbone.framework.spring.dao.core.SingleEntityResultSetExtractor;
import org.nbone.lang.MathOperation;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.mvc.domain.ParentDomain;
import org.nbone.persistence.*;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.mapper.EntityMapper;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.MappingBuilder;
import org.nbone.persistence.model.SqlModel;
import org.nbone.persistence.util.SqlUtils;
import org.nbone.web.util.RequestQueryUtils;
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
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
public class NamedJdbcDao extends BaseSqlSession implements SqlSession,BatchSqlSession, RequestQuery,InitializingBean{

	@Resource(name="baseJdbcDao")
	private BaseJdbcDao baseJdbcDao;

	@Resource(name="simpleJdbcDao")
	private BatchSqlSession simpleJdbcDao;

	private JdbcTemplate jdbcTemplate;
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
		this.namedJdbcTemplate = new NamedJdbcTemplate(jdbcTemplate, sqlBuilder);
	}


	@Override
	public int insert(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.insertSelectiveSql(object);
		checkSqlModel(sqlModel);

		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);
		return namedJdbcTemplate.update(sqlModel.getSql(), paramSource);
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
		namedJdbcTemplate.update(sqlModel.getSql(), paramSource, generatedKeyHolder);
		return generatedKeyHolder.getKey();
	}

	@Override
	public Object add(Object object) {
		Serializable num = save(object);
		FieldMapper primaryKey = sqlBuilder.getPrimaryKey(object.getClass());
		if(primaryKey != null){
			BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
			beanWrapper.setPropertyValue(primaryKey.getFieldName(), num);
		}
		return object;
	}

	@Override
	public int update(Object object) {

		SqlModel<Object> sqlModel = sqlBuilder.updateSql(object,null,null);
		checkSqlModel(sqlModel);

		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);

		return namedJdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public int updateSelective(Object object) {
		//SqlModel<Object> sqlModel = sqlBuilder.updateSelectiveSql(object,null);
		return updateSelective(object,null,null);
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

		return namedJdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public void saveOrUpdate(Object object) {

	}

	@Override
	public int delete(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.deleteSqlByEntity(object, true,null);
		checkSqlModel(sqlModel);

		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);

		return namedJdbcTemplate.update(sqlModel.getSql(), paramSource);
	}


	@Override
	public int deleteByEntity(Object object) {
		SqlModel<Object> sqlModel = sqlBuilder.deleteSqlByEntity(object, false,null);
		checkSqlModel(sqlModel);

		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);

		return namedJdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public int delete(SqlConfig sqlConfig) {
		SqlModel<Object> sqlModel = sqlBuilder.deleteSql(sqlConfig);
		SqlParameterSource paramSource = namedJdbcTemplate.getSqlParameterSource(sqlModel);
		if (paramSource != null) {
			return namedJdbcTemplate.update(sqlModel.getSql(), paramSource);
		} else {
			return jdbcTemplate.update(sqlModel.getSql());
		}
	}

	@Override
	public int delete(Class<?> clazz, Serializable id,String tableName) {
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.deleteSqlById(clazz, id,tableName);
		int row = 0;
		checkSqlModel(sqlModel);

		row = namedJdbcTemplate.update(sqlModel.getSql(),sqlModel.getParameter());

		return row;
	}

	@Override
	public <T> int delete(Class<T> clazz, Object[] ids,String tableName) {
		SqlModel<T> sqlModel = sqlBuilder.deleteSqlByIds(clazz, ids,tableName);
		int row = 0;
		checkSqlModel(sqlModel);
		row = jdbcTemplate.update(sqlModel.getSql(),sqlModel.getParameterArray());
		return row;
	}


	@Override
	public <T> T get(Class<T> clazz, Serializable id,String tableName) {
        SqlModel<Map<String,?>> sqlModel = sqlBuilder.selectSqlById(clazz, id,tableName);
        return getOne(sqlModel, new MapSqlParameterSource(sqlModel.getParameter()));
	}

	@Override
	public <T> T get(Object object){
		SqlModel<Object> sqlModel = sqlBuilder.selectSqlById(object,null);
		return getOne(sqlModel,new BeanPropertySqlParameterSource(object));
	}

	@Override
	public <T> T getOne(Object object,SqlConfig sqlConfig) {
		SqlModel<Object> sqlModel = sqlBuilder.selectSql(object, sqlConfig);

		RowMapper<T> rowMapper = (RowMapper<T>) sqlModel.getRowMapper();
		T bean  = namedJdbcTemplate.query(sqlModel, object, new SingleEntityResultSetExtractor<T>(rowMapper));
		return bean;
	}

	private <T> T getOne(SqlModel<?> sqlModel, SqlParameterSource paramSource){
		List<T> list = namedJdbcTemplate.query(sqlModel,paramSource);
		int row ;
		if(list != null && (row = list.size()) > 0){
			if(row > 1){
				//Primary key
				logger.warn("unique query result return multiple lines ["+row+"].thinking");
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

		Number number = namedJdbcTemplate.queryForObject(sqlModel.getSql(),parameterSource, Long.class);
		return (number != null ? number.longValue() : 0);
	}


	@Override
	public <T> List<T> getAll(Class<T> clazz,String tableName) {
		SqlModel<T> sqlModel = sqlBuilder.selectAllSql(clazz,tableName);
		checkSqlModel(sqlModel);
		RowMapper<T> rowMapper =    (RowMapper<T>) sqlModel.getRowMapper();
		List<T> result  = jdbcTemplate.query(sqlModel.getSql(),rowMapper);
		return result;
	}

	@Override
	public <T> List<T> getAll(Class<T> clazz,Collection<?> ids,String tableName) {
		return getAll(clazz, ids.toArray(),tableName);
	}

	@Override
	public <T> List<T> getAll(Class<T> clazz,Object[] ids,String tableName){
		SqlModel<T> sqlModel = sqlBuilder.selectSqlByIds(clazz, ids,tableName);
		checkSqlModel(sqlModel);
		RowMapper<T> rowMapper =    (RowMapper<T>) sqlModel.getRowMapper();
		List<T> result  = jdbcTemplate.query(sqlModel.getSql(),ids,rowMapper);
		return result;
	}

	@Override
	public <T> List<T> getForList(Object object,SqlConfig sqlConfig) {
		return list(object, sqlConfig);
	}
	@Override
	public <T> List<T> getForList(Map<String, ?> columnMap,SqlConfig sqlConfig) {
		SqlModel<Map<String,?>> sqlModel = sqlBuilder.selectSql(columnMap,sqlConfig);
		checkSqlModel(sqlModel);

		List<T> list = namedJdbcTemplate.query(sqlModel,new MapSqlParameterSource(columnMap));
		return list;
	}

	@Override
	public <K, T> Map<K, T> getMapKeyValue(Object object, SqlConfig sqlConfig) {
		String mapKey = sqlConfig != null ? sqlConfig.getMapKey() : null;
		Class<?> keyType = sqlConfig != null ? sqlConfig.getMapKeyType() : null;
		String valueName = sqlConfig != null ? sqlConfig.getMapValueName() : null;
		if(StringUtils.hasLength(mapKey) && StringUtils.hasLength(valueName)){
			sqlConfig.setFieldNames(new String[]{mapKey,valueName});
		}
		SqlModel<Object> sqlModel = sqlBuilder.selectSql(object, sqlConfig);

		RowMapper<T> rowMapper = (RowMapper<T>) sqlModel.getRowMapper();
		Map<?, T> map = namedJdbcTemplate.query(sqlModel, object, new RowMapperWithMapExtractor<T>(rowMapper, mapKey,keyType,valueName));
		return (Map<K, T>) map;
	}
	@Override
	public <K, T> Map<K, T> getMapKeyValue(SqlConfig sqlConfig) {
		return getMapKeyValue(null,sqlConfig);
	}
	public <K, T> Map<K, T> getMapWithMapKey(Object object, SqlConfig sqlConfig){
		return getMapKeyValue(object,sqlConfig);
	}
	public <K, T> Map<K, T> getMapWithMapKey(SqlConfig sqlConfig){
		return getMapKeyValue(sqlConfig);
	}


	public <T, K> Map<K, String> getParentName(Collection<? extends ParentDomain> entityDomains, Class<T> entityClass,
											   String idField, String nameField, String tableName) {
		if (CollectionUtils.isEmpty(entityDomains)) {
			return Collections.emptyMap();
		}
		Set<Serializable> parentIds = new HashSet<>(entityDomains.size());
		Class<?> idFieldType = null;
		for (ParentDomain entity : entityDomains) {
			Serializable pid = entity.getParentId();
			if (idFieldType == null && pid != null) {
				idFieldType = pid.getClass();
			}
			parentIds.add(pid);
		}
		EntityMapper<T> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
		FieldMapper fieldMapper = entityMapper.getFieldMapperByProperty(idField);
	/*	if (idFieldType == null) {
			idFieldType = fieldMapper.getPropertyType();
		}
		if (idFieldType == Serializable.class) {
			idFieldType = null;
		}*/
		String columnName = fieldMapper.getDbFieldName();
		String in = SqlUtils.in("", columnName, idFieldType, parentIds, true).toString();
		SqlConfig sqlConfig = SqlConfig.builder(-1).withEntityClass(entityClass)
				.mapKey(idField).mapKeyType(idFieldType).mapValueName(nameField)
				.conditionFields(idField)
				.usedExtField(false)
				.firstCondition(in);
		sqlConfig.setTableName(tableName);
		Map<K, String> map = getMapKeyValue(sqlConfig);

		for (ParentDomain entity : entityDomains) {
			String parentName = map.get(entity.getParentId());
			entity.setParentName(parentName);
		}
		return map;
	}


	@Override
	public  <T> List<T> queryForList(Object object,SqlConfig sqlConfig){
		return list(object,sqlConfig);
	}

	private <T> List<T> list(Object object,SqlConfig sqlConfig){
		SqlModel<Object> sqlModel = sqlBuilder.selectSql(object,sqlConfig);

		List<T> list = namedJdbcTemplate.query(sqlModel,object);
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
		List<T> list = namedJdbcTemplate.query(sqlModel,new MapSqlParameterSource(sqlModel.getParameter()));
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
		return batchInsert(objects,insertProperties,jdbcBatch,false);
	}

	@Override
	public int[] batchInsert(Collection<?> objects, String[] insertProperties, boolean jdbcBatch,boolean usedMapping) {
		return simpleJdbcDao.batchInsert(objects,insertProperties,jdbcBatch,usedMapping);
	}

	@Override
	public int[] batchUpdate(Object[] objects,String...properties) {
		return batchUpdate(objects,properties,(String[])null);
	}

	@Override
	public int[] batchUpdate(Collection<?> objects,String...properties) {
		return batchUpdate(objects,properties,(String[])null);
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
		int[] rows = namedJdbcTemplate.batchUpdate(sqlModel.getSql(), batchArgs);
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
		int[] rows = namedJdbcTemplate.batchUpdate(sql, batchArgs);
		return rows;
	}

	@Override
	public <T> int[] batchDelete(Class<T> clazz, List<Serializable> ids) {
		 if(ids == null || ids.size() <= 0){
			return new int[] {0};
		 }
		//XXX: thinking 共享第一实体的sql
		 EntityMapper<T> entityMapper =  MappingBuilder.ME.getTableMapper(clazz);
		 String sql  = entityMapper.getDeleteSqlWithId(null).toString();
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
		 String sql  = entityMapper.getDeleteSqlWithId(null).toString();
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
		return namedJdbcTemplate.listLimit(object,sqlConfig,limit);
	}

	@Override
	public <T> List<T> getForLimit(Object object, SqlConfig sqlConfig, long offset, int limit) {
		return namedJdbcTemplate.listLimit(object,sqlConfig,offset,limit);
	}

	@Override
	public <T> List<T> getForLimit(Object object, Map<String, String> operationMap,GroupQuery group, int limit,String... afterWhere) {
		SqlConfig sqlConfig = new SqlConfig(-1);
		sqlConfig.addOperationMapString(operationMap).groupQuery(group).afterWhere(afterWhere);
		return namedJdbcTemplate.listLimit(object, sqlConfig, limit);
	}

	@Override
	public <T> List<T> queryForLimit(Object object,SqlConfig sqlConfig, int limit) {
		return namedJdbcTemplate.listLimit(object,sqlConfig,limit);
	}


	@Override
	public <T> List<T> getForList(Object object, String fieldName, final Class<T> requiredType,String... afterWhere) {
		SqlConfig sqlConfig = new SqlConfig(-1);
		sqlConfig.setFieldNames(new String[]{fieldName});
		sqlConfig.setAfterWhere(afterWhere);

		List<T> reuslt  = getForList(object,sqlConfig,null,requiredType);
		return reuslt;
	}

	@Override
	public <T> List<T> getForList(Object object, SqlConfig sqlConfig, String fieldName, Class<T> requiredType) {
		if (sqlConfig == null) {
			sqlConfig = new SqlConfig(-1);
		}
		if(fieldName != null){
			sqlConfig.setFieldNames(new String[]{fieldName});
		}

		SqlModel<Object> sqlModel= sqlBuilder.selectSql(object, sqlConfig);
		List<T> reuslt  = namedJdbcTemplate.query(sqlModel,object,new RowMapper<T>() {

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
	public  int updateMathOperation(Object object,String property, MathOperation mathOperation,String[] conditionFields, String whereString) {
		if(mathOperation == null ){
			mathOperation = MathOperation.ADD;
		}
		SqlModel<Object> sqlModel = sqlBuilder.updateMathOperationSql(object,property, mathOperation,conditionFields,whereString);
		checkSqlModel(sqlModel);

		SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(object);

		return namedJdbcTemplate.update(sqlModel.getSql(), paramSource);
	}

	@Override
	public int updateMathOperation(Object object, String property, MathOperation mathOperation, String[] conditionFields) {
		return updateMathOperation(object,property,mathOperation,conditionFields,null);
	}

	//---------------ServletRequestQuery----------------
	@Override
	public <T> List<T> requestQuery(ServletRequest request, SqlConfig sqlConfig) {
		Integer limit = RequestQueryUtils.getLimit(request);

		SqlModel<Map<String, Object>> sqlModel = sqlBuilder.requestQuery(request, sqlConfig);
		Map<String, Object> paramMap = sqlModel.getParameter();
		SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
		if (limit == null) {
			return namedJdbcTemplate.query(sqlModel, paramSource);
		}
		paramMap.put("limit", limit);
		return namedJdbcTemplate.queryList(sqlModel, paramSource, 1, limit);
	}

	@Override
	public <T> Page<T> requestQueryPage(ServletRequest request, SqlConfig sqlConfig) {
		Integer pageNum = RequestQueryUtils.getPageNum(request);
		Integer pageSize = RequestQueryUtils.getPageSize(request);

		SqlModel<Map<String, Object>> sqlModel = sqlBuilder.requestQuery(request, sqlConfig);
		Map<String, Object> paramMap = sqlModel.getParameter();
		paramMap.put("pageNum", pageNum);
		paramMap.put("pageSize", pageSize);
		return namedJdbcTemplate.processPage(sqlModel, pageNum, pageSize);
	}
}
