package org.nbone.framework.mybatis;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.nbone.persistence.SqlSession;
/**
 * 
 * @author thinking
 *
 */
public class MySqlSessionTemplate extends SqlSessionTemplate implements SqlSession  {

	public MySqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	public int insert(Object object,String resultMapId) {
		
		
		// TODO Auto-generated method stub
		return 0;
	}

	public int update(Object object,String resultMapId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int delete(Object object,String resultMapId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public <T> T get(Class<T> clazz, Serializable id,String resultMapId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	//---------------------------------------------------------------------
	@Override
	public int insert(Object object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Object object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Object object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Class<?> clazz, Serializable id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T get(Class<T> clazz, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> getByBeanParams(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
