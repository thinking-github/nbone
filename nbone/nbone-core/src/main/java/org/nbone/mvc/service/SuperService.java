/*
 * https://github.com/thinking-github/nbone
 */
package org.nbone.mvc.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.nbone.lang.MathOperation;
import org.nbone.mvc.ISuper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

/**
 * Supper Service interface
 * @author thinking
 * @since 2015年12月12日下午1:45:26
 *
 * @param <T>
 * @param <IdType>
 */
public interface SuperService<T,IdType extends Serializable> extends ISuper<T, IdType> {
	
	//static Logger logger = LoggerFactory.getLogger(SuperService.class);

	//批量增删改
	/**
	 * @param objects
	 */
	public void batchInsert(T[] objects);
	
	public void batchInsert(Collection<T> objects);
	
	
	public void batchUpdate(T[] objects);
	
	public void batchUpdate(Collection<T> objects);
	
	
	public void batchDelete(Class<T> clazz,Serializable[] ids);
	
	public void batchDelete(Class<T> clazz,List<Serializable> ids);
	
	
	//查询分页
	/**
	 * getForPage(index,pageNow,pageSize,"and status != -1"," order by  create_time DESC"); <br>
	 *
	 * getForPage(index,pageNow,pageSize," order by  create_time DESC");
	 * 
	 * @param object 
	 * @param pageNum 当前页
	 * @param pageSize 页的大小
	 * @param afterWhere group by/order by 子句
	 * @return
	 */
	public Page<T> getForPage(Object object,int pageNum,int pageSize,String... afterWhere);
	/**
	 * queryForPage(index,pageNow,pageSize,"and status != -1"," order by  create_time DESC"); <br>
	 *
	 * queryForPage(index,pageNow,pageSize," order by  create_time DESC");
	 * @param object 
	 * @param pageNum 当前页
	 * @param pageSize 页的大小
	 * @param afterWhere group by/order by 子句
	 * @return
	 */
	public Page<T> queryForPage(Object object,int pageNum,int pageSize,String... afterWhere);
	/**
	 * 
	 * @param object 
	 * @param pageNum 当前页
	 * @param pageSize 页的大小
	 * @param afterWhere group by/order by 子句
	 * @return
	 */
	public Page<T> findForPage(Object object,int pageNum,int pageSize,String... afterWhere);


	/**
	 * getForLimit(index,10,"and status != -1"," order by  create_time DESC"); <br>
	 *
	 * getForLimit(index,10," order by  create_time DESC");
	 * @param object
	 * @param limit
	 * @param afterWhere
	 * @return
	 */
	public List<T> getForLimit(Object object,int limit,String... afterWhere);

	public List<T> queryForLimit(Object object,int limit,String... afterWhere);
	
	
	//按需字段查询
	/**
	 * 根据实体参数查询返回单个字段的列表(比返回整个实体数据提高效率)
	 * @param object
	 * @param fieldName 单个字段名称 默认采用java property mapping
	 * @param requiredType 目标类型
	 * @return
	 * @see SqlSession#getForList(Object, String)
	 */
	public <E> List<E> getForList(Object object, String fieldName,Class<E> requiredType);
	
	/**
	 * 
	 * @param object
	 * @param fieldNames
	 * @return
	 * @see SqlSession#getForListWithFieldNames(Object, String[], boolean)	
	 */
	public List<T> getForListWithFieldNames(Object object,String[] fieldNames);
	
	/**
	 * 
	 * @param object
	 * @param mathOperation
	 * @return
	 * @see SqlSession#updateMathOperation(Object, MathOperation)
	 */
	public int updateMathOperation(Object object, MathOperation mathOperation);
	


}
