package org.nbone.persistence;

import java.util.Collection;
import java.util.List;

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
	public <T> Page<T> findForPage(Object object, int pageNum, int pageSize) {
		return null;
	}

	
	@Override
	public long count(Class<?> clazz) {
		return 0;
	}

	@Override
	public int deleteByEntityParams(Object object) {
		return 0;
	}

	
}
