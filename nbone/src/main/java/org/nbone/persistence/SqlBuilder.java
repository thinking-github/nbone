package org.nbone.persistence;

import org.nbone.persistence.mapper.TableMapper;
import org.nbone.persistence.model.SqlModel;

/**
 * 构建ORM映射超级接口
 * @author thinking
 * @since 2015-12-12
 * 
 */
public interface SqlBuilder {
	
	/**
	 * 由传入的对象生成insert sql语句
	 * @param object
	 * @return {@link SqlModel}
	 * @throws Exception
	 */
	 public SqlModel buildInsertSql(Object object,TableMapper<?> tableMapper) throws Exception;
	 
	 /**
	  * 由传入的对象生成update sql语句
	  * @param object
	  * @return
	  * @throws Exception
	  */
	 public SqlModel buildUpdateSql(Object object,TableMapper<?> tableMapper) throws Exception;
	 
	 
	 /**
	  * 由传入的对象生成update sql语句(可以启用安全属性设置,即为空的属性值不进行更新)
	  * @param object
	  * @param safeAttr 
	  * @return
	  * @throws Exception
	  */
	 public SqlModel buildUpdateSql(Object object,TableMapper<?> tableMapper,boolean safeAttr) throws Exception;
	 
	 /**
	  * 由传入的对象生成delete sql语句
	  * @param object
	  * @return
	  * @throws Exception
	  */
	 public SqlModel buildDeleteSql(Object object,TableMapper<?> tableMapper) throws Exception;
	 
	 /**
	  * 
	  * @param object
	  * @return
	  * @throws Exception
	  */
	 public SqlModel buildSelectSql(Object object,TableMapper<?> tableMapper) throws Exception;

}
