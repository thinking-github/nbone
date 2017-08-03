package org.nbone.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nbone.framework.spring.dao.BaseJdbcDao;
import org.nbone.lang.MathOperation;
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
			throw new BuilderSQLException("sqlModel is null.");
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
	public <T> List<T> getForList(Object object,String afterWhere) {
		throw new UnsupportedOperationException("unsupported getForList operation.");
	}
	@Override
	public <T> List<T> getForList(Object object, FieldLevel fieldLevel,String afterWhere) {
		throw new UnsupportedOperationException("unsupported getForList operation.");
	}

	@Override
	public <T> List<T> queryForList(Object object,String afterWhere) {
		throw new UnsupportedOperationException("unsupported queryForList operation.");
	}
	@Override
	public <T> List<T> queryForList(Object object, FieldLevel fieldLevel,String afterWhere) {
		throw new UnsupportedOperationException("unsupported queryForList operation.");
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
	public <T> List<T> findForList(Object object, FieldLevel fieldLevel) {
		throw new UnsupportedOperationException("unsupported findForList operation.");
	}
	
	
	@Override
	public <T> Page<T> getForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		throw new UnsupportedOperationException("unsupported getForPage operation.");
	}

	@Override
	public <T> Page<T> queryForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		throw new UnsupportedOperationException("unsupported queryForPage operation.");
	}
	
	@Override
	public <T> Page<T> findForPage(Object object, int pageNum, int pageSize,String... afterWhere) {
		throw new UnsupportedOperationException("unsupported findForPage operation.");
	}


	
	public  <T> List<T> getForList(Object object,String fieldName,Class<T> requiredType){
		throw new UnsupportedOperationException("unsupported getForList operation.");
	}

	@Override
	public <T> List<T> getForListWithFieldNames(Object object, String[] fieldNames,boolean dbFieldMode) {
		throw new UnsupportedOperationException("unsupported getForListWithFieldNames operation.");
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
	public long count(Class<?> clazz) {
		throw new UnsupportedOperationException("unsupported  operation.");
	}

	@Override
	public long count(Object object) {
		throw new UnsupportedOperationException("unsupported  operation.");
	}

	@Override
	public  int updateMathOperation(Object object, MathOperation mathOperation) {
		throw new UnsupportedOperationException("unsupported  operation.");
	}

	
}
