package org.nbone.framework.spring.dao.simple;

import org.nbone.framework.spring.dao.BaseJdbcDao;
import org.nbone.framework.spring.dao.core.EntityPropertySqlParameterSource;
import org.nbone.persistence.BaseSqlSession;
import org.nbone.persistence.BatchSqlSession;
import org.nbone.persistence.mapper.EntityMapper;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.MappingBuilder;
import org.nbone.util.PropertyUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

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
	

	@Override
	public void afterPropertiesSet() throws Exception {
		//代理有时存在问题取不到值
		JdbcTemplate jdbcTemplate =  baseJdbcDao.getJdbcTemplate();
		if(jdbcTemplate == null ){
			jdbcTemplate = baseJdbcDao.getSuperJdbcTemplate();
		}
		this.jdbcTemplate = jdbcTemplate;

	}
	
	@Override
	public int insert(Object object) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		insertProcess(simpleJdbcInsert,object,null,false);
		int row  = simpleJdbcInsert.execute(new EntityPropertySqlParameterSource(object));
		return row;
	}
	
    
	@Override
	public int insert(Class<?> entityClass, Map<String, Object> fieldMap) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
		simpleJdbcInsert.withTableName(entityMapper.getTableName(fieldMap));
		String primaryKey = entityMapper.getPrimaryKey();
		Object value  = fieldMap.get(primaryKey);
		if(value == null){
			simpleJdbcInsert.usingGeneratedKeyColumns(primaryKey);
		}
		
		return simpleJdbcInsert.execute(fieldMap);
	}

	@Override
	public Serializable save(Object object) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		insertProcess(simpleJdbcInsert,object,null,false);
		Number id = simpleJdbcInsert.executeAndReturnKey(new EntityPropertySqlParameterSource(object));
		return id;
	}

	@Override
	public Object add(Object object) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		EntityMapper<?> entityMapper = insertProcess(simpleJdbcInsert,object,null,false);
		FieldMapper  primaryKey= entityMapper.getPrimaryKeyFieldMapper();
		
		Number num = simpleJdbcInsert.executeAndReturnKey(new EntityPropertySqlParameterSource(object));
		
		if(primaryKey != null){
			BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
			beanWrapper.setPropertyValue(primaryKey.getFieldName(), num);
		}
		
		return object;
	}

	/**
	 * @param object
	 * @param insertProperties 可为空 ,为空时自动添加字段
	 * @param usedMapping      true used entity properties mapping db columnNames ;false direct used db column
	 * @return
	 */
	private EntityMapper<?> insertProcess(SimpleJdbcInsert simpleJdbcInsert,Object object, String[] insertProperties,boolean usedMapping){

		EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(object.getClass());

		FieldMapper fieldMapper = entityMapper.getPrimaryKeyFieldMapper();
		Class<?> cls = fieldMapper.getPropertyType();
		simpleJdbcInsert.withTableName(entityMapper.getTableName(object));

		Object value = PropertyUtil.getProperty(object, fieldMapper.getFieldName());
		//XXX：当是数字且为空时使用自动生成主键
		boolean generatedKeyColumnsUsed = false;
		if(value == null && (Number.class.isAssignableFrom(cls)
				|| long.class.isAssignableFrom(cls) || int.class.isAssignableFrom(cls))){
			simpleJdbcInsert.usingGeneratedKeyColumns(entityMapper.getPrimaryKeys());
			generatedKeyColumnsUsed = true;
		}

		// option insertProperties
		if(insertProperties != null && insertProperties.length > 0){
			List<String> columnNames = new ArrayList<String>(insertProperties.length);
			for (String insertProperty : insertProperties) {
				String column = entityMapper.getDbFieldName(insertProperty);
				if(column != null){
					columnNames.add(column);
				}
			}

			simpleJdbcInsert.setColumnNames(columnNames);
		}else {
			if(usedMapping){
				simpleJdbcInsert.setColumnNames(entityMapper.getColumnNames(generatedKeyColumnsUsed));
			}
		}

		return entityMapper;
	}

	
	@Override
	public int[] batchInsert(Object[] objects,boolean jdbcBatch) {
		return batchInsert(objects,null,jdbcBatch);
	}
	
	@Override
	public int[] batchInsert(Collection<?> objects,boolean jdbcBatch) {
		return  batchInsert(objects,null,jdbcBatch,false);
	}

	@Override
	public int[] batchInsert(Object[] objects, String[] insertProperties, boolean jdbcBatch) {
		if(objects == null || objects.length <= 0){
			return new int[] {0};
		}
		// SimpleJdbcInsert 如果复用 多线程使用时 存在并发问题
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		EntityPropertySqlParameterSource[] batch = new EntityPropertySqlParameterSource[objects.length];
		insertProcess(simpleJdbcInsert,objects[0],insertProperties,false);
		for (int i = 0; i < objects.length; i++) {
			batch[i] = new EntityPropertySqlParameterSource(objects[i]);
		}
		simpleJdbcInsert.compileNonSync();
		int[] row;
		if(jdbcBatch){
			row  = simpleJdbcInsert.executeBatch(batch);
		}else {
			row = dbBatchInsert(simpleJdbcInsert,batch);
		}
		return row;
	}

	@Override
	public int[] batchInsert(Collection<?> objects, String[] insertProperties, boolean jdbcBatch) {
		return batchInsert(objects,insertProperties,jdbcBatch,false);
	}

	@Override
	public int[] batchInsert(Collection<?> objects, String[] insertProperties, boolean jdbcBatch,boolean usedMapping) {
		if(objects == null || objects.size() <= 0){
			return new int[] {0};
		}
		// SimpleJdbcInsert 如果复用 多线程使用时 存在并发问题
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		EntityPropertySqlParameterSource[] batch = new EntityPropertySqlParameterSource[objects.size()];
		int index = 0 ;
		for (Object object : objects) {
			batch[index] = new EntityPropertySqlParameterSource(object);
			index ++;
		}
		insertProcess(simpleJdbcInsert,batch[0].getObject(),insertProperties,usedMapping);
		simpleJdbcInsert.compileNonSync();
		int[] row;
		if(jdbcBatch){
			row  = simpleJdbcInsert.executeBatch(batch);
		}else {
			row = dbBatchInsert(simpleJdbcInsert,batch);
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
	private int[] dbBatchInsert(SimpleJdbcInsert simpleJdbcInsert,SqlParameterSource... batch){

        int count = simpleJdbcInsert.dbExecuteBatch(batch);

		return new int[]{count};

	}

}
