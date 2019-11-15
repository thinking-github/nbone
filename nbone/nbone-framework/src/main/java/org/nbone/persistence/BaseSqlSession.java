package org.nbone.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nbone.framework.spring.dao.BaseJdbcDao;
import org.nbone.lang.MathOperation;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.model.SqlModel;
import org.springframework.data.domain.Page;
/**
 * 
 * @author thinking
 * @version 1.0
 * @since 2016-04-01
 */
public abstract class BaseSqlSession implements SqlSession {
	
	@Resource(name="baseJdbcDao")
	protected BaseJdbcDao baseJdbcDao;
	
	public <T> void  checkSqlModel(SqlModel<T> sqlModel) {
		if(sqlModel == null){
			throw new BuilderSQLException("build param sqlModel is null.");
		}
	}
	
	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		throw new UnsupportedOperationException("unsupported getAll operation.");
	}

	@Override
	public <T> List<T> getAll(Class<T> clazz, Collection<?> ids) {
		throw new UnsupportedOperationException("unsupported getAll operation.");
	}

	@Override
	public <T> List<T> getAll(Class<T> clazz, Object[] ids) {
		throw new UnsupportedOperationException("unsupported getAll operation.");
	}

	@Override
	public <T> List<T> getForList(Object object,SqlConfig sqlConfig) {
		throw new UnsupportedOperationException("unsupported getForList operation.");
	}
	@Override
	public <T> List<T> getForList(Map<String, ?> columnMap, SqlConfig sqlConfig) {
		throw new UnsupportedOperationException("unsupported getForList operation.");
	}

	@Override
	public <K, V> Map<K, V> getMapWithMapKey(Object object, SqlConfig sqlConfig) {
		throw new UnsupportedOperationException("unsupported getMapWithMapKey operation.");
	}
	@Override
	public <K, V> Map<K, V> getMapWithMapKey(SqlConfig sqlConfig) {
		throw new UnsupportedOperationException("unsupported getMapWithMapKey operation.");
	}

	@Override
	public <T> List<T> queryForList(Object object, SqlConfig sqlConfig) {
		throw new UnsupportedOperationException("unsupported queryForList operation.");
	}

	@Override
	public <T> List<T> findForList(Object object) {
		throw new UnsupportedOperationException("unsupported findForList operation.");
	}
	@Override
	public <T> List<T> findForList(Object object, SqlConfig sqlConfig) {
		throw new UnsupportedOperationException("unsupported findForList operation.");
	}

	@Override
	public <T> Page<T> getForPage(Object object, SqlConfig sqlConfig, int pageNum, int pageSize) {
		throw new UnsupportedOperationException("unsupported getForPage operation.");
	}
	@Override
	public <T> Page<T> getForPage(Map<String, ?> paramMap, SqlConfig sqlConfig, int pageNum, int pageSize) {
		throw new UnsupportedOperationException("unsupported getForPage operation.");
	}

	@Override
	public <T> Page<T> queryForPage(Object object,SqlConfig sqlConfig, int pageNum, int pageSize) {
		throw new UnsupportedOperationException("unsupported queryForPage operation.");
	}

	@Override
	public <T> Page<T> queryForPage(Object object, int pageNum, int pageSize, SqlConfig sqlConfig) {
		throw new UnsupportedOperationException("unsupported queryForPage operation.");
	}

	@Override
	public <T> Page<T> findForPage(Object object, int pageNum, int pageSize, String... afterWhere) {
		throw new UnsupportedOperationException("unsupported findForPage operation.");
	}

	@Override
	public <T> Page<T> findForPage(Object object, int pageNum, int pageSize,SqlConfig sqlConfig) {
		throw new UnsupportedOperationException("unsupported findForPage operation.");
	}

	@Override
	public <T> List<T> getForLimit(Object object, SqlConfig sqlConfig, int limit) {
		throw new UnsupportedOperationException("unsupported getForLimit operation.");
	}

	@Override
	public <T> List<T> getForLimit(Object object, SqlConfig sqlConfig, long offset, int limit) {
		throw new UnsupportedOperationException("unsupported getForLimit operation.");
	}

	@Override
	public <T> List<T> getForLimit(Object object, Map<String, String> operationMap, GroupQuery group,int limit,String... afterWhere) {
		throw new UnsupportedOperationException("unsupported getForLimit operation.");
	}

	@Override
	public <T> List<T> queryForLimit(Object object,SqlConfig sqlConfig, int limit) {
		throw new UnsupportedOperationException("unsupported queryForLimit operation.");
	}



	@Override
	public  <T> List<T> getForList(Object object,String fieldName,Class<T> requiredType,String... afterWhere){
		throw new UnsupportedOperationException("unsupported getForList operation.");
	}

	@Override
	public int insert(Object object) {
		throw new UnsupportedOperationException("unsupported insert operation.");
	}

	@Override
	public int insert(Class<?> entityClass, Map<String, Object> fieldMap) {
		throw new UnsupportedOperationException("unsupported insert operation.");
	}

	@Override
	public Serializable save(Object object) {
		throw new UnsupportedOperationException("unsupported save operation.");
	}

	@Override
	public Object add(Object object) {
		throw new UnsupportedOperationException("unsupported add operation.");
	}

	@Override
	public int update(Object object) {
		throw new UnsupportedOperationException("unsupported update operation.");
	}

	@Override
	public int update(Class<?> entityClass, Map<String, Object> fieldMap) {
		throw new UnsupportedOperationException("unsupported update operation.");
	}

	@Override
	public int updateSelective(Object object) {
		throw new UnsupportedOperationException("unsupported update operation.");
	}


	@Override
	public int updateSelective(Object object, Object whereEntity) {
		throw new UnsupportedOperationException("unsupported update operation.");
	}
	@Override
	public int updateSelective(Object object, Map<String, Object> whereMap) {
		throw new UnsupportedOperationException("unsupported update operation.");
	}
	@Override
	public int updateSelective(Object object,String[] properties, String whereString) {
		throw new UnsupportedOperationException("unsupported update operation.");
	}
	@Override
	public int updateSelective(Object object, String[] properties, String[] conditionFields, String whereString) {
		throw new UnsupportedOperationException("unsupported update operation.");
	}

	@Override
	public void saveOrUpdate(Object object) {
		throw new UnsupportedOperationException("unsupported update operation.");
	}

	@Override
	public int delete(Object object) {
		throw new UnsupportedOperationException("unsupported delete operation.");
	}

	@Override
	public int delete(Class<?> clazz, Serializable id) {
		throw new UnsupportedOperationException("unsupported delete operation.");
	}

	@Override
	public <T> int delete(Class<T> clazz, Object[] ids) {
		throw new UnsupportedOperationException("unsupported delete operation.");
	}
	@Override
	public int deleteByEntityParams(Object object) {
		throw new UnsupportedOperationException("unsupported deleteByEntityParams operation.");
	}
	
	@Override
	public <T> T get(Class<T> clazz, Serializable id) {
		throw new UnsupportedOperationException("unsupported  operation.");
	}

	@Override
	public <T> T get(Object object) {
		throw new UnsupportedOperationException("unsupported  operation.");
	}

	@Override
	public <T> T getOne(Object object) {
		throw new UnsupportedOperationException("unsupported  operation.");
	}

	@Override
	public long count(Class<?> clazz,String afterWhere) {
		throw new UnsupportedOperationException("unsupported  operation.");
	}

	@Override
	public long count(Object object,SqlConfig sqlConfig) {
		throw new UnsupportedOperationException("unsupported  operation.");
	}

	@Override
	public  int updateMathOperation(Object object,String property, MathOperation mathOperation) {
		throw new UnsupportedOperationException("unsupported  operation.");
	}


}
