/*
 * https://github.com/thinking-github/nbone
 */
package org.nbone.framework.mybatis;

import java.io.Serializable;
import java.util.List;

/**
 * mybatis generator tool super mapper
 * @author thinking
 * @since 2016年3月25日下午3:15:33
 *
 * @param <T> 单个实体类型
 * @param <IdType> 主键类型 String/Long [Serializable]
 */
public interface SupperMapper<T,IdType extends Serializable> {
	
	    /**
	     * 添加一条记录
	     * @param object
	     * @return
	     */
	    int insert(T object);
	    
	    /**
	     * 添加一条记录(有选择的增加,为空的数据丢弃)
	     * @param object
	     * @return
	     */
	    int insertSelective(T object);
	    
	    /**
	     * 根据主键删除一条记录
	     * @param id
	     * @return
	     */
	    int deleteByPrimaryKey(IdType id);
	   /**
	    * 根据主键更新一条记录
	    * @param object
	    * @return
	    */
	    int updateByPrimaryKey(T object);
	   /**
	    * 根据主键更新一条记录(有选择的更新,为空的数据丢弃)
	    * @param object
	    * @return
	    */
	    int updateByPrimaryKeySelective(T object);
        /**
         * 
         * @param object
         * @return
         */
	    int updateByPrimaryKeyWithBLOBs(T object);

	    /**
	     * 根据主键查询一条记录
	     * @param id
	     * @return
	     */
	    T selectByPrimaryKey(IdType id);
	    
	    /**
	     * 根据实体Bean 字段含有值的进行查询
	     * @param object
	     * @return
	     */
	    List<T> queryForList(T object);
	
	

}
