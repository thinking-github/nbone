package org.nbone.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nbone.framework.mybatis.util.MyMapperUtils;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.mapper.DbMappingBuilder;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.TableMapper;
import org.nbone.persistence.model.SqlModel;
import org.nbone.persistence.util.SqlUtils;
import org.nbone.util.PropertyUtil;
import org.nbone.util.reflect.SimpleTypeMapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;

/**
 * 根据JPA注解构建sql
 * @author thinking
 * @since 2015-12-12
 * @version 1.0
 * @see javax.persistence.Entity
 * @see javax.persistence.Table
 * @see javax.persistence.Id
 * @see javax.persistence.Column
 *
 */
@SuppressWarnings("unchecked")
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
			placeholderPrefix = ":";
			placeholderSuffix ="";
			break;

		default:
			placeholderPrefix = "";
			placeholderSuffix = "";
			break;
		}
	}
	
	
	@Override
	public SqlModel<Object> buildInsertSql(Object object) throws BuilderSQLException {
		Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
		
		
		return null;
	}


	@Override
	public SqlModel<Object>  buildInsertSelectiveSql(Object object) throws BuilderSQLException {
		Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
     
        TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(object.getClass());
        String tableName = tableMapper.getDbTableName();
        StringBuffer tableSql = new StringBuffer();
        StringBuffer valueSql = new StringBuffer();

        tableSql.append("insert into ").append(tableName).append("(");
        
        valueSql.append("values(");

        boolean allFieldNull = true;
        int columnCount = 0;
        for (String dbFieldName : tableMapper.getFieldMappers().keySet()) {
            FieldMapper fieldMapper = tableMapper.getFieldMappers().get(dbFieldName);
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
	public SqlModel<Object>  buildUpdateSelectiveSql(Object object) throws BuilderSQLException {
		Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
   
        TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(object.getClass());
        String tableName = tableMapper.getDbTableName();
        if(tableName == null){
        	throw new RuntimeException(TableNameIsNullMSG);     
        }
        
        String[] primaryKeys = tableMapper.getPrimaryKeys();
        
        if(primaryKeys.length == 0){
             throw new RuntimeException(PrimaryKeyIsNullMSG);        	
        }

        StringBuffer tableSql = new StringBuffer();
        StringBuffer whereSql = new StringBuffer(" where ");

        tableSql.append("update ").append(tableName).append(" set ");

        boolean allFieldNull = true;
        int columnCount = 0;
        for (String dbFieldName : tableMapper.getFieldMappers().keySet()) {
            FieldMapper fieldMapper = tableMapper.getFieldMappers().get(dbFieldName);
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
	public SqlModel<Object>  buildUpdateSql(Object object) throws BuilderSQLException {
		
		Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
		   
        TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(object.getClass());
        String tableName = tableMapper.getDbTableName();
        if(tableName == null){
        	throw new RuntimeException(TableNameIsNullMSG);     
        }
        String[] primaryKeys = tableMapper.getPrimaryKeys();
        
        if(primaryKeys.length == 0){
             throw new RuntimeException(PrimaryKeyIsNullMSG);        	
        }

        StringBuffer tableSql = new StringBuffer();
        tableSql.append("update ").append(tableName).append(" set ");

        int columnCount = 0;
        for (String dbFieldName : tableMapper.getFieldMappers().keySet()) {
            FieldMapper fieldMapper = tableMapper.getFieldMappers().get(dbFieldName);
            String fieldName = fieldMapper.getFieldName();
            if(fieldMapper.isPrimaryKey()){
            	continue;
            }
            
            columnCount ++;
            if(columnCount > 1){
            	tableSql.append(",");
            }
            tableSql.append(dbFieldName).append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
            //tableSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
        }
        
        
        StringBuffer whereSql = new StringBuffer(" where ");
        whereSql.append(primaryKeysCondition(object, tableMapper));
        String sql = tableSql.append(whereSql).toString();
        
        SqlModel<Object>  model = new SqlModel<Object> (sql, object,tableMapper);
        return model;
		
	}
	
	
	@Override
	public SqlModel<Object>  buildDeleteSql(Object object) throws BuilderSQLException {
		Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
      
        TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(object.getClass());

        StringBuffer sql = new StringBuffer();

        // delete from tableName where primaryKeyName = ?
        sql.append(tableMapper.getDeleteAllSql()).append(" where ");
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
	       String[]  primaryKeys = tableMapper.getPrimaryKeys();
	       FieldMapper fieldMapper =  tableMapper.getFieldMapper(primaryKeys[0]);
			
			StringBuffer sql = new StringBuffer();
	        // delete from tableName where primaryKeyName = ?
	        sql.append(tableMapper.getDeleteAllSql()).append(" where ");
	        
	        sql.append(fieldMapper.getDbFieldName()).append(" = ");
	        sql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);
	        
	        Map<String,Serializable> paramsMap = new HashMap<String, Serializable>(1);
	        paramsMap.put(fieldMapper.getFieldName(), id);
	        
	        SqlModel<Map<String,?>> model = new SqlModel<Map<String,?>>(sql.toString(), paramsMap,tableMapper);
			
			
			return model;
	 }
	 
	 public <T> SqlModel<T> buildDeleteSqlByIds(Class<T> entityClass,Object[] ids) throws BuilderSQLException{
		   Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");	
		   if(ids == null){
				return SqlModel.EmptySqlModel;
		   }
	       TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(entityClass);
	       String[]  primaryKeys = tableMapper.getPrimaryKeys();
	       FieldMapper fieldMapper =  tableMapper.getFieldMapper(primaryKeys[0]);
			
			StringBuffer sql = new StringBuffer();
	        // delete from tableName where primaryKeyName = ?
	        sql.append(tableMapper.getDeleteAllSql()).append(" where ");
	        
	        StringBuilder in =  SqlUtils.list2In(fieldMapper.getDbFieldName(), ids);
	        sql.append(in);
	        
	        SqlModel<T> model = new SqlModel<T>(sql.toString(),null,tableMapper);
	        model.setParameterArray(ids);
			return model;
		 
	 }
	
	@Override
	public  <T> SqlModel<Map<String,?>> buildSelectSqlById(Class<T> entityClass,Serializable id) throws BuilderSQLException {
		Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");
		if(id == null){
			return SqlModel.EmptySqlModel;
		}
		TableMapper<T> tableMapper =  DbMappingBuilder.ME.getTableMapper(entityClass);
		StringBuilder all = tableMapper.getSelectAllSql();
		String[]  primaryKeys = tableMapper.getPrimaryKeys();
		
		
		FieldMapper fieldMapper =  tableMapper.getFieldMapper(primaryKeys[0]);
		StringBuffer selectSql = new StringBuffer(all);
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
		TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(object.getClass());
        
        StringBuffer selectSql = new StringBuffer(tableMapper.getSelectAllSql());
        
        StringBuffer whereSql = new StringBuffer(" where ");
        whereSql.append(primaryKeysCondition(object,tableMapper));
        
        String sql = selectSql.append(whereSql).toString();
        
        SqlModel<Object> model = new SqlModel<Object>(sql,object,tableMapper);
        
        return model;
	}
	
	 public <T> SqlModel<T> buildSelectAllSql(Class<T> entityClass) throws BuilderSQLException{
		 Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");
		 TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(entityClass);
	     String sql = tableMapper.getSelectAllSql().toString();
		 SqlModel<T> sqlModel = new SqlModel<T>(sql, null, tableMapper);
		return sqlModel;
	 }
	 
	 public <T> SqlModel<T> buildCountSql(Class<T> entityClass) throws BuilderSQLException{
		 TableMapper<T> tableMapper =  DbMappingBuilder.ME.getTableMapper(entityClass);
		 return new SqlModel<T>(tableMapper.getCountSql().toString(), null, tableMapper);
	 }
	 
	 public <T> SqlModel<T> buildSelectSqlByIds(Class<T> entityClass,Collection<?> ids) throws BuilderSQLException{
		return buildSelectSqlByIds(entityClass, ids.toArray());
	 }
	 public <T> SqlModel<T> buildSelectSqlByIds(Class<T> entityClass,Object[] ids) throws BuilderSQLException{
		 Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");
		 TableMapper<T> tableMapper =  DbMappingBuilder.ME.getTableMapper(entityClass);
		 StringBuilder allsql = tableMapper.getSelectAllSql();
		 
		 String[]  primaryKeys =tableMapper.getPrimaryKeys();
		 StringBuilder in  =   SqlUtils.list2In(primaryKeys[0], ids);
		 
		 StringBuilder sql = new StringBuilder(allsql).append(" where ").append(in);
		
		 SqlModel<T> sqlModel = new SqlModel<T>(sql.toString(), null, tableMapper);
		 sqlModel.setParameterArray(ids);
		return sqlModel;
	 }
	 
	 
	@Override
	public <T> SqlModel<T>  buildSelectSql(Object object) throws BuilderSQLException {
		SqlModel<T> model = (SqlModel<T>) buildSelectSql(object,SqlConfig.getSqlConfig(-1));

		return model;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> SqlModel<T> buildSimpleSelectSql(Object object) throws BuilderSQLException {
		SqlModel<T> model = (SqlModel<T>) buildSelectSql(object, SqlConfig.getSqlConfig(SqlConfig.PrimaryMode));
		
		return model;
	}

	@Override
	public <T> SqlModel<T> buildMiddleModeSelectSql(Object object) throws BuilderSQLException {
		SqlModel<T> model = (SqlModel<T>) buildSelectSql(object,  SqlConfig.getSqlConfig(SqlConfig.MiddleMode));
		return model;
	}

	@Override
	public <T> SqlModel<T> buildHighModeSelectSql(Object object) throws BuilderSQLException {
		SqlModel<T> model = (SqlModel<T>) buildSelectSql(object, SqlConfig.getHighMode());
		return model;
	}
	
	 public  SqlModel<Map<String,Object>>  buildObjectModeSelectSql(Object object,SqlConfig sqlConfig) throws BuilderSQLException{
		 
		    Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
			
		    
		    TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(object.getClass());
		    StringBuilder allsql = tableMapper.getSelectAllSql();
		    
	        StringBuffer selectSql = new StringBuffer(allsql).append(" where 1 = 1 ");
	        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(object);
	      
	        List<FieldMapper> fields =  tableMapper.getFieldMapperList();
	        Map<String,Object> namedParameters =  new HashMap<String, Object>();
	        
	        for (FieldMapper fieldMapper : fields) {
	    		String fieldName = fieldMapper.getFieldName();
	    		String dbFieldName = fieldMapper.getDbFieldName();
				Class<?> fieldType  = fieldMapper.getPropertyType();
				if(fieldType == Class.class){
					continue;
				}
				Object fieldValue  = bw.getPropertyValue(fieldName);
				SqlPropertyDescriptor sqlPropertyDescriptor = sqlConfig.getSqlPd(fieldName);
				//XXX：特殊情况可为空
				if(fieldValue == null &&  sqlPropertyDescriptor != null){
					String operType = sqlPropertyDescriptor.getOperType();
					if(SqlPropertyDescriptor.is_null.equalsIgnoreCase(operType) || SqlPropertyDescriptor.is_not_null.equalsIgnoreCase(operType)){
						
						StringBuilder sqlpd = SqlUtils.getCommonWherePart(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);
						selectSql.append(sqlpd);
						continue;
						
					}else if(SqlPropertyDescriptor.in.equalsIgnoreCase(operType)|| SqlPropertyDescriptor.not_in.equalsIgnoreCase(operType)){
						
						StringBuilder sqlpd = SqlUtils.getSpringJdbcWhereIn(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);
						selectSql.append(sqlpd);
						continue;
						
					}else if(sqlPropertyDescriptor.isBetween()){
						StringBuilder sqlpd = SqlUtils.getWhereBetween(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);
						selectSql.append(sqlpd);
						continue;
					}
				}
				
				if( fieldValue != null ){
					if(sqlPropertyDescriptor ==  null ){
						sqlPropertyDescriptor = new SqlPropertyDescriptor(dbFieldName);
						
					}
					StringBuilder sqlpd = SqlUtils.getSpringJdbcWhere(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);
					//StringBuilder sqlpd = SqlUtils.getCommonWherePart(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);
					
					selectSql.append(sqlpd);
				}
					
			}
	        
	          //:XXX Range beginTime <=  value  <= endTime
	          List<SqlPropertyRange> sqlPropertyRanges = sqlConfig.getSqlPropertyRanges();
			  if(sqlPropertyRanges != null && sqlPropertyRanges.size() > 0 ){
					for (SqlPropertyRange sqlPropertyRange : sqlPropertyRanges) {
						String dbLeftField = tableMapper.getDbFieldName(sqlPropertyRange.getLeftField());
						String dbRightField = tableMapper.getDbFieldName(sqlPropertyRange.getRightField());
						sqlPropertyRange.setDbLeftField(dbLeftField);
						sqlPropertyRange.setDbRightField(dbRightField);
						StringBuilder rangeSql  = SqlUtils.getPropertyRange(sqlPropertyRange, namedParameters);
					    if(rangeSql != null){
			        	  selectSql.append(rangeSql);
			            }
					}
					
			  }
	        
	         //order by 
	          StringBuilder orderSql  = SqlUtils.getOrderBy(sqlConfig, false);
	          if(orderSql != null){
	        	  selectSql.append(orderSql);
	          }
	          
			  String sql = selectSql.toString();
		      SqlModel<Map<String,Object>>  model = new SqlModel<Map<String,Object>> (sql, namedParameters,tableMapper);
			return  model;
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
            FieldMapper fieldMapper = tableMapper.getFieldMappers().get(primaryKeys[i]);
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
            FieldMapper fieldMapper = tableMapper.getFieldMappers().get(primaryKeys[i]);
            
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
	/*public <T> SqlModel<T> selectAllTableSql(Class<T> entityClass){
		
	}*/
	
	
	private SqlModel<Object> buildSelectSql(Object object,SqlConfig sqlConfig){
		Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
		
	    TableMapper<?> tableMapper =  DbMappingBuilder.ME.getTableMapper(object.getClass());
	    StringBuilder allsql = tableMapper.getSelectAllSql();
	    
        StringBuffer selectSql = new StringBuffer(allsql).append(" where 1 = 1 ");
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(object);
      
        List<FieldMapper> fields =  tableMapper.getFieldMapperList();
        int levelMode = sqlConfig.getSqlMode();
        for (FieldMapper fieldMapper : fields) {
    		String fieldName = fieldMapper.getFieldName();
    		String dbFieldName = fieldMapper.getDbFieldName();
			Class<?> fieldType  = fieldMapper.getPropertyType();
			if(fieldType == Class.class){
				continue;
			}
			Object fieldValue  = bw.getPropertyValue(fieldName);

			
			if(levelMode == SqlConfig.PrimaryMode){
				
				if(fieldValue != null){
					if(SimpleTypeMapper.isPrimitiveWithString(fieldType)){
						
						selectSql.append(" and ").append(dbFieldName).append(" like  '%").append(fieldValue).append("%'");
						
					/*	selectSql.append(" and ").append(dbFieldName).append(" like  '%");
						selectSql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);
						selectSql.append("%'");*/
						
					}else{
						selectSql.append(" and ").append(dbFieldName).append(" = ");
						selectSql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);
					}
				}
				
				
			}else if(levelMode == SqlConfig.MiddleMode){
				
				
			}else if(levelMode == SqlConfig.HighMode){
				SqlPropertyDescriptor sqlPropertyDescriptor = sqlConfig.getSqlPd(fieldName);
				if( fieldValue != null ){
					if(sqlPropertyDescriptor == null){
						sqlPropertyDescriptor = new SqlPropertyDescriptor(fieldName);
					}
					//SqlUtils.getSpringJdbcWhere(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);
				}
				
				
				
			}
			else{
				if(fieldValue != null){
					selectSql.append(" and ").append(fieldMapper.getDbFieldName()).append(" = ");
					selectSql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);
				}
			}
	  }
	
		  String sql = selectSql.toString();
	      SqlModel<Object>  model = new SqlModel<Object> (sql, object,tableMapper);
		return model;
	}
	
	

    
    public static <E> TableMapper<E> buildTableMapper(Class<E> entityClass,String namespace, String id) {
    	TableMapper<E> tableMapper;
    	//get cache 
		 if(DbMappingBuilder.ME.isTableMappered(entityClass)){
			 return DbMappingBuilder.ME.getTableMapper(entityClass);
		 }
		 
    	synchronized(DbMappingBuilder.ME){
             //load 
             tableMapper =  MyMapperUtils.resultMap2TableMapper(entityClass,namespace, id);
             DbMappingBuilder.ME.addTableMapper(entityClass, tableMapper);
    		
    	}
		return tableMapper;
    }
    
    
}
