package org.nbone.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.nbone.common.model.Pager;

/**
 * <p>Description: [公共DAO接口]</p>
 * @author chenyicheng
 * @version 1.0 
 * @since 2016-3-29
 */
public interface BaseDAO<T> {

	/**
	 * <p>Discription:[新增]</p>
	 * @param t 新增的实体对象
	 */
	public void add(T t);

	/**
	 * <p>Discription:[修改]</p>
	 * @param t 需要修改的实体Bean
	 * @return
	 */
	public Integer update(T t);
	
	/**
	 * <p>Discription:[根据查询条件修改]</p>
	 * @param t 需要修改的实体Bean
	 * @return
	 */
	public Integer updateBySelective(T t);
	
	/**
	 * <p>Discription:[根据ID删除数据]</p>
	 * @param id 主键ID
	 * @return
	 */
	public Integer delete(Object id);
	
	
	/**
	 * <p>Discription:[根据ID查询]</p>
	 * @param id 主键编号
	 * @return
	 */
	public T queryById(Object id);
	
	/**
	 * <p>Discription:[根据条件查询总数]</p>
	 * @param t Bean对象查询条件
	 * @param paramMap Bean实体外的查询参数
	 * @return 总数
	 */
	public Long queryCount(@Param("entity") T t, @Param("paramMap")Map<String, Object> paramMap);
	
	/**
	 * <p>Discription:[根据条件进行分页查询]</p>
	 * @param t Bean对象查询条件
	 * @param page 分页对象
	 * @param paramMap Bean实体外的查询参数
	 * @return Bean实体list集合
	 */
	public List<T> queryList(@Param("entity") T t, @SuppressWarnings("rawtypes") @Param("page") Pager page, @Param("paramMap")Map<String, Object> paramMap);

}
