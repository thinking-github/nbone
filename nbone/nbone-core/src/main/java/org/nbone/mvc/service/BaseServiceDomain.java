package org.nbone.mvc.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.nbone.mvc.BaseObject;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.SqlSession;
import org.springframework.util.Assert;

/**
 * 
 * @author thinking
 * @since 2016年3月25日下午3:39:45
 *
 * @param <T>
 * @param <IdType>
 */
public  class BaseServiceDomain<P,IdType extends Serializable> extends BaseObject implements SuperService<P, IdType>{
	
	@Resource
	private SqlSession namedJdbcDao;
	
	private Class<P> targetClass;
	
	private String namespace;
	private String id;
	
	private  boolean builded =  false;
	
	private static long count = 0;

	
	public BaseServiceDomain() {
		count ++;
	}



	protected synchronized void  builded(){
	    Assert.notNull(namespace, "namespace is not null.thinking");
	    Assert.notNull(id, "id is not null.thinking");
	    if(!builded){
	    	  try {
	  	    	BaseSqlBuilder.buildTableMapper(targetClass,namespace,id);
	  	    	builded =  true;
	  		  } catch (Exception e) {
	  			builded =  false;
	  			logger.error(e.getMessage(),e);
	  		  }
	  		
	    }
	  
		
	}
	
	protected  void checkBuilded(){
		if(!builded){
			builded();
		}
	}
	
	
    public SqlSession getNamedJdbcDao() {
		return namedJdbcDao;
	}

	public void setNamedJdbcDao(SqlSession namedJdbcDao) {
		this.namedJdbcDao = namedJdbcDao;
	}

	public Class<P> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<P> targetClass) {
		this.targetClass = targetClass;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static long getCount() {
		return count;
	}

	public static void setCount(long count) {
		BaseServiceDomain.count = count;
	}

	//--------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	public IdType save(P object) {
		checkBuilded();
		Serializable id  = namedJdbcDao.save(object);
		return (IdType) id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public P add(P object) {
		checkBuilded();
		object = (P) namedJdbcDao.add(object);
		return object;
	}

	@Override
	public int insert(P object) {
		checkBuilded();
		int row = namedJdbcDao.insert(object);
		return row;
	}

	@Override
	public void update(P object) {
		checkBuilded();
		namedJdbcDao.updateSelective(object);
	}
	
	@Override
	public void updateSelective(P object) {
		checkBuilded();
		namedJdbcDao.updateSelective(object);
	}


	@Override
	public void delete(IdType id) {
		checkBuilded();
		namedJdbcDao.delete(targetClass, id);
	}

	@Override
	public P get(IdType id) {
		checkBuilded();
		 P bean = namedJdbcDao.get(targetClass, id);
		return bean;
	}

	@Override
	public void saveOrUpdate(P object) {
		
	}

	@Override
	public void delete(P object) {
		checkBuilded();
		namedJdbcDao.delete(object);
	}

	@Override
	public List<P> getAll() {
		checkBuilded();
		List<P> beans = namedJdbcDao.getAll(targetClass);
		return beans;
	}

	@Override
	public List<P> getForList(P object) {
		checkBuilded();
		List<P> beans =  namedJdbcDao.getForList(object);
		return beans;
	}

	@Override
	public List<P> queryForList(P object) {
		checkBuilded();
		List<P> beans  = namedJdbcDao.queryForList(object);	
		return beans;
	}


	@Override
	public void delete(IdType[] ids) {
		checkBuilded();
		namedJdbcDao.delete(targetClass, ids);
	}


	@Override
	public List<P> getAll(IdType[] ids) {
		checkBuilded();
		List<P> beans = namedJdbcDao.getAll(targetClass, ids);
		return beans;
	}







}
