package org.nbone.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.nbone.lang.MathOperation;
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
	
	
	public <T> void  checkSqlModel(SqlModel<T> sqlModel) {
		if(sqlModel == null){
			throw new BuilderSQLException("sqlModel is null.");
		}
	}
	
	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		return null;
	}

	@Override
	public <T> List<T> getAll(Class<T> clazz, Collection<?> ids) {
		return null;
	}

	@Override
	public <T> List<T> getAll(Class<T> clazz, Object[] ids) {
		return null;
	}

	@Override
	public <T> List<T> getForList(Object object) {
		return null;
	}

	@Override
	public <T> List<T> queryForList(Object object) {
		return null;
	}

	@Override
	public <T> List<T> queryForList(Object object, SqlConfig sqlConfig) {
		return null;
	}

	@Override
	public <T> List<T> findForList(Object object) {
		return null;
	}
	
	@Override
	public <T> Page<T> getForPage(Object object, int pageNum, int pageSize) {
		return null;
	}

	@Override
	public <T> Page<T> queryForPage(Object object, int pageNum, int pageSize) {
		return null;
	}
	
	@Override
	public <T> Page<T> findForPage(Object object, int pageNum, int pageSize) {
		return null;
	}

	
	

	@Override
	public <T> List<T> getForList(Object object, String fieldName) {
		return null;
	}
	
	public  <T> List<T> getForList(Object object,String fieldName,Class<T> requiredType){
		return null;
	}

	@Override
	public <T> List<T> getForListWithFieldNames(Object object, String[] fieldNames,boolean dbFieldMode) {
		return null;
	}
	
	

	@Override
	public int insert(Object object) {
		return 0;
	}

	@Override
	public Serializable save(Object object) {
		return null;
	}

	@Override
	public Object add(Object object) {
		return null;
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
	public int deleteByEntityParams(Object object) {
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
	public long count(Class<?> clazz) {
		return 0;
	}

	@Override
	public long count(Object object) {
		return 0;
	}

	@Override
	public  int updateMathOperation(Object object, MathOperation mathOperation) {
		return 0;
	}
	

	
}
