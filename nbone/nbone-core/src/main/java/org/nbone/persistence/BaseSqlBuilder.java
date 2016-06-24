package org.nbone.persistence;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.beanutils.PropertyUtils;
import org.nbone.framework.mybatis.util.MyMapperUtils;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.mapper.DbMappingBuilder;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.TableMapper;
import org.nbone.persistence.model.SqlModel;
import org.nbone.util.PropertyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * 根据JPA注解构建sql
 * @author thinking
 * @since 2015-12-12
 * @see javax.persistence.Entity
 * @see javax.persistence.Table
 * @see javax.persistence.Id
 * @see javax.persistence.Column
 *
 */
public abstract class BaseSqlBuilder implements SqlBuilder {
	
	public final static int oxm = 1;
	public final static int annotation = 2;
	
	
	private String placeholderPrefix = "";
	private String placeholderSuffix = "";
	
	public final static String TableNameIsNullMSG = "table name  must not is null. --thinking";
	public final static String PrimaryKeyIsNullMSG = "primary Keys must not is null. --thinking";
	
	
	private JdbcFrameWork jdbcFrameWork;
	
	public BaseSqlBuilder(JdbcFrameWork jdbcFrameWork) {
		this.jdbcFrameWork = jdbcFrameWork;
		
		switch (this.jdbcFrameWork) {
		case SPRING_JDBC:
			placeholderPrefix = ":";
			placeholderSuffix ="";
			
			break;
		case MYBATIS:
			placeholderPrefix = "#{";
			placeholderSuffix = "}";
			break;
		case HIBERNATE:
			
			break;

		default:
			break;
		}
	}
	
	
	@Override
	public SqlModel<Object>  buildInsertSql(Object object) throws BuilderSQLException {
		Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
     
        TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(object.getClass());
        String tableName = tableMapper.getDbTableName();
        StringBuffer tableSql = new StringBuffer();
        StringBuffer valueSql = new StringBuffer();

        tableSql.append("insert into ").append(tableName).append("(");
        
        valueSql.append("values(");

        boolean allFieldNull = true;
        int columnCount = 0;
        for (String dbFieldName : tableMapper.getFieldMapperCache().keySet()) {
            FieldMapper fieldMapper = tableMapper.getFieldMapperCache().get(dbFieldName);
            String fieldName = fieldMapper.getFieldName();
            Object value = PropertyUtil.getProperty(object, fieldName);
            if (value == null) {
                continue;
            }
            allFieldNull = false;
            columnCount ++;
            if(columnCount > 1){
            	tableSql.append(", ");
            	valueSql.append(", ");
            }
            tableSql.append(dbFieldName);
            
            valueSql.append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
            //valueSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
        }
        if (allFieldNull) {
            throw new RuntimeException("Are you joking? Object " + object.getClass().getName()+ "'s all fields are null, how can i build sql for it?!");
        }
        
        String sql = tableSql.append(") ").append(valueSql).append(")").toString();
        
        SqlModel<Object>  model = new SqlModel<Object> (sql, object,tableMapper);
        return model;
	}

	
	@Override
	public SqlModel<Object>  buildUpdateSql(Object object) throws BuilderSQLException {
		Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
   
        TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(object.getClass());
        String tableName = tableMapper.getDbTableName();
        if(tableName == null){
        	throw new RuntimeException(TableNameIsNullMSG);     
        }
        
        // String[] uniqueKeyNames = buildUniqueKey(tableMapper);
        String[] primaryKeys = tableMapper.getPrimaryKeys();
        
        if(primaryKeys.length == 0){
             throw new RuntimeException(PrimaryKeyIsNullMSG);        	
        }

        StringBuffer tableSql = new StringBuffer();
        StringBuffer whereSql = new StringBuffer(" where ");

        tableSql.append("update ").append(tableName).append(" set ");

        boolean allFieldNull = true;
        int columnCount = 0;
        for (String dbFieldName : tableMapper.getFieldMapperCache().keySet()) {
            FieldMapper fieldMapper = tableMapper.getFieldMapperCache().get(dbFieldName);
            String fieldName = fieldMapper.getFieldName();
            Object value = PropertyUtil.getProperty(object, fieldName);
            if (value == null) {
                continue;
            }
            allFieldNull = false;
            columnCount ++;
            if(columnCount > 1){
            	tableSql.append(",");
            }
            tableSql.append(dbFieldName).append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
            //tableSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
            
        }
        if (allFieldNull) {
            throw new RuntimeException("Are you joking? Object " + object.getClass().getName()+ "'s all fields are null, how can i build sql for it?!");
        }
        
        whereSql.append(primaryKeysCondition(object, tableMapper));
 
        
        String sql = tableSql.append(whereSql).toString();
        
        SqlModel<Object>  model = new SqlModel<Object> (sql, object,tableMapper);
        return model;
	}
	
	@Override
	public SqlModel<Object>  buildUpdateSql(Object object, boolean safeAttr) throws BuilderSQLException {
		return null;
	}
	
	
	@Override
	public SqlModel<Object>  buildDeleteSql(Object object) throws BuilderSQLException {
		Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
      
        TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(object.getClass());
        String tableName = tableMapper.getDbTableName();

        StringBuffer sql = new StringBuffer();

        // delete from tableName where primaryKeyName = ?
        sql.append("delete from ").append(tableName).append(" where ");
        sql.append(primaryKeysCondition(object, tableMapper));
        
        SqlModel<Object>  model = new SqlModel<Object> (sql.toString(), object,tableMapper);
        
        return model;
	}
	
	 public <T> SqlModel<Map<String,?>> buildDeleteSqlById(Class<T> entityClass,Serializable id) throws BuilderSQLException{
		 Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");	
		 if(id == null){
				return SqlModel.EmptySqlModel;
			}
	       TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(entityClass);
	       String tableName = tableMapper.getDbTableName();
	       String[]  primaryKeys = tableMapper.getPrimaryKeys();
	       FieldMapper fieldMapper =  tableMapper.getFieldMapper(primaryKeys[0]);
			
			StringBuffer sql = new StringBuffer();
	        // delete from tableName where primaryKeyName = ?
	        sql.append("delete from ").append(tableName).append(" where ");
	        
	        sql.append(fieldMapper.getDbFieldName()).append(" = ");
	        sql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);
	        
	        Map<String,Serializable> paramsMap = new HashMap<String, Serializable>(1);
	        paramsMap.put(fieldMapper.getFieldName(), id);
	        
	        SqlModel<Map<String,?>> model = new SqlModel<Map<String,?>>(sql.toString(), paramsMap,tableMapper);
			
			
			return model;
	 }
	
	@Override
	public  <T> SqlModel<Map<String,?>> buildSelectSqlById(Class<T> entityClass,Serializable id) throws BuilderSQLException {
		Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");
		if(id == null){
			return SqlModel.EmptySqlModel;
		}
		SqlModel<T> sqlModel = selectAllTableSql(entityClass);
		
		TableMapper<T> tableMapper =  DbMappingBuilder.ME.getTableMapper(entityClass);
		String[]  primaryKeys = tableMapper.getPrimaryKeys();
		
		
		FieldMapper fieldMapper =  tableMapper.getFieldMapper(primaryKeys[0]);
		StringBuffer selectSql = new StringBuffer(sqlModel.getSql());
		selectSql.append(" where ");
		selectSql.append(fieldMapper.getDbFieldName()).append(" = ");
		selectSql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);
		
		String sql = selectSql.toString();
		
		Map<String,Serializable> paramsMap = new HashMap<String, Serializable>(1);
	    paramsMap.put(fieldMapper.getFieldName(), id);
	        
		SqlModel<Map<String,?>> model = new SqlModel<Map<String,?>>(sql,paramsMap,tableMapper);
		

		return model;
	}

	@Override
	public SqlModel<Object> buildSelectSqlById(Object object) throws BuilderSQLException {
		Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
		
		SqlModel<?> sqlModel = selectAllTableSql(object.getClass());
        
        StringBuffer selectSql = new StringBuffer(sqlModel.getSql());
        
        StringBuffer whereSql = new StringBuffer(" where ");
        whereSql.append(primaryKeysCondition(object, sqlModel.getTableMapper()));
        
        String sql = selectSql.append(whereSql).toString();
        
        SqlModel<Object> model = new SqlModel<Object>(sql,object,sqlModel.getTableMapper());
        
        return model;
	}
	
	 public <T> SqlModel<T> buildSelectAllSql(Class<T> entityClass) throws BuilderSQLException{
		 Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");
		 SqlModel<T> sqlModel = selectAllTableSql(entityClass);
		return sqlModel;
	 }
	
	@Override
	public SqlModel<Object>  buildSelectSql(Object object) throws BuilderSQLException {
		
		  SqlModel<?> sqlModel = selectAllTableSql(object.getClass());
		  
	      StringBuffer selectSql = new StringBuffer(sqlModel.getSql()).append(" where ");
	      //TODO: where 
	      String sql = selectSql.toString();
	      SqlModel<Object>  model = new SqlModel<Object> (sql, object,sqlModel.getTableMapper());

		return model;
	}
	
	
	//-----------------------------------------------------------------------------
	//
	//-----------------------------------------------------------------------------
	/**
	 * @see #primaryKeysCondition(Class, TableMapper)
	 */
	private StringBuffer primaryKeysCondition(Object object,TableMapper<?> tableMapper) throws BuilderSQLException{
		Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
		
		String[]  primaryKeys = tableMapper.getPrimaryKeys();
        for (int i = 0; i < primaryKeys.length; i++) {
            FieldMapper fieldMapper = tableMapper.getFieldMapperCache().get(primaryKeys[i]);
            String fieldName = fieldMapper.getFieldName();
            Object value = PropertyUtil.getProperty(object, fieldName);
            if (value == null) {
                throw new RuntimeException("Unique key '" + primaryKeys[i] + "' can't be null, build sql failed!");
            }
            
        }
		
		return this.primaryKeysCondition(object.getClass(), tableMapper);
	}
	/**
	 * where主键条件
	 * <p> eg: id = 1 and name = chen 
	 * @param object
	 * @param tableMapper
	 * @return
	 */
	private StringBuffer primaryKeysCondition(Class<?> entityClass,TableMapper<?> tableMapper) throws BuilderSQLException{
		
		String[]  primaryKeys = tableMapper.getPrimaryKeys();
		StringBuffer sql = new StringBuffer();
        for (int i = 0; i < primaryKeys.length; i++) {
            sql.append(primaryKeys[i]);
            FieldMapper fieldMapper = tableMapper.getFieldMapperCache().get(primaryKeys[i]);
            
            String fieldName = fieldMapper.getFieldName();
            
            if(i > 0){
            	sql.append(" and ");
           }
            sql.append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
            //sql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
        }
		
		return sql;
	}
	
	
	/***
	 * SELECT id,name,age from User
	 * @param tableMapper
	 * @return
	 */
	private <T> SqlModel<T> selectAllTableSql(Class<T> entityClass){
		TableMapper<T> tableMapper =  DbMappingBuilder.ME.getTableMapper(entityClass);
		String tableName = tableMapper.getDbTableName();

        StringBuffer selectSql = new StringBuffer();
        selectSql.append("select ");
        
        String commaDelimitedColumns = tableMapper.getCommaDelimitedColumns();
        selectSql.append(commaDelimitedColumns);
        
        selectSql.append(" from ").append(tableName);
        
        SqlModel<T>  model = new SqlModel<T> (selectSql.toString(), null,tableMapper);
		
		return model;
	}
	
	

    
    public static <E> TableMapper<E> buildTableMapper(Class<E> entityClass,String id) {
    	TableMapper<E> tableMapper;
    	//get cache 
		 if(DbMappingBuilder.ME.isTableMappered(entityClass)){
			 return DbMappingBuilder.ME.getTableMapper(entityClass);
		 }
		 
    	synchronized(DbMappingBuilder.ME){
             //load 
             tableMapper =  MyMapperUtils.resultMap2TableMapper(entityClass, id);
             DbMappingBuilder.ME.addTableMapper(entityClass, tableMapper);
    		
    	}
		return tableMapper;
    }
    
    
}
