package org.nbone.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
