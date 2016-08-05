package org.nbone.framework.spring.dao.simple;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.nbone.framework.spring.dao.BaseJdbcDao;
import org.nbone.framework.spring.dao.core.EntityPropertySqlParameterSource;
import org.nbone.persistence.BaseSqlSession;
import org.nbone.persistence.BatchSqlSession;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.SqlSession;
import org.nbone.persistence.mapper.DbMappingBuilder;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.TableMapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class SimpleJdbcDao extends BaseSqlSession  implements SqlSession,BatchSqlSession,InitializingBean{
	
	@Resource(name="baseJdbcDao")
	private BaseJdbcDao baseJdbcDao;
	
	private JdbcTemplate jdbcTemplate;
	
	private SimpleJdbcInsert simpleJdbcInsert;
	

	@Override
	public void afterPropertiesSet() throws Exception {
		JdbcTemplate jdbcTemplate =  baseJdbcDao.getJdbcTemplate();
		this.jdbcTemplate = jdbcTemplate;
		this.simpleJdbcInsert  = new SimpleJdbcInsert(jdbcTemplate);
		
	}
	
	@Override
	public int insert(Object object) {
		insertProcess(object);
		int row  = simpleJdbcInsert.execute(new EntityPropertySqlParameterSource(object));
		return row;
	}

	@Override
	public Serializable save(Object object) {
		insertProcess(object);
		Number id = simpleJdbcInsert.executeAndReturnKey(new EntityPropertySqlParameterSource(object));
		return id;
	}

	@Override
	public Object add(Object object) {
		simpleJdbcInsert.reuse();
		TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(object.getClass());
		String[]  primaryKeys= tableMapper.getPrimaryKeys();
		simpleJdbcInsert.withTableName(tableMapper.getDbTableName());
		simpleJdbcInsert.usingGeneratedKeyColumns(primaryKeys);
		Number num = simpleJdbcInsert.executeAndReturnKey(new EntityPropertySqlParameterSource(object));
		
		if(primaryKeys != null && primaryKeys.length > 0){
			BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
			beanWrapper.setPropertyValue(primaryKeys[0], num);
		}
		
		return object;
	}
	
	private void insertProcess(Object object){
		simpleJdbcInsert.reuse();
		TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(object.getClass());
		simpleJdbcInsert.withTableName(tableMapper.getDbTableName());
		String[]  primaryKeys= tableMapper.getPrimaryKeys();
		FieldMapper fieldMapper = tableMapper.getFieldMapper(primaryKeys[0]);
		Class<?> cls = fieldMapper.getPropertyType();
		if(Number.class.isAssignableFrom(cls) || long.class.isAssignableFrom(cls) || int.class.isAssignableFrom(cls)){
			simpleJdbcInsert.usingGeneratedKeyColumns(primaryKeys);
		}
	}

	@Override
	public int update(Object object) {
		return 0;
	}

	@Override
	public int updateSelective(Object object) {
		return 0;
	}

	@Override
	public void saveOrUpdate(Object object) {
	}

	@Override
	public int delete(Object object) {
		return 0;
	}

	@Override
	public int delete(Class<?> clazz, Serializable id) {
		return 0;
	}
	
	@Override
	public <T> int delete(Class<T> clazz, Object[] ids) {
		return 0;
	}


	@Override
	public <T> T get(Class<T> clazz, Serializable id) {
		return null;
	}
	
	@Override
	public <T> T get(Object object) {
		return null;
	}

	
	@Override
	public int[] batchInsert(Object[] objects) {
		EntityPropertySqlParameterSource[] batch = new EntityPropertySqlParameterSource[objects.length];
		for (int i = 0; i < objects.length; i++) {
			if(i == 0){
				insertProcess(objects[0]);
			}
			batch[i] = new EntityPropertySqlParameterSource(objects[i]);
		}
		int[] row  = simpleJdbcInsert.executeBatch(batch);
		return row;
	}
	
	@Override
	public int[] batchInsert(Collection<?> objects) {
		EntityPropertySqlParameterSource[] batch = new EntityPropertySqlParameterSource[objects.size()];
		int index = 0 ; 
		for (Object object : objects) {
			if(index == 0){
				insertProcess(object);
			}
			batch[index] = new EntityPropertySqlParameterSource(object);
			index ++;
		}
		int[] row  = simpleJdbcInsert.executeBatch(batch);
		return row;
	}

	
	@Override
	public int[] batchUpdate(Object[] objects) {
		return null;
	}

	@Override
	public int[] batchUpdate(Collection<?> objects) {
		return null;
	}

	@Override
	public <T> int[] batchDelete(Class<T> clazz, Serializable[] ids) {
		return null;
	}

	@Override
	public <T> int[] batchDelete(Class<T> clazz, List<Serializable> ids) {
		return null;
	}

}
