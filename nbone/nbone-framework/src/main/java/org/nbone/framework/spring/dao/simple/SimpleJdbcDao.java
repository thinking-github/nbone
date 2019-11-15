package org.nbone.framework.spring.dao.simple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nbone.framework.spring.dao.BaseJdbcDao;
import org.nbone.framework.spring.dao.core.EntityPropertySqlParameterSource;
import org.nbone.persistence.BaseSqlSession;
import org.nbone.persistence.BatchSqlSession;
import org.nbone.persistence.SqlSession;
import org.nbone.persistence.mapper.MappingBuilder;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.EntityMapper;
import org.nbone.util.PropertyUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * 根据实体方式实现单表的增删改查(目前实现不完善, 请使用NamedJdbcDao)
 * @author thinking
 * @version 1.0 
 * @see org.nbone.framework.spring.dao.BaseJdbcDao
 */
@Repository("simpleJdbcDao")
@Primary
@Lazy
public class SimpleJdbcDao extends BaseSqlSession implements BatchSqlSession,InitializingBean{
	
	@Resource(name="baseJdbcDao")
	private BaseJdbcDao baseJdbcDao;
	
	private JdbcTemplate jdbcTemplate;
	
	private SimpleJdbcInsert simpleJdbcInsert;
	

	@Override
	public void afterPropertiesSet() throws Exception {
		//代理有时存在问题取不到值
		JdbcTemplate jdbcTemplate =  baseJdbcDao.getJdbcTemplate();
		if(jdbcTemplate == null ){
			jdbcTemplate = baseJdbcDao.getSuperJdbcTemplate();
		}
		this.jdbcTemplate = jdbcTemplate;
		this.simpleJdbcInsert  = new SimpleJdbcInsert(jdbcTemplate);
		
	}
	
	@Override
	public int insert(Object object) {
		insertProcess(object,null);
		int row  = simpleJdbcInsert.execute(new EntityPropertySqlParameterSource(object));
		return row;
	}
	
    
	@Override
	public int insert(Class<?> entityClass, Map<String, Object> fieldMap) {
		simpleJdbcInsert.reuse();
		EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
		simpleJdbcInsert.withTableName(entityMapper.getDbTableName());
		String primaryKey = entityMapper.getPrimaryKey();
		Object value  = fieldMap.get(primaryKey);
		if(value == null){
			simpleJdbcInsert.usingGeneratedKeyColumns(primaryKey);
		}
		
		return simpleJdbcInsert.execute(fieldMap);
	}

	@Override
	public Serializable save(Object object) {
		insertProcess(object,null);
		Number id = simpleJdbcInsert.executeAndReturnKey(new EntityPropertySqlParameterSource(object));
		return id;
	}

	@Override
	public Object add(Object object) {
		EntityMapper<?> entityMapper = insertProcess(object,null);
		String[]  primaryKeys= entityMapper.getPrimaryKeys();
		
		Number num = simpleJdbcInsert.executeAndReturnKey(new EntityPropertySqlParameterSource(object));
		
		if(primaryKeys != null && primaryKeys.length > 0){
			BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
			beanWrapper.setPropertyValue(primaryKeys[0], num);
		}
		
		return object;
	}
	
	private EntityMapper<?> insertProcess(Object object, String[] insertProperties){
		simpleJdbcInsert.reuse();
		EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(object.getClass());
		String[]  primaryKeys= entityMapper.getPrimaryKeys();
		
		FieldMapper fieldMapper = entityMapper.getFieldMapper(primaryKeys[0]);
		Class<?> cls = fieldMapper.getPropertyType();
		simpleJdbcInsert.withTableName(entityMapper.getDbTableName());
		// option insertProperties
		if(insertProperties != null && insertProperties.length > 0){
			List<String> columnNames = new ArrayList<String>();
			for (String insertProperty : insertProperties) {
				String column = entityMapper.getDbFieldName(insertProperty);
				if(column != null){
					columnNames.add(column);
				}
			}

			simpleJdbcInsert.setColumnNames(columnNames);
		}

		Object value = PropertyUtil.getProperty(object, fieldMapper.getFieldName());
		//XXX：当是数字且为空时使用自动生成主键
		if(value == null && (Number.class.isAssignableFrom(cls) || long.class.isAssignableFrom(cls) || int.class.isAssignableFrom(cls))){
			simpleJdbcInsert.usingGeneratedKeyColumns(primaryKeys);
		}
		return entityMapper;
	}

	
	@Override
	public int[] batchInsert(Object[] objects,boolean jdbcBatch) {
		return batchInsert(objects,null,jdbcBatch);
	}
	
	@Override
	public int[] batchInsert(Collection<?> objects,boolean jdbcBatch) {
		return  batchInsert(objects,null,jdbcBatch);
	}

	@Override
	public int[] batchInsert(Object[] objects, String[] insertProperties, boolean jdbcBatch) {
		if(objects == null || objects.length <= 0){
			return new int[] {0};
		}
		EntityPropertySqlParameterSource[] batch = new EntityPropertySqlParameterSource[objects.length];
		insertProcess(objects[0],insertProperties);
		for (int i = 0; i < objects.length; i++) {
			batch[i] = new EntityPropertySqlParameterSource(objects[i]);
		}
		int[] row;
		if(jdbcBatch){
			row  = simpleJdbcInsert.executeBatch(batch);
		}else {
			row = dbBatchInsert(batch);
		}
		return row;
	}

	@Override
	public int[] batchInsert(Collection<?> objects, String[] insertProperties, boolean jdbcBatch) {
		if(objects == null || objects.size() <= 0){
			return new int[] {0};
		}
		EntityPropertySqlParameterSource[] batch = new EntityPropertySqlParameterSource[objects.size()];
		int index = 0 ;
		for (Object object : objects) {
			batch[index] = new EntityPropertySqlParameterSource(object);
			index ++;
		}
		insertProcess(batch[0].getObject(),insertProperties);
		int[] row;
		if(jdbcBatch){
			row  = simpleJdbcInsert.executeBatch(batch);
		}else {
			row = dbBatchInsert(batch);
		}
		return row;
	}

	@Override
	public int[] batchUpdate(Object[] objects,String...properties) {
		throw new UnsupportedOperationException("unsupported batchUpdate operation.");
	}

	@Override
	public int[] batchUpdate(Collection<?> objects,String...properties) {
		throw new UnsupportedOperationException("unsupported batchUpdate operation.");
	}

	@Override
	public int[] batchUpdate(Object[] objects, String[] properties, String... conditionPropertys) {
		throw new UnsupportedOperationException("unsupported batchUpdate operation.");
	}

	@Override
	public int[] batchUpdate(Collection<?> objects, String[] properties, String... conditionPropertys) {
		throw new UnsupportedOperationException("unsupported batchUpdate operation.");
	}

	@Override
	public <T> int[] batchDelete(Class<T> clazz, Serializable[] ids) {
		throw new UnsupportedOperationException("unsupported batchDelete operation.");
	}

	@Override
	public <T> int[] batchDelete(Class<T> clazz, List<Serializable> ids) {
		throw new UnsupportedOperationException("unsupported batchDelete operation.");
	}


    /**
     *  使用DB 批量模式
     *  INSERT INTO tdcode
     *  (status, tdcode_content, node_id, tenant_code, member_code, batch_id, batch_attach_id,
     *   scan_num, first_scan, recent_scan, create_time, update_time, create_by, update_by)
     *  VALUES
     *  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?),(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?),
     *  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?),(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
     * @param batch
     * @return
     */
	private int[] dbBatchInsert(SqlParameterSource... batch){

        int count = simpleJdbcInsert.dbExecuteBatch(batch);

		return new int[]{count};

	}

}
