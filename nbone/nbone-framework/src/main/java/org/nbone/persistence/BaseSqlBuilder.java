package org.nbone.persistence;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

import org.nbone.framework.mybatis.util.MyMapperUtils;
import org.nbone.lang.MathOperation;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.annotation.MappedBy;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.enums.QueryType;
import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.mapper.DbMappingBuilder;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.TableMapper;
import org.nbone.persistence.model.SqlModel;
import org.nbone.persistence.util.SqlUtils;
import org.nbone.util.PropertyUtil;
import org.nbone.util.reflect.SimpleTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import com.google.common.base.Function;

/**
 * 根据JPA注解构建sql
 *
 * @author thinking
 * @version 1.0
 * @see javax.persistence.Entity
 * @see javax.persistence.Table
 * @see javax.persistence.Id
 * @see javax.persistence.Column
 * @since 2015-12-12
 */
@SuppressWarnings("unchecked")
public abstract class BaseSqlBuilder implements SqlBuilder {

    private static final Logger logger = LoggerFactory.getLogger(BaseSqlBuilder.class);
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
                placeholderSuffix = "";

                break;
            case MYBATIS:
                placeholderPrefix = "#{";
                placeholderSuffix = "}";
                break;
            case HIBERNATE:
                placeholderPrefix = ":";
                placeholderSuffix = "";
                break;

            default:
                placeholderPrefix = "";
                placeholderSuffix = "";
                break;
        }
    }


    @Override
    public SqlModel<Object> insertSql(Object object) throws BuilderSQLException {
        Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");

        return null;
    }

    @Override
    public SqlModel<Object> insertSelectiveSql(Object object) throws BuilderSQLException {
        Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");

        TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(object.getClass());
        String tableName = tableMapper.getDbTableName();
        StringBuffer tableSql = new StringBuffer();
        StringBuffer valueSql = new StringBuffer();

        tableSql.append("insert into ").append(tableName).append("(");

        valueSql.append("values(");

        boolean allFieldNull = true;
        int columnCount = 0;
        Collection<FieldMapper> fields = tableMapper.getFieldMapperList();
        for (FieldMapper fieldMapper : fields) {
            String fieldName = fieldMapper.getFieldName();
            String dbFieldName = fieldMapper.getDbFieldName();
            Object value = PropertyUtil.getProperty(object, fieldName);
            if (value == null) {
                continue;
            }
            allFieldNull = false;
            columnCount++;
            if (columnCount > 1) {
                tableSql.append(", ");
                valueSql.append(", ");
            }
            tableSql.append(dbFieldName);

            valueSql.append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
            //valueSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
        }
        //XXX:allFieldNull
        if (allFieldNull) {
            throw new RuntimeException(object.getClass().getName() + "'s all fields are null, how can i build sql for it?!");
        }

        String sql = tableSql.append(") ").append(valueSql).append(")").toString();

        SqlModel<Object> model = new SqlModel<Object>(sql, object, tableMapper);
        return model;
    }

    //update
    @Override
    public SqlModel<Object> updateSql(Object object,String[] properties,String[] conditionFields) throws BuilderSQLException {
        return updateSql(object,properties, false,conditionFields, null);

    }

    @Override
    public SqlModel<Object> updateSelectiveSql(final Object object,String[] conditionFields) throws BuilderSQLException {
        return updateSql(object,null, true, conditionFields,null);
    }

    @Override
    public SqlModel<Object> updateSql(final Object object,String[] properties, final boolean isSelective,String[] conditionFields,String whereSql)
            throws BuilderSQLException {
        SqlModel<Object> sqlModel = update(object, new Function<TableMapper<?>, StringBuilder>() {
            @Override
            public StringBuilder apply(TableMapper<?> tableMapper) {
                StringBuilder fieldSql = new StringBuilder();
                boolean allFieldNull = true;
                int columnCount = 0;
                if(properties == null || properties.length == 0){
                    Collection<FieldMapper> fields = tableMapper.getFieldMapperList();
                    for (FieldMapper fieldMapper : fields) {
                        String fieldName = fieldMapper.getFieldName();
                        String dbFieldName = fieldMapper.getDbFieldName();
                        Object value = PropertyUtil.getProperty(object, fieldName);
                        //XXX: beark primaryKey update
                        if (fieldMapper.isPrimaryKey()) {
                            continue;
                        }
                        // isSelective = true
                        if (isSelective && value == null) {
                            continue;
                        }

                        allFieldNull = false;
                        columnCount++;
                        if (columnCount > 1) {
                            fieldSql.append(",");
                        }
                        fieldSql.append(dbFieldName).append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
                        //tableSql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());

                    }
                }else {
                    allFieldNull = false;
                    for (String property : properties) {
                        FieldMapper fieldMapper =   tableMapper.getFieldMapperByPropertyName(property);
                        String fieldName = fieldMapper.getFieldName();
                        String dbFieldName = fieldMapper.getDbFieldName();
                        columnCount++;
                        if (columnCount > 1) {
                            fieldSql.append(",");
                        }
                        fieldSql.append(dbFieldName).append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
                    }

                }
                //XXX: allFieldNull
                if (allFieldNull) {
                    throw new RuntimeException(object.getClass().getName() + "'s all fields are null, how can i build sql for it?!");
                }
                return fieldSql;
            }
        }, conditionFields,whereSql);
        return sqlModel;
    }

    @Override
    public <T> SqlModel<Map<String, ?>> updateSql(Class<T> entityClass, Map<String, ?> fieldsMap, boolean isDbFieldName)
            throws BuilderSQLException {
        return null;
    }

    @Override
    public SqlModel<Object> updateMathOperationSql(final Object object,String property, final MathOperation mathOperation)
            throws BuilderSQLException {
        SqlModel<Object> sqlModel = update(object, new Function<TableMapper<?>, StringBuilder>() {
            @Override
            public StringBuilder apply(TableMapper<?> tableMapper) {
                StringBuilder fieldSql = new StringBuilder();

                boolean allFieldNull = true;
                int columnCount = 0;
                Collection<FieldMapper> fields = tableMapper.getFieldMapperList();
                for (FieldMapper fieldMapper : fields) {
                    String fieldName = fieldMapper.getFieldName();
                    String dbFieldName = fieldMapper.getDbFieldName();
                    Object value = PropertyUtil.getProperty(object, fieldName);
                    //XXX: beark primaryKey update
                    if (fieldMapper.isPrimaryKey()) {
                        continue;
                    }
                    if (value == null) {
                        continue;
                    }

                    columnCount++;
                    if (columnCount > 1) {
                        fieldSql.append(",");
                    }
                    // 手工指定计算字段
                    if (property != null && property.equals(fieldName)) {
                        allFieldNull = false;
                        fieldSql.append(dbFieldName).append(" = ").append(dbFieldName).append(" ").append(mathOperation.getMark()).append(" ").append(value);
                    }
                    //没有指定计算字段 根据规则计算 only number type calc
                    else if (property == null && value instanceof Number) {
                        allFieldNull = false;
                        fieldSql.append(dbFieldName).append(" = ").append(dbFieldName).append(" ").append(mathOperation.getMark()).append(" ").append(value);
                    } else {
                        //other type  update value
                        fieldSql.append(dbFieldName).append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);

                    }
                }
                //XXX: allFieldNull
                if (allFieldNull) {
                    throw new RuntimeException(object.getClass().getName() + "'s  calc fields are null, how can i build sql for it?!");
                }
                return fieldSql;
            }
        }, null,null);

        return sqlModel;

    }

    /**
     * update tableName set name='chen' where id = 1
     * update 更新公共回调方法
     *
     * @param object 实体对象
     * @param function  回调函数
     * @param conditionFields  属性条件 可为空， 为空时默认使用主键作为条件
     * @param  whereSqlPart 可为空， 为空时默认使用主键作为条件
     * @return
     */
    protected SqlModel<Object> update(Object object, Function<TableMapper<?>, StringBuilder> function,String[] conditionFields,String whereSqlPart) {

        TableMapper<?> tableMapper = checkBuildUpdate(object);

        String tableName = tableMapper.getDbTableName();
        StringBuffer tableSql = new StringBuffer();
        tableSql.append("update ").append(tableName).append(" set ");

        //XXX: fields  callback
        StringBuilder setSql = function.apply(tableMapper);
        tableSql.append(setSql);

        //where id = 1
        StringBuffer whereSql = new StringBuffer(" where ");

        if(conditionFields != null && conditionFields.length > 0){
            whereSql.append(propertysCondition(object.getClass(),tableMapper,conditionFields));
        }else  if(whereSqlPart != null && whereSqlPart.trim().length() > 0){
            whereSql.append(whereSqlPart);
        }else {
            whereSql.append(primaryKeysCondition(object, tableMapper));
        }

        String sql = tableSql.append(whereSql).toString();

        SqlModel<Object> model = new SqlModel<Object>(sql, object, tableMapper);
        return model;
    }

    private TableMapper<?> checkBuildUpdate(Object object) {
        Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");

        TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(object.getClass());

        String primaryKey = tableMapper.getPrimaryKey();

        if (primaryKey == null) {
            throw new RuntimeException(PrimaryKeyIsNullMSG);
        }
        return tableMapper;
    }

    @Override
    public SqlModel<Object> deleteSqlByEntityParams(Object object, boolean onlypkParam) throws BuilderSQLException {

        Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");

        TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(object.getClass());

        StringBuffer sql = new StringBuffer();

        // delete from tableName where primaryKeyName = ?
        sql.append(tableMapper.getDeleteAllSql()).append(" where ");
        if (onlypkParam) {
            sql.append(primaryKeysCondition(object, tableMapper));

        } else {
            // delete from tableName where 1=1 and name = ? and age = 13
            boolean allFieldNull = true;
            sql.append(" 1=1 ");
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(object);
            Collection<FieldMapper> fields = tableMapper.getFieldMapperList();
            for (FieldMapper fieldMapper : fields) {
                String fieldName = fieldMapper.getFieldName();
                String dbFieldName = fieldMapper.getDbFieldName();
                Class<?> fieldType = fieldMapper.getPropertyType();
                if (fieldType == Class.class) {
                    continue;
                }
                Object fieldValue = bw.getPropertyValue(fieldName);

                if (fieldValue != null) {
                    allFieldNull = false;
                    sql.append(" and ").append(dbFieldName).append(" = ");
                    sql.append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
                }
            }
            //XXX: allFieldNull
            if (allFieldNull) {
                throw new RuntimeException(object.getClass().getName() + "'s all fields are null, how can i build sql for it?!");
            }

        }

        SqlModel<Object> model = new SqlModel<Object>(sql.toString(), object, tableMapper);

        return model;


    }

    @Override
    public <T> SqlModel<Map<String, ?>> deleteSqlById(Class<T> entityClass, Serializable id) throws BuilderSQLException {
        Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");
        if (id == null) {
            return SqlModel.EmptySqlModel;
        }
        TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(entityClass);
        String[] primaryKeys = tableMapper.getPrimaryKeys();
        FieldMapper fieldMapper = tableMapper.getFieldMapper(primaryKeys[0]);

        StringBuffer sql = new StringBuffer();
        // delete from tableName where primaryKeyName = ?
        sql.append(tableMapper.getDeleteAllSql()).append(" where ");

        sql.append(fieldMapper.getDbFieldName()).append(" = ");
        sql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);

        Map<String, Serializable> paramsMap = new HashMap<String, Serializable>(1);
        paramsMap.put(fieldMapper.getFieldName(), id);

        SqlModel<Map<String, ?>> model = new SqlModel<Map<String, ?>>(sql.toString(), paramsMap, tableMapper);


        return model;
    }

    @Override
    public <T> SqlModel<T> deleteSqlByIds(Class<T> entityClass, Object[] ids) throws BuilderSQLException {
        Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");
        if (ids == null) {
            return SqlModel.EmptySqlModel;
        }
        TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(entityClass);
        String[] primaryKeys = tableMapper.getPrimaryKeys();
        FieldMapper fieldMapper = tableMapper.getFieldMapper(primaryKeys[0]);

        StringBuffer sql = new StringBuffer();
        // delete from tableName where primaryKeyName = ?
        sql.append(tableMapper.getDeleteAllSql()).append(" where ");

        StringBuilder in = SqlUtils.list2In(fieldMapper.getDbFieldName(), ids);
        sql.append(in);

        SqlModel<T> model = new SqlModel<T>(sql.toString(), null, tableMapper);
        model.setParameterArray(ids);
        return model;

    }

    @Override
    public <T> SqlModel<Map<String, ?>> selectSqlById(Class<T> entityClass, Serializable id) throws BuilderSQLException {
        Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");
        if (id == null) {
            return SqlModel.EmptySqlModel;
        }
        TableMapper<T> tableMapper = DbMappingBuilder.ME.getTableMapper(entityClass);
        String all = tableMapper.getSelectAllSql(false);
        String[] primaryKeys = tableMapper.getPrimaryKeys();


        FieldMapper fieldMapper = tableMapper.getFieldMapper(primaryKeys[0]);
        StringBuffer selectSql = new StringBuffer(all);
        selectSql.append(" where ");
        selectSql.append(fieldMapper.getDbFieldName()).append(" = ");
        selectSql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);

        String sql = selectSql.toString();

        Map<String, Serializable> paramsMap = new HashMap<String, Serializable>(1);
        paramsMap.put(fieldMapper.getFieldName(), id);

        SqlModel<Map<String, ?>> model = new SqlModel<Map<String, ?>>(sql, paramsMap, tableMapper);


        return model;
    }

    @Override
    public SqlModel<Object> selectSqlById(Object object) throws BuilderSQLException {
        Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");
        TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(object.getClass());

        boolean isDistinct = false;
        if (object instanceof Distinct) {
            Distinct distinct = (Distinct) object;
            isDistinct = distinct.isDistinct();
        }

        StringBuffer selectSql = new StringBuffer(tableMapper.getSelectAllSql(isDistinct));

        StringBuffer whereSql = new StringBuffer(" where ");
        whereSql.append(primaryKeysCondition(object, tableMapper));

        String sql = selectSql.append(whereSql).toString();

        SqlModel<Object> model = new SqlModel<Object>(sql, object, tableMapper);

        return model;
    }

    @Override
    public <T> SqlModel<T> selectAllSql(Class<T> entityClass) throws BuilderSQLException {
        Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");
        TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(entityClass);
        String sql = tableMapper.getSelectAllSql(false);
        SqlModel<T> sqlModel = new SqlModel<T>(sql, null, tableMapper);
        return sqlModel;
    }

    @Override
    public <T> SqlModel<T> countSql(Class<T> entityClass,String afterWhere) throws BuilderSQLException {
        TableMapper<T> tableMapper = DbMappingBuilder.ME.getTableMapper(entityClass);
        SqlModel<T> model = new SqlModel<T>(tableMapper.getCountSql().toString(), null, tableMapper);
        if(afterWhere != null){
            model.setAfterWhere(new String[]{ afterWhere });
        }
        return model;
    }


    @Override
    public SqlModel<Object> countSql(Object object, String... afterWhere) throws BuilderSQLException {
        Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");

        TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(object.getClass());
        String countSql = tableMapper.getCountSql();

        StringBuilder countSqlAndWhere = new StringBuilder(countSql);

        StringBuilder whereSql = getWhereSql(object, tableMapper, new SqlConfig(-1));
        countSqlAndWhere.append(whereSql);

        String sql = countSqlAndWhere.toString();
        SqlModel<Object> model = new SqlModel<Object>(sql, object, tableMapper);
        model.setAfterWhere(afterWhere);
        return model;
    }


    @Override
    public <T> SqlModel<T> selectSqlByIds(Class<T> entityClass, Collection<?> ids) throws BuilderSQLException {
        return selectSqlByIds(entityClass, ids.toArray());
    }

    @Override
    public <T> SqlModel<T> selectSqlByIds(Class<T> entityClass, Object[] ids) throws BuilderSQLException {
        Assert.notNull(entityClass, "Sorry,I refuse to build sql for a null entityClass!");
        TableMapper<T> tableMapper = DbMappingBuilder.ME.getTableMapper(entityClass);
        String allsql = tableMapper.getSelectAllSql(false);

        String[] primaryKeys = tableMapper.getPrimaryKeys();
        StringBuilder in = SqlUtils.list2In(primaryKeys[0], ids);

        StringBuilder sql = new StringBuilder(allsql).append(" where ").append(in);

        SqlModel<T> sqlModel = new SqlModel<T>(sql.toString(), null, tableMapper);
        sqlModel.setParameterArray(ids);
        return sqlModel;
    }


    @Override
    public SqlModel<Map<String, ?>> objectModeSelectSql(Object object, SqlConfig sqlConfig) throws BuilderSQLException {

        Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");

        TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(object.getClass());


        String allsql = getSelectSqlBeforeWhere(object, tableMapper, sqlConfig);

        StringBuffer whereSql = new StringBuffer(allsql).append(" where 1 = 1 ");
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(object);

        Collection<FieldMapper> fields = tableMapper.getFieldMapperList();
        Map<String, Object> namedParameters = new HashMap<String, Object>();

        for (FieldMapper fieldMapper : fields) {
            String fieldName = fieldMapper.getFieldName();
            String dbFieldName = fieldMapper.getDbFieldName();
            Class<?> fieldType = fieldMapper.getPropertyType();
            if (fieldType == Class.class) {
                continue;
            }
            Object fieldValue = bw.getPropertyValue(fieldName);
            SqlPropertyDescriptor sqlPropertyDescriptor = sqlConfig.getSqlPd(fieldName);
            //XXX：特殊情况可为空
            if (fieldValue == null && sqlPropertyDescriptor != null) {
                sqlPropertyDescriptor.setPropertyType(fieldType);
                String operType = sqlPropertyDescriptor.getOperType();
                if (SqlPropertyDescriptor.is_null.equalsIgnoreCase(operType) || SqlPropertyDescriptor.is_not_null.equalsIgnoreCase(operType)) {

                    StringBuilder sqlpd = SqlUtils.getCommonWherePart(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);
                    whereSql.append(sqlpd);
                    continue;

                } else if (sqlPropertyDescriptor.isIn()) {

                    StringBuilder sqlpd = SqlUtils.getSpringJdbcWhereIn(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);
                    whereSql.append(sqlpd);
                    continue;

                } else if (sqlPropertyDescriptor.isBetween()) {
                    StringBuilder sqlpd = SqlUtils.getWhereBetween(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);
                    whereSql.append(sqlpd);
                    continue;
                }
            }
            //XXX: 2016-08-10  string not is ""
            if (fieldValue != null && !fieldValue.equals("")) {
                if (sqlPropertyDescriptor == null) {
                    sqlPropertyDescriptor = new SqlPropertyDescriptor(fieldName);
                    sqlPropertyDescriptor.setPropertyType(fieldType);

                }
                StringBuilder sqlWhere = SqlUtils.getSpringJdbcWhere(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);
                //StringBuilder sqlWhere = SqlUtils.getCommonWherePart(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);

                whereSql.append(sqlWhere);
            }

        }

        //:XXX Range beginTime <=  value  <= endTime
        List<SqlPropertyRange> sqlPropertyRanges = sqlConfig.getSqlPropertyRanges();
        if (sqlPropertyRanges != null && sqlPropertyRanges.size() > 0) {
            for (SqlPropertyRange sqlPropertyRange : sqlPropertyRanges) {
                String dbLeftField = tableMapper.getDbFieldName(sqlPropertyRange.getLeftField());
                String dbRightField = tableMapper.getDbFieldName(sqlPropertyRange.getRightField());
                sqlPropertyRange.setDbLeftField(dbLeftField);
                sqlPropertyRange.setDbRightField(dbRightField);
                StringBuilder rangeSql = SqlUtils.getPropertyRange(sqlPropertyRange, namedParameters);
                if (rangeSql != null) {
                    whereSql.append(rangeSql);
                }
            }

        }

        //order by
        StringBuilder orderSql = SqlUtils.getOrderBy(sqlConfig, false);
        if (orderSql != null) {
            whereSql.append(orderSql);
        }

        String sql = whereSql.toString();
        SqlModel<Map<String, ?>> model = new SqlModel<Map<String, ?>>(sql, namedParameters, tableMapper);
        return model;
    }


    @Override
    public SqlModel<Object> selectSql(Object object, SqlConfig sqlConfig) {
        Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");

        TableMapper<?> tableMapper = DbMappingBuilder.ME.getTableMapper(object.getClass());

        String selectsql = getSelectSqlBeforeWhere(object, tableMapper, sqlConfig);

        StringBuilder selectSqlAndWhere = new StringBuilder(selectsql);

        StringBuilder whereSql = getWhereSql(object, tableMapper, sqlConfig);
        selectSqlAndWhere.append(whereSql);

        String sql = selectSqlAndWhere.toString();
        SqlModel<Object> model = new SqlModel<Object>(sql, object, tableMapper);
        model.setAfterWhere(sqlConfig.getAfterWhere());
        return model;
    }
    @Override
    public <T> RowMapper<T> getRowMapper(GroupQuery groupQuery) {
        RowMapper<T> rowMapper = null;
        if(groupQuery != null && groupQuery.getMapClass() != null){
            if(Map.class.isAssignableFrom(groupQuery.getMapClass())){
                rowMapper = (RowMapper<T>) new ColumnMapRowMapper();
            }else {
                TableMapper<T> tableMapper = (TableMapper<T>) DbMappingBuilder.ME.getTableMapper(groupQuery.getMapClass());
                if(tableMapper != null){
                    rowMapper = tableMapper.getRowMapper();
                }
            }
        }
        return  rowMapper;
    }

    /**
     * 获取查询sql  where 之前的语句 select * from  test
     * <ul>
     * <li>构建字段列表</li>
     * <li>是否去重</li>
     * </ul>
     *
     * @param object
     * @param tableMapper
     * @param sqlConfig
     * @return
     */
    private String getSelectSqlBeforeWhere(Object object, TableMapper<?> tableMapper, SqlConfig sqlConfig) {

        String[] fieldNames = sqlConfig.getFieldNames();
        String allsql = null;

        boolean isDistinct = false;
        if (object instanceof Distinct) {
            Distinct distinct = (Distinct) object;
            isDistinct = distinct.isDistinct();
        }

        GroupQuery groupQuery = sqlConfig.getGroupQuery();
        if(groupQuery == null){
            // 1.优先使用自定义查询，
            if (fieldNames == null || fieldNames.length == 0) {
                //2.字段级别次之
                FieldLevel fieldLevel = sqlConfig.getFieldLevel();
                if (fieldLevel == null) {
                    allsql = tableMapper.getSelectAllSql(isDistinct);
                } else {
                    allsql = tableMapper.getSelectAllSql(fieldLevel, isDistinct);
                }

            } else {
                allsql = tableMapper.getSelectAllSql(fieldNames, sqlConfig.isDbFieldMode(), isDistinct);
            }
        }else {
            //分组查询 构建分组查询字段
            allsql = tableMapper.getGroupSelectAllSql(groupQuery.getQueryColumnBySql());
        }
        return allsql;
    }

    private StringBuilder getWhereSql(Object object, TableMapper<?> tableMapper, SqlConfig sqlConfig) {
        StringBuilder whereSql = new StringBuilder(" where 1 = 1 ");
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(object);

        Collection<FieldMapper> fields = tableMapper.getFieldMapperList();
        int levelMode = sqlConfig.getSqlMode();
        for (FieldMapper fieldMapper : fields) {
            String fieldName = fieldMapper.getFieldName();
            String dbFieldName = fieldMapper.getDbFieldName();
            Class<?> fieldType = fieldMapper.getPropertyType();
            if (fieldType == Class.class) {
                continue;
            }
            Object fieldValue = bw.getPropertyValue(fieldName);
            //XXX: value is null or value is ""  break
            if (fieldValue == null || fieldValue.equals("")) {
                continue;
            }

            if (levelMode == SqlConfig.PrimaryMode) {

                if (SimpleTypeMapper.isPrimitiveWithString(fieldType)) {

                    whereSql.append(" and ").append(dbFieldName).append(" like  '%").append(fieldValue).append("%'");

					/*	whereSql.append(" and ").append(dbFieldName).append(" like  '%");
                        whereSql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);
						whereSql.append("%'");*/

                } else {
                    whereSql.append(" and ").append(dbFieldName).append(" = ");
                    whereSql.append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
                }


            } else if (levelMode == SqlConfig.MiddleMode) {


            } else if (levelMode == SqlConfig.HighMode) {
                SqlPropertyDescriptor sqlPropertyDescriptor = sqlConfig.getSqlPd(fieldName);
                if (sqlPropertyDescriptor == null) {
                    sqlPropertyDescriptor = new SqlPropertyDescriptor(fieldName);
                }
                //SqlUtils.getSpringJdbcWhere(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);


            } else {
                whereSql.append(" and ").append(dbFieldName).append(" = ");
                whereSql.append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
            }
        }
       //扩展属性条件查询 in / between
        extFieldsCondition(object,tableMapper,whereSql);

        GroupQuery groupQuery = sqlConfig.getGroupQuery();
        if(groupQuery != null){
            whereSql.append(groupQuery.getGroupBySql());
            whereSql.append(groupQuery.getOrderBySql());
        }
        return whereSql;
    }

    /**
     * extPropertysCondition
     */
    private void extFieldsCondition(Object object, TableMapper<?> tableMapper,StringBuilder whereSql){
        List<Field> extFields = tableMapper.getExtFields();
        if(extFields != null && extFields.size() > 0){
            for (Field field : extFields) {
                MappedBy mappedBy = field.getAnnotation(MappedBy.class);
                String fieldName    = mappedBy.name();
                QueryType queryType = mappedBy.queryType();
                FieldMapper fieldMapper =  tableMapper.getFieldMapperByPropertyName(fieldName);
                String dbFieldName  = fieldMapper.getDbFieldName();
                Class<?>  fieldType = field.getType();
                StringBuilder  extSql = null;
                try {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    Object value  = field.get(object);
                    field.setAccessible(accessible);
                    switch (queryType) {
                        case IN:
                            if(fieldType.isArray()){
                                extSql =  SqlUtils.andIn(dbFieldName,fieldMapper.getPropertyType(),value,true);
                            }
                            else if(Collection.class.isAssignableFrom(fieldType)){
                                Object[] array = ((Collection)value).toArray();
                                extSql = SqlUtils.andIn(dbFieldName,fieldMapper.getPropertyType(),array,true);
                            }
                            break;
                        case NOT_IN:
                            if(fieldType.isArray()){
                                extSql =  SqlUtils.andIn(dbFieldName,fieldMapper.getPropertyType(), value,false);
                            }
                            else if(Collection.class.isAssignableFrom(fieldType)){
                                Object[] array = ((Collection)value).toArray();
                                extSql = SqlUtils.andIn(dbFieldName,fieldMapper.getPropertyType(),array,false);
                            }
                            break;
                        case BETWEEN:
                            if(fieldType.isArray()){
                                extSql =  SqlUtils.andBetween(dbFieldName,(Object[]) value,true);
                            }
                            else if(Collection.class.isAssignableFrom(fieldType)){
                                Object[] array = ((Collection)value).toArray();
                                extSql = SqlUtils.andBetween(dbFieldName,array,true);
                            }
                            break;
                        case NOT_BETWEEN:
                            if(fieldType.isArray()){
                                extSql =  SqlUtils.andBetween(dbFieldName,(Object[]) value,false);
                            }
                            else if(Collection.class.isAssignableFrom(fieldType)){
                                Object[] array = ((Collection)value).toArray();
                                extSql = SqlUtils.andBetween(dbFieldName,array,false);
                            }

                            break;
                        default:
                            break;
                    }
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage());
                }

                if(extSql != null){
                    whereSql.append(extSql);
                }
            }
        }

    }


    //-----------------------------------------------------------------------------
    //
    //-----------------------------------------------------------------------------

    /**
     * @see #primaryKeysCondition(Class, TableMapper)
     */
    private StringBuffer primaryKeysCondition(Object object, TableMapper<?> tableMapper) throws BuilderSQLException {
        Assert.notNull(object, "Sorry,I refuse to build sql for a null object!");

        // object PrimaryKey value validata
        String[] primaryKeys = tableMapper.getPrimaryKeys();
        for (int i = 0; i < primaryKeys.length; i++) {
            FieldMapper fieldMapper = tableMapper.getFieldMapper(primaryKeys[i]);
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
     *
     * @param entityClass
     * @param tableMapper
     * @return
     */
    private StringBuffer primaryKeysCondition(Class<?> entityClass, TableMapper<?> tableMapper) throws BuilderSQLException {

        String[] primaryKeys = tableMapper.getPrimaryKeys();
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < primaryKeys.length; i++) {
            sql.append(primaryKeys[i]);
            FieldMapper fieldMapper = tableMapper.getFieldMapper(primaryKeys[i]);
            String fieldName = fieldMapper.getFieldName();

            if (i > 0) {
                sql.append(" and ");
            }
            sql.append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
            //sql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
        }

        return sql;
    }

    /**
     * where 属性条件
     * <p> eg: id = 1 and name = chen
     *
     * @param entityClass  按实体类
     * @param tableMapper  实体映射对象
     * @param  conditionFields 属性条件 可为空， 当为空时自动使用主键条件
     * @return
     */
    private StringBuffer propertysCondition(Class<?> entityClass, TableMapper<?> tableMapper,String[] conditionFields) throws BuilderSQLException {

        if(conditionFields == null || conditionFields.length == 0){
            return  primaryKeysCondition(entityClass,tableMapper);
        }else {
            StringBuffer sql = new StringBuffer();
            for (int i = 0; i < conditionFields.length; i++) {
                String propertyName = conditionFields[i];
                FieldMapper fieldMapper = tableMapper.getFieldMapperByPropertyName(propertyName);
                sql.append(fieldMapper.getDbFieldName());
                String fieldName = fieldMapper.getFieldName();

                if (i > 0) {
                    sql.append(" and ");
                }
                sql.append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
                //sql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
            }
            return sql;
            }
    }


    public static <E> TableMapper<E> buildTableMapper(Class<E> entityClass, String namespace, String id) {
        TableMapper<E> tableMapper;
        //get cache
        if (DbMappingBuilder.ME.isTableMappered(entityClass)) {
            return DbMappingBuilder.ME.getTableMapper(entityClass);
        }

        synchronized (DbMappingBuilder.ME) {
            //load
            tableMapper = MyMapperUtils.resultMap2TableMapper(entityClass, namespace, id);
            DbMappingBuilder.ME.addTableMapper(entityClass, tableMapper);

        }
        return tableMapper;
    }

    public static <E> TableMapper<E> buildTableMapper(String namespace, String id) {
        TableMapper<E> tableMapper;
        synchronized (DbMappingBuilder.ME) {
            //load
            tableMapper = MyMapperUtils.resultMap2TableMapper(namespace, id);
            DbMappingBuilder.ME.addTableMapper(tableMapper.getEntityClazz(), tableMapper);

        }
        return tableMapper;
    }


}
