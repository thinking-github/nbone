package org.nbone.mvc.service;

import java.io.Serializable;
import java.util.List;

import org.nbone.framework.mybatis.SupperMapper;

/**
 * 
 * @author thinking
 * @since 2016年3月25日下午3:39:45
 *
 * @param <T>
 * @param <IdType>
 */
public  class BaseServiceDto<T,IdType extends Serializable> implements SuperService<T, IdType>{

	private SupperMapper<T, IdType> superMapper;
	

	protected void setSuperMapper(SupperMapper<T, IdType> superMapper) {
		this.superMapper = superMapper;
	}
	
    //--------------------------------------------------------------------
	@Override
	public IdType save(T object) {
		return null;
	}

	@Override
	public T add(T object) {
		insert(object);
		return object;
	}

	@Override
	public int insert(T object) {
		return superMapper.insert(object);
	}

	@Override
	public void update(T object) {
		superMapper.updateByPrimaryKey(object);
	}

	@Override
	public int updateNotNull(T object) {
		return superMapper.updateByPrimaryKeySelective(object);
	}

	@Override
	public void delete(IdType id) {
		superMapper.deleteByPrimaryKey(id);
	}

	@Override
	public T get(IdType id) {
		return superMapper.selectByPrimaryKey(id);
	}

	@Override
	public void saveOrUpdate(T object) {
		
	}

	@Override
	public void delete(T object) {
		
	}

	@Override
	public List<T> getAll() {
		return null;
	}

	@Override
	public List<T> getForList(T object) {
		return null;
	}

	@Override
	public List<T> queryForList(T object) {
		return null;
	}



}
