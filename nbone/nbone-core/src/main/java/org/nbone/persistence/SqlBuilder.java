package org.nbone.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.model.SqlModel;

/**
 * 构建ORM映射超级接口
 * @author thinking
 * @since 2015-12-12
 * 
 */
public interface SqlBuilder {
	
	/**
	 * 由传入的对象的参数生成insert sql语句(不校验空值)
	 * @param object
	 * @return {@link SqlModel}
	 * @throws BuilderSQLException
	 */
	 public SqlModel<Object> buildInsertSql(Object object) throws BuilderSQLException;
	/**
	 * 由传入的对象的参数生成insert sql语句(参数值不为空的加入)
	 * @param object
	 * @return {@link SqlModel}
	 * @throws BuilderSQLException
	 */
	 public SqlModel<Object> buildInsertSelectiveSql(Object object) throws BuilderSQLException;
	 
	 /**
	  * 由传入的对象生成update sql语句(参数值不为空的加入)(启用安全属性设置,即为空的属性值不进行更新)
	  * @param object
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> buildUpdateSelectiveSql(Object object) throws BuilderSQLException;
	 
	 
	 /**
	  * 由传入的对象生成update sql语句
	  * @param object
	  * @param safeAttr 
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> buildUpdateSql(Object object) throws BuilderSQLException;
	 
	 /**
	  * 由传入的对象生成delete sql语句
	  * @param object
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> buildDeleteSql(Object object) throws BuilderSQLException;
	 
	 /**
	  * 根据主键Id删除
	  * @param entityClass
	  * @param id
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<Map<String,?>> buildDeleteSqlById(Class<T> entityClass,Serializable id) throws BuilderSQLException;
	 
	 /**
	  * 根据主键列表Id删除
	  * @param entityClass
	  * @param id
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> buildDeleteSqlByIds(Class<T> entityClass,Object[] ids) throws BuilderSQLException;
	
	 /**
	  * 根据主键Id查询
	  * @param id
	  * @param entityClass
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<Map<String,?>> buildSelectSqlById(Class<T> entityClass,Serializable id) throws BuilderSQLException;
	 /**
	  * 根据实体中的主键Id查询
	  * @param object
	  * @return
	  * @throws BuilderSQLException
	  */
	 public SqlModel<Object> buildSelectSqlById(Object object) throws BuilderSQLException;
	 /**
	  * 查询全表sql(小数量时使用)
	  * @param entityClass
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> buildSelectAllSql(Class<T> entityClass) throws BuilderSQLException;
	 /**
	  * 统计单表的数量行数
	  * @param entityClass
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> buildCountSql(Class<T> entityClass) throws BuilderSQLException;
	 
	 
	 /**
	  * 根据主键列表查询
	  * @param entityClass
	  * @param ids
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> buildSelectSqlByIds(Class<T> entityClass,Collection<?> ids) throws BuilderSQLException;
	 /**
	  * 根据主键列表查询
	  * @param entityClass
	  * @param ids
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> buildSelectSqlByIds(Class<T> entityClass,Object[] ids) throws BuilderSQLException;
	 
	 /**
	  * 根据实体中的参数查询(全部使用等号)
	  * @param object
	  * @return
	  * @throws Exception
	  */
	 public <T> SqlModel<T> buildSelectSql(Object object) throws BuilderSQLException;
	 
	 /**
	  * 根据实体中的参数查询
	  * <p> number use = ;String use Like
	  * @param object
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> buildSimpleSelectSql(Object object) throws BuilderSQLException;
	 
	 /**
	  * 根据实体中的参数查询
	  * @param object
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> buildMiddleModeSelectSql(Object object) throws BuilderSQLException;
	 /**
	  * 根据实体中的参数查询
	  * @param object
	  * @return
	  * @throws BuilderSQLException
	  */
	 public <T> SqlModel<T> buildHighModeSelectSql(Object object) throws BuilderSQLException;
	 
	 public  SqlModel<Map<String,Object>> buildObjectModeSelectSql(Object object,SqlConfig sqlConfig) throws BuilderSQLException;
	 
	 
	 

}
