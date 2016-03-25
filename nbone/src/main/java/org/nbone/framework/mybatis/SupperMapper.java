/*
 * https://github.com/thinking-github/nbone
 */
package org.nbone.framework.mybatis;

import java.io.Serializable;

/**
 * mybatis generator tool super mapper
 * @author thinking
 * @since 2016年3月25日下午3:15:33
 *
 * @param <T>
 * @param <IdType>
 */
public interface SupperMapper<T,IdType extends Serializable> {
	
	    int insert(T object);
	    
	    int insertSelective(T object);
	    
	    int deleteByPrimaryKey(IdType id);
	   
	    int updateByPrimaryKey(T object);
	   
	    int updateByPrimaryKeySelective(T object);

	    int updateByPrimaryKeyWithBLOBs(T object);

	    T selectByPrimaryKey(IdType id);
	
	

}
