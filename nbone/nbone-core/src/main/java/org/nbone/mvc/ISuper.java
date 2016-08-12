package org.nbone.mvc;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 超级通用接口(供BaseDao和BaseService继承使用)
 * @author thinking entity
 * @version 1.0
 * @since   2016年4月4日
 *
 * @param <T>
 * @param <PK>
 */
public interface ISuper<T,PK extends Serializable> {
	
	/**
	 * 保存数据
	 * @param object
	 * @return 返回主键id
	 */
	public PK save(T object);
	
	/**
	 *  保存数据
	 * <p></p>
	 * @param object
	 * @return 返回含有Id实体Bean
	 */
	public T add(T object);
	/**
	 *  保存数据
	 * <p></p>
	 * @param object
	 * @return 返回插入的行数
	 */
	public int insert(T object);
	
	
	/**
	 *  保存或者更新数据
	 * @param object
	 */
	
	public void saveOrUpdate(T object);
	/**
	 *  按id更新数据
	 * @param object
	 * @return
	 */
	public void update(T object);
	/**
	 * 根据主键更新一条记录(有选择的更新,为空的数据丢弃)
	 * @param object
	 */
	public void updateSelective(T object);
    
	/**
	 * 按实体中的id删除数据
	 * @param object
	 */
	public void delete(T object);
	/**
	 * 按实体中不为空属性参数删除数据
	 * @param object
	 */
	public void deleteByEntityParams(T object);
	
	/**
	 * 按id删除数据
	 * @param id
	 */
	public void delete(PK id);
	
	public void delete(PK[] ids);
	
	public void delete(Collection<?> ids);
	
	/**
	 * 按id获取数据
	 * @param id
	 * @return
	 */
	public T get(PK id);
	/**
	 * 统计全部数据的行数
	 * @return
	 */
	public long count();
	
	/**
	 * 按照实体中不为空的参数作为参数统计行数
	 * @param object
	 * @return
	 */
	public long count(T object);
	
	/**
	 * 获取数据(数据量大时不建议使用)
	 * @return
	 */
	public List<T> getAll();
	
	public List<T> getAll(PK[] ids);
	
	public List<T> getAll(Collection<?> ids);
	
	/**
	 * 按照实体中的参数查询实体列表
	 * @param object
	 * @return {@link List}
	 */
	public List<T> getForList(T object);
	
	/**
	 * 按照实体中的参数查询实体列表（特殊情况下不同得实现方式）
	 * @param object
	 * @return  {@link List}
	 */
	public List<T> queryForList(T object);
	
	
	
	

}
