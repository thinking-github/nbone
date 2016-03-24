package org.nbone.framework.mybatis;

import org.apache.commons.beanutils.PropertyUtils;
import org.nbone.persistence.SqlBuilderBase;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.TableMapper;
import org.nbone.persistence.model.SqlModel;

/**
 * 通过自定义tableMapper and FieldMapper 生成SQl
 * @author thinking
 * @since 2015-12-12
 * @see TableMapper
 * @see FieldMapper
 *
 */
public class MybatisSqlBuilder  extends SqlBuilderBase{
	
	public final static MybatisSqlBuilder annotation_me = new MybatisSqlBuilder(annotation);
	
	public final static MybatisSqlBuilder oxm_me = new MybatisSqlBuilder(oxm);
	
    private String prefix = "" ;
    private String suffix = "Mapper";
    
	
	private int ormType = 1;
 	
    

	private MybatisSqlBuilder(int ormType) {
		this.ormType = ormType;
	}

	@Override
	public SqlModel buildInsertSql(Object object,TableMapper<?> tableMapper) throws Exception {
        if (null == object) {
            throw new RuntimeException("Sorry,I refuse to build sql for a null object!");
        }
        
        String tableName = tableMapper.getDbTableName();
        StringBuffer tableSql = new StringBuffer();
        StringBuffer valueSql = new StringBuffer();

        tableSql.append("insert into ").append(tableName).append("(");
        
        valueSql.append("values(");

        boolean allFieldNull = true;
        for (String dbFieldName : tableMapper.getFieldMapperCache().keySet()) {
            FieldMapper fieldMapper = tableMapper.getFieldMapperCache().get(dbFieldName);
            String fieldName = fieldMapper.getFieldName();
            Object value = PropertyUtils.getProperty(object, fieldName);
            if (value == null) {
                continue;
            }
            allFieldNull = false;
            tableSql.append(dbFieldName).append(",");
            
            valueSql.append("#{").append(fieldName);
            //valueSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
            valueSql.append("},");
        }
        if (allFieldNull) {
            throw new RuntimeException("Are you joking? Object " + object.getClass().getName()
                    + "'s all fields are null, how can i build sql for it?!");
        }
        int lastIndex1 = tableSql.lastIndexOf(",");
        int lastIndex2 = valueSql.lastIndexOf(",");
        
        
        tableSql.delete(lastIndex1, lastIndex1 + 1);
        valueSql.delete(lastIndex2, lastIndex2 + 1);
        
        
        String sql = tableSql.append(") ").append(valueSql).append(")").toString();
        
        SqlModel model = new SqlModel(sql, object);
        return model;
	}

	
	@Override
	public SqlModel buildUpdateSql(Object object,TableMapper<?> tableMapper) throws Exception {
        if (null == object) {
            throw new RuntimeException("Sorry,I refuse to build sql for a null object!");
        }
        String tableName = tableMapper.getDbTableName();
        if(tableName == null){
        	throw new RuntimeException(tableNameIsNullmsg);     
        }
        
        // String[] uniqueKeyNames = buildUniqueKey(tableMapper);
        String[] primaryKeys = tableMapper.getPrimaryKeys();
        
        if(primaryKeys.length == 0){
             throw new RuntimeException(primaryKeyIsNullmsg);        	
        }

        StringBuffer tableSql = new StringBuffer();
        StringBuffer whereSql = new StringBuffer(" where ");

        tableSql.append("update ").append(tableName).append(" set ");

        boolean allFieldNull = true;

        for (String dbFieldName : tableMapper.getFieldMapperCache().keySet()) {
            FieldMapper fieldMapper = tableMapper.getFieldMapperCache().get(dbFieldName);
            String fieldName = fieldMapper.getFieldName();
            Object value = PropertyUtils.getProperty(object, fieldName);
            if (value == null) {
                continue;
            }
            allFieldNull = false;
            tableSql.append(dbFieldName).append(" = #{").append(fieldName);
            //tableSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
            tableSql.append("},");
        }
        if (allFieldNull) {
            throw new RuntimeException("Are you joking? Object " + object.getClass().getName()
                    + "'s all fields are null, how can i build sql for it?!");
        }
        int lastIndexOf1 = tableSql.lastIndexOf(",");
        if(lastIndexOf1 != -1){
        	tableSql.delete(lastIndexOf1, lastIndexOf1 + 1);
        }
        
        for (int i = 0; i < primaryKeys.length; i++) {
            whereSql.append(primaryKeys[i]);
            FieldMapper fieldMapper = tableMapper.getFieldMapperCache().get(primaryKeys[i]);
            String fieldName = fieldMapper.getFieldName();
            Object value = PropertyUtils.getProperty(object, fieldName);
            if (value == null) {
                throw new RuntimeException("Unique key '" + primaryKeys[i]
                        + "' can't be null, build update sql failed!");
            }
            whereSql.append("=#{").append(fieldName);
            //whereSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
            whereSql.append("} and ");
        }
        int lastAndIndex = whereSql.lastIndexOf("and");
        if(lastAndIndex != -1){
        	whereSql.delete(lastAndIndex, lastAndIndex + 3);
        }
        
        String sql = tableSql.append(whereSql).toString();
        
        SqlModel model = new SqlModel(sql, object);
        return model;
	}
	
	@Override
	public SqlModel buildUpdateSql(Object object,TableMapper<?> tableMapper, boolean safeAttr) throws Exception {
		return null;
	}
	
	
	@Override
	public SqlModel buildDeleteSql(Object object,TableMapper<?> tableMapper) throws Exception {
        if (null == object) {
            throw new RuntimeException("Sorry,I refuse to build sql for a null object!");
        }
        String tableName = tableMapper.getDbTableName();
        
        String[] primaryKeys = tableMapper.getPrimaryKeys();

        StringBuffer sql = new StringBuffer();

        // delete from tableName where primaryKeyName=?
        sql.append("delete from ").append(tableName).append(" where ");
        for (int i = 0; i < primaryKeys.length; i++) {
            sql.append(primaryKeys[i]);
            FieldMapper fieldMapper = tableMapper.getFieldMapperCache().get(primaryKeys[i]);
            
            String fieldName = fieldMapper.getFieldName();
            Object value = PropertyUtils.getProperty(object, fieldName);
            if (value == null) {
                throw new RuntimeException("Unique key '" + primaryKeys[i]
                        + "' can't be null, build update sql failed!");
            }
            sql.append(" = #{").append(fieldName);
            //sql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
            sql.append("} and ");
        }
        
        int lastAndIndex  = sql.lastIndexOf("and");
        sql.delete(lastAndIndex, lastAndIndex + 3);
        
        SqlModel model = new SqlModel(sql.toString(), object);
        
        return model;
	}

	
	@Override
	public SqlModel buildSelectSql(Object object,TableMapper<?> tableMapper) throws Exception {
        if (null == object) {
            throw new RuntimeException("Sorry,I refuse to build sql for a null object!");
        }
        //TableMapper<?> tableMapper = buildTableMapper(object.getClass());
        String tableName = tableMapper.getDbTableName();
        String[] primaryKeys = tableMapper.getPrimaryKeys();

        StringBuffer selectSql = new StringBuffer();
        selectSql.append("select ");
        for (String dbFieldName : tableMapper.getFieldMapperCache().keySet()) {
            selectSql.append(dbFieldName).append(",");
        }
        int lastIndex1 = selectSql.lastIndexOf(",");
        selectSql.delete(lastIndex1, lastIndex1 + 1);
        
        
        selectSql.append(" from ").append(tableName);

        StringBuffer whereSql = new StringBuffer(" where ");
        for (int i = 0; i < primaryKeys.length; i++) {
            whereSql.append(primaryKeys[i]);
            FieldMapper fieldMapper = tableMapper.getFieldMapperCache().get(primaryKeys[i]);
            String fieldName = fieldMapper.getFieldName();
            Object value = PropertyUtils.getProperty(object, fieldName);
            if (value == null) {
                throw new RuntimeException("Unique key '" + primaryKeys[i]
                        + "' can't be null, build query sql failed!");
            }
            whereSql.append(" = #{").append(fieldName);
            //whereSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
            whereSql.append("} and ");
        }
        int lastAndIndex  = whereSql.lastIndexOf("and");
        whereSql.delete(lastAndIndex,lastAndIndex + 3);
        
        SqlModel model = new SqlModel(selectSql.append(whereSql).toString(), object);
        
        return model;
		
	}
	
	
	

}
