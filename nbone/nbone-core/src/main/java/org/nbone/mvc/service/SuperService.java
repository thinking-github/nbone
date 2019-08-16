/*
 * https://github.com/thinking-github/nbone
 */
package org.nbone.mvc.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.nbone.lang.MathOperation;
import org.nbone.mvc.ISuper;
import org.nbone.mvc.domain.GroupQuery;
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
	public void batchInsert(T[] objects,boolean jdbcBatch);
	
	public void batchInsert(Collection<T> objects,boolean jdbcBatch);

	/**
	 *
	 * @param objects   实体对象列表
	 * @param  propertys 更新的属性字段 可为空，当为空更新全部字段
	 */
	public void batchUpdate(T[] objects,String...propertys);
	/**
	 *
	 * @param objects 实体对象列表
	 * @param  propertys 更新的属性字段 可为空，当为空更新全部字段
	 */
	public void batchUpdate(Collection<T> objects,String...propertys);
	
	
	public void batchDelete(Class<T> clazz,Serializable[] ids);
	
	public void batchDelete(Class<T> clazz,List<Serializable> ids);

	//查询分页
	/**
	 * getForPage(index,pageNow,pageSize,"and status != -1"," order by  create_time DESC"); <br>
	 *
	 * getForPage(index,pageNow,pageSize," order by  create_time DESC");
	 *
	 * @param object 含参数实体对象
	 * @param  fieldNames java字段名称 可为空,为空返回全部字段
	 * @param pageNum 当前页
	 * @param pageSize 页的大小
	 * @param afterWhere group by/order by 子句
	 * @return
	 */
	public Page<T> getForPage(Object object,String[] fieldNames, int pageNum,int pageSize,String... afterWhere);
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
	 * @param object 查询实体参数
	 * @param pageNum 当前页
	 * @param pageSize 页的大小
	 * @param afterWhere group by/order by 子句
	 * @return
	 */
	public Page<T> queryForPage(Object object,int pageNum,int pageSize,String... afterWhere);
	/**
	 * 
	 * @param object 查询实体参数
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
	 * @param object 查询实体参数
	 * @param group  分组查询 可为空
	 * @param limit 限制返回的大小
	 * @param afterWhere  order by 子句
	 * @return
	 */
	public List<T> getForLimit(Object object, GroupQuery group,int limit, String... afterWhere);
	/**
	 *
	 * @param object 查询实体参数
	 * @param group  分组查询 可为空
	 * @param limit 限制返回的大小
	 * @param afterWhere  order by 子句
	 * @return
	 */
	public List<T> queryForLimit(Object object,GroupQuery group,int limit,String... afterWhere);
	
	
	//按需字段查询
	/**
	 * 根据实体参数查询返回单个字段的列表(比返回整个实体数据提高效率)
	 * @param object 查询实体参数
	 * @param fieldName 单个字段名称 默认采用java property mapping
	 * @param requiredType 目标类型
	 * @param afterWhere  追加条件语句 或者 order by 子句 如: and id in (1,2,3,4)
	 * @return
	 * @see SqlSession#getForList(Object, String)
	 */
	public <E> List<E> getForList(Object object, String fieldName,Class<E> requiredType,String... afterWhere);
	
	/**
	 * 
	 * @param object 查询实体参数
	 * @param fieldNames  按需返回的java字段名称列表
	 * @param afterWhere  追加条件语句 或者 order by 子句 如: and id in (1,2,3,4)
	 * @return
	 * @see SqlSession#getForList(Object, String[], boolean)
	 */
	public List<T> getForList(Object object,String[] fieldNames,String... afterWhere);
	
	/**
	 * 
	 * @param object
	 * @param property 计算字段名称 可为空，为空时 参数值不为空且为数字的加入进行数学计算
	 * @param mathOperation
	 * @return
	 * @see org.nbone.persistence.SqlSession#updateMathOperation(Object, String, MathOperation)
	 */
	public int updateMathOperation(Object object,String property, MathOperation mathOperation);
	


}
