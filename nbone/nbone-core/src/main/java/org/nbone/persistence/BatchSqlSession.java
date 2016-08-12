package org.nbone.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
/**
 * 
 * 以实体Bean的方式实现对象的批量的  增/删/改/查
 * @author thinking
 * @since 2015-12-12
 */
public interface BatchSqlSession extends SqlSession {
	
	/**
	 * 批量添加
	 * @param objects
	 * @return
	 */
	public int[] batchInsert(Object[] objects);
	
	/**
	 * 批量添加
	 * @param objects
	 * @return
	 */
	public int[] batchInsert(Collection<?> objects);
	
	/**
	 * 批量更新
	 * @param objects
	 * @return
	 */
	public int[] batchUpdate(Object[] objects);
	
	/**
	 * 批量更新
	 * @param objects
	 * @return
	 */
	public int[] batchUpdate(Collection<?> objects);
	/**
	 * 批量删除(不用in语句实现)
	 * @param objects
	 * @return
	 */
	public <T> int[] batchDelete(Class<T> clazz,Serializable[] ids);
	
	/**
	 * 批量删除(不用in语句实现)
	 * @param objects
	 * @return
	 */
	public <T> int[] batchDelete(Class<T> clazz,List<Serializable> ids);
	

}
