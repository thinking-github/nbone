package org.nbone.persistence;

import com.google.common.base.Function;
import org.nbone.constants.DateConstant;
import org.nbone.framework.mybatis.util.MyMapperUtils;
import org.nbone.lang.MathOperation;
import org.nbone.mvc.domain.GroupQuery;
import org.nbone.persistence.annotation.FieldLevel;
import org.nbone.persistence.annotation.QueryOperation;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.enums.QueryType;
import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.mapper.EntityMapper;
import org.nbone.persistence.mapper.FieldMapper;
import org.nbone.persistence.mapper.MappingBuilder;
import org.nbone.persistence.model.SqlModel;
import org.nbone.persistence.util.SqlUtils;
import org.nbone.util.DateFPUtils;
import org.nbone.util.PropertyUtil;
import org.nbone.util.reflect.SimpleTypeMapper;
import org.nbone.web.util.RequestQueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.*;

import javax.servlet.ServletRequest;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

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

    private static final List<String> IGNORE_PROPERTY = Arrays.asList("class", "serialVersionUID");

    public final static int oxm = 1;
    public final static int annotation = 2;


    private String placeholderPrefix = "";
    private String placeholderSuffix = "";

    public final static String TABLE_NAME_NULL_MSG = "table name  must not be null. --thinking";
    public final static String PRIMARY_KEY_NULL_MSG = "primary Keys must not be null. --thinking";


    private JdbcFrameWork jdbcFrameWork;

    public BaseSqlBuilder(JdbcFrameWork jdbcFrameWork) {
        this.jdbcFrameWork = jdbcFrameWork;

        switch (this.jdbcFrameWork) {
            case SPRING_JDBC:
            case HIBERNATE:
                placeholderPrefix = ":";

                break;
            case MYBATIS:
                placeholderPrefix = "#{";
                placeholderSuffix = "}";
                break;
            default:
                placeholderPrefix = "";
                placeholderSuffix = "";
                break;
        }
    }


    @Override
    public SqlModel<Object> insertSql(Object object) throws BuilderSQLException {
        Assert.notNull(object, "build sql error, object must not be null.");

        return insertSelectiveSql(object);
    }

    @Override
    public SqlModel<Object> insertSelectiveSql(Object object) throws BuilderSQLException {
        Assert.notNull(object, "build sql error, object must not be null.");

        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(object.getClass());
        String tableName = entityMapper.getTableName(object);
        StringBuilder tableSql = new StringBuilder();
        StringBuilder valueSql = new StringBuilder();

        tableSql.append("insert into ").append(tableName).append("(");

        valueSql.append("values(");

        boolean allFieldNull = true;
        int columnCount = 0;
        Collection<FieldMapper> fields = entityMapper.getFieldMapperList();
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

        SqlModel<Object> model = new SqlModel<Object>(sql, object, entityMapper);
        return model;
    }

    @Override
    public List<FieldMapper> getPrimaryKeys(Class<?> entityClass) {
        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
        return entityMapper.getPrimaryKeyFields();
    }

    @Override
    public FieldMapper getPrimaryKey(Class<?> entityClass) {
        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
        return entityMapper.getPrimaryKeyFieldMapper();
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
        SqlModel<Object> sqlModel = update(object, new Function<EntityMapper<?>, StringBuilder>() {
            @Override
            public StringBuilder apply(EntityMapper<?> tableMapper) {
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
                        FieldMapper fieldMapper =   tableMapper.getFieldMapperByProperty(property);
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
    public SqlModel<Object> updateMathOperationSql(final Object object,String property, final MathOperation mathOperation, String[] conditionFields,String whereSql)
            throws BuilderSQLException {
        SqlModel<Object> sqlModel = update(object, new Function<EntityMapper<?>, StringBuilder>() {
            @Override
            public StringBuilder apply(EntityMapper<?> tableMapper) {
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
        }, conditionFields,whereSql);

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
     * @return 返回更新的sqlModel 实体
     */
    protected SqlModel<Object> update(Object object, Function<EntityMapper<?>, StringBuilder> function, String[] conditionFields, String whereSqlPart) {

        EntityMapper<?> entityMapper = checkBuildUpdate(object);

        String tableName = entityMapper.getTableName(object);
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update ").append(tableName).append(" set ");

        //XXX: fields  callback
        StringBuilder setSql = function.apply(entityMapper);
        updateSql.append(setSql);

        //where id = 1
        StringBuilder whereSql = new StringBuilder(" where ");

        if(conditionFields != null && conditionFields.length > 0){
            whereSql.append(propertiesCondition(object, entityMapper,conditionFields,whereSqlPart));
        }else  if(whereSqlPart != null && whereSqlPart.trim().length() > 0){
            whereSql.append(whereSqlPart);
        }else {
            whereSql.append(primaryKeysCondition(object, entityMapper));
        }

        String sql = updateSql.append(whereSql).toString();

        SqlModel<Object> model = new SqlModel<Object>(sql, object, entityMapper);
        return model;
    }

    private EntityMapper<?> checkBuildUpdate(Object object) {
        Assert.notNull(object, "build sql for a null object,object must not be null.");

        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(object.getClass());

        String primaryKey = entityMapper.getPrimaryKey();

        if (primaryKey == null) {
            throw new RuntimeException(PRIMARY_KEY_NULL_MSG);
        }
        return entityMapper;
    }

    @Override
    public SqlModel<Object> deleteSqlByEntity(Object object, boolean primaryKey,String tableName)
            throws BuilderSQLException {

        Assert.notNull(object, "build sql error,object must not be null.");

        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(object.getClass());

        StringBuilder sql = new StringBuilder();

        // delete from tableName where primaryKeyName = ?
        sql.append(entityMapper.getDeleteAllSql(object,tableName)).append(" where ");
        int validArgsCount = 0;
        if (primaryKey) {
            sql.append(primaryKeysCondition(object, entityMapper));

        } else {
            // delete from tableName where name = ? and age = 13
            boolean allFieldNull = true;
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(object);
            Collection<FieldMapper> fields = entityMapper.getFieldMapperList();
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
                    if(validArgsCount > 0 ){
                        sql.append(" and ");
                    }
                    sql.append(dbFieldName).append(" = ");
                    sql.append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
                    validArgsCount++;
                }
            }
            //XXX: allFieldNull
            if (allFieldNull) {
                throw new RuntimeException(object.getClass().getName() + "'s all fields are null, how can i build sql for it?!");
            }

        }

        SqlModel<Object> model = new SqlModel<Object>(sql.toString(), object, entityMapper);

        return model;
    }

    @Override
    public SqlModel<Object> deleteSql(SqlConfig sqlConfig) throws BuilderSQLException {
        Assert.notNull(sqlConfig, "sqlConfig config must be not null.");
        Assert.notNull(sqlConfig.getEntityClass(), "SqlConfig.entityClass type must be not null.");

        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(sqlConfig.getEntityClass());
        // delete from tableName where primaryKeyName = ?
        StringBuilder sql = entityMapper.getDeleteAllSql(null, sqlConfig.getTableName());
        StringBuilder where = getWhereSql(null, entityMapper, sqlConfig);
        sql.append(where);
        return new SqlModel<Object>(sql.toString(), entityMapper);
    }

    @Override
    public <T> SqlModel<Map<String, ?>> deleteSqlById(Class<T> entityClass, Serializable id,String tableName)
            throws BuilderSQLException {
        Assert.notNull(entityClass, "build sql error,entityClass must not be null!");
        if (id == null) {
            return SqlModel.EmptySqlModel;
        }
        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
        FieldMapper fieldMapper = entityMapper.getPrimaryKeyFieldMapper();

        StringBuilder sql = new StringBuilder();
        // delete from tableName where primaryKeyName = ?
        sql.append(entityMapper.getDeleteAllSql(null,tableName)).append(" where ");

        sql.append(fieldMapper.getDbFieldName()).append(" = ");
        sql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);

        return paramsMapSqlModel(sql.toString(),entityMapper,fieldMapper,id);
    }

    @Override
    public <T> SqlModel<T> deleteSqlByIds(Class<T> entityClass, Object[] ids,String tableName) throws BuilderSQLException {
        Assert.notNull(entityClass, "build sql error, entityClass must not be null!");
        if (ids == null) {
            return SqlModel.EmptySqlModel;
        }
        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
        FieldMapper fieldMapper = entityMapper.getPrimaryKeyFieldMapper();

        StringBuilder sql = new StringBuilder();
        // delete from tableName where primaryKeyName = ?
        sql.append(entityMapper.getDeleteAllSql(null,tableName)).append(" where ");

        StringBuilder in = SqlUtils.list2In(fieldMapper.getDbFieldName(), ids);
        sql.append(in);

        return new SqlModel<T>(sql.toString(), ids, entityMapper);
    }

    @Override
    public <T> SqlModel<Map<String, ?>> selectSqlById(Class<T> entityClass, Serializable id,String tableName)
            throws BuilderSQLException {
        Assert.notNull(entityClass, "build sql error,entityClass must not be null.");
        if (id == null) {
            return SqlModel.EmptySqlModel;
        }
        EntityMapper<T> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
        StringBuilder all = entityMapper.getSelectAllSql(null,null,false,false,tableName);
        FieldMapper fieldMapper = entityMapper.getPrimaryKeyFieldMapper();

        StringBuilder selectSql = new StringBuilder(all);
        selectSql.append(" where ");
        selectSql.append(fieldMapper.getDbFieldName()).append(" = ");
        selectSql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);

        return paramsMapSqlModel(selectSql.toString(),entityMapper,fieldMapper,id);
    }

    private <T> SqlModel<Map<String, ?>> paramsMapSqlModel(String sql, EntityMapper<T> entityMapper,
                                                           FieldMapper fieldMapper, Serializable id) {
        Map<String, Serializable> paramsMap = new HashMap<String, Serializable>(1);
        paramsMap.put(fieldMapper.getFieldName(), id);
        SqlModel<Map<String, ?>> model = new SqlModel<Map<String, ?>>(sql, paramsMap, entityMapper);
        return model;
    }

    @Override
    public SqlModel<Object> selectSqlById(Object object,String tableName) throws BuilderSQLException {
        Assert.notNull(object, "entity object must be not null.");
        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(object.getClass());

        boolean isDistinct = false;

        StringBuilder selectSql  = entityMapper.getSelectAllSql(object,null,false,isDistinct,tableName);

        selectSql.append(" where ");
        selectSql.append(primaryKeysCondition(object, entityMapper));

        String sql = selectSql.toString();

        SqlModel<Object> model = new SqlModel<Object>(sql, object, entityMapper);

        return model;
    }

    @Override
    public <T> SqlModel<T> selectAllSql(Class<T> entityClass,String tableName) throws BuilderSQLException {
        Assert.notNull(entityClass, "build sql for a null entityClass,entityClass must not be null.");
        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
        String sql = entityMapper.getSelectAllSql(null,null,false,false,tableName).toString();
        SqlModel<T> sqlModel = new SqlModel<T>(sql, entityMapper);
        return sqlModel;
    }

    @Override
    public <T> SqlModel<T> countSql(Class<T> entityClass,String afterWhere) throws BuilderSQLException {
        EntityMapper<T> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
        String sql = entityMapper.getCountSql(null,null).toString();
        SqlModel<T> model = new SqlModel<T>(sql, entityMapper);
        if(afterWhere != null){
            model.setAfterWhere(new String[]{ afterWhere });
        }
        return model;
    }


    @Override
    public SqlModel<Object> countSql(Object object, SqlConfig sqlConfig) throws BuilderSQLException {
        Assert.notNull(object, "build sql for a null object,object must not be null.");
        if(sqlConfig == null){
            sqlConfig = SqlConfig.EMPTY;
        }

        Class<?> entityClass = sqlConfig.getEntityClass() != null ? sqlConfig.getEntityClass() : object.getClass();
        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
        StringBuilder countSql = entityMapper.getCountSql(object,sqlConfig.getTableName());

        StringBuilder whereSql = getWhereSql(object, entityMapper,sqlConfig);
        countSql.append(whereSql);

        String sql = countSql.toString();
        SqlModel<Object> model = new SqlModel<Object>(sql, object, entityMapper,sqlConfig);
        return model;
    }


    @Override
    public <T> SqlModel<T> selectSqlByIds(Class<T> entityClass, Collection<?> ids,String tableName)
            throws BuilderSQLException {
        return selectSqlByIds(entityClass, ids.toArray(),tableName);
    }

    @Override
    public <T> SqlModel<T> selectSqlByIds(Class<T> entityClass, Object[] ids,String tableName)
            throws BuilderSQLException {
        Assert.notNull(entityClass, "build sql for a null entityClass,entityClass must not be null.");
        Assert.notEmpty(ids,"collection or array 'ids' must not be Empty.");

        EntityMapper<T> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);
        StringBuilder sql = entityMapper.getSelectAllSql(null,null,false,false,tableName);

        String primaryKey = entityMapper.getPrimaryKey();
        StringBuilder in = SqlUtils.list2In(primaryKey, ids);
        sql.append(" where ").append(in);

        SqlModel<T> sqlModel = new SqlModel<T>(sql.toString(), ids, entityMapper);
        return sqlModel;
    }


    @Override
    public SqlModel<Map<String, ?>> objectModeSelectSql(Object object, SqlConfig sqlConfig) throws BuilderSQLException {
        Assert.notNull(object, "build sql error,object must not be null.");
        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(object.getClass());

        StringBuilder allSql = getSelectSqlBeforeWhere(object, entityMapper, sqlConfig);
        StringBuilder whereSql = new StringBuilder(allSql).append(" where 1 = 1 ");
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(object);

        Collection<FieldMapper> fields = entityMapper.getFieldMapperList();
        Map<String, Object> namedParameters = new HashMap<String, Object>();

        for (FieldMapper fieldMapper : fields) {
            String fieldName = fieldMapper.getFieldName();
            String dbFieldName = fieldMapper.getDbFieldName();
            Class<?> fieldType = fieldMapper.getPropertyType();
            if (fieldType == Class.class) {
                continue;
            }
            Object fieldValue = bw.getPropertyValue(fieldName);
            SqlOperation sqlOperation = sqlConfig.getSqlOperation(fieldName);
            //XXX：特殊情况可为空
            if (fieldValue == null && sqlOperation != null) {
                sqlOperation.setPropertyType(fieldType);
                String operationType = sqlOperation.getOperationType();
                if (SqlOperation.is_null.equalsIgnoreCase(operationType) || SqlOperation.is_not_null.equalsIgnoreCase(operationType)) {

                    StringBuilder sqlpd = SqlUtils.getCommonWherePart(sqlOperation, fieldValue, dbFieldName, namedParameters);
                    whereSql.append(sqlpd);
                    continue;

                } else if (sqlOperation.isIn()) {

                    StringBuilder sqlpd = SqlUtils.getSpringJdbcWhereIn(sqlOperation, fieldValue, dbFieldName, namedParameters);
                    whereSql.append(sqlpd);
                    continue;

                } else if (sqlOperation.isBetween()) {
                    StringBuilder sqlpd = SqlUtils.getWhereBetween(sqlOperation, fieldValue, dbFieldName, namedParameters);
                    whereSql.append(sqlpd);
                    continue;
                }
            }
            //XXX: 2016-08-10  string not is ""
            if (fieldValue != null && !fieldValue.equals("")) {
                if (sqlOperation == null) {
                    sqlOperation = new SqlOperation(fieldName);
                    sqlOperation.setPropertyType(fieldType);

                }
                StringBuilder sqlWhere = SqlUtils.getSpringJdbcWhere(sqlOperation, fieldValue, dbFieldName, namedParameters);
                //StringBuilder sqlWhere = SqlUtils.getCommonWherePart(sqlPropertyDescriptor, fieldValue, dbFieldName, namedParameters);

                whereSql.append(sqlWhere);
            }

        }

        //:XXX Range beginTime <=  value  <= endTime
        List<SqlOperationRange> ranges = sqlConfig.getSqlOperations().getSqlOperationRanges();
        if (ranges != null && ranges.size() > 0) {
            for (SqlOperationRange sqlOperationRange : ranges) {
                String dbLeftField = entityMapper.getDbFieldName(sqlOperationRange.getLeftField());
                String dbRightField = entityMapper.getDbFieldName(sqlOperationRange.getRightField());
                sqlOperationRange.setDbLeftField(dbLeftField);
                sqlOperationRange.setDbRightField(dbRightField);
                StringBuilder rangeSql = SqlUtils.getPropertyRange(sqlOperationRange, namedParameters);
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
        SqlModel<Map<String, ?>> model = new SqlModel<Map<String, ?>>(sql, namedParameters, entityMapper);
        return model;
    }


    @Override
    public SqlModel<Object> selectSql(Object object, SqlConfig sqlConfig) {
        //Assert.notNull(object, "entity object must be not null.");
        if (object == null && (sqlConfig == null || sqlConfig.getEntityClass() == null)) {
            throw new IllegalArgumentException("mapper entityClass is null,entity object must instance or set SqlConfig.entityClass");
        }
        if (sqlConfig == null) {
            sqlConfig = SqlConfig.EMPTY;
        }
        Class<?> entityClass = sqlConfig.getEntityClass() != null ? sqlConfig.getEntityClass() : object.getClass();
        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(entityClass);

        StringBuilder querySql = getSelectSqlBeforeWhere(object, entityMapper, sqlConfig);

        StringBuilder whereSql = getWhereSql(object, entityMapper, sqlConfig);
        querySql.append(whereSql);

        String sql = querySql.toString();
        SqlModel<Object> model = new SqlModel<Object>(sql, object, entityMapper,sqlConfig);
        RowMapper rowMapper = getRowMapper(sqlConfig);
        if(rowMapper != null){
            model.setRowMapper(rowMapper);
        }
        return model;
    }

    @Override
    public SqlModel<Map<String, ?>> selectSql(Map<String, ?> columnMap, SqlConfig sqlConfig) throws BuilderSQLException {
        //Assert.notNull(columnMap, "map columnMap must be not null.");
        Assert.notNull(sqlConfig, "sqlConfig config must be not null.");
        Assert.notNull(sqlConfig.getEntityClass(), "SqlConfig.entityClass type must be not null.");

        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(sqlConfig.getEntityClass());

        StringBuilder querySql = getSelectSqlBeforeWhere(columnMap, entityMapper, sqlConfig);

        //selectWhere
        StringBuilder whereSql = getWhereSql(columnMap, entityMapper, sqlConfig);
        querySql.append(whereSql);

        String sql = querySql.toString();
        SqlModel<Map<String, ?> > model = new SqlModel<Map<String, ?>>(sql, columnMap, entityMapper,sqlConfig);
        RowMapper rowMapper = getRowMapper(sqlConfig);
        if(rowMapper != null){
            model.setRowMapper(rowMapper);
        }
        return model;
    }

    @Override
    public SqlModel<Map<String,Object>> requestQuery(ServletRequest request, SqlConfig sqlConfig) {
        Assert.notNull(request, "ServletRequest request must be not null.");
        Assert.notNull(sqlConfig, "sqlConfig config must be not null.");
        Assert.notNull(sqlConfig.getEntityClass(), "entityClass type must be not null.");

        EntityMapper<?> entityMapper = MappingBuilder.ME.getTableMapper(sqlConfig.getEntityClass());
        StringBuilder querySql = getSelectSqlBeforeWhere(request, entityMapper, sqlConfig);
        StringBuilder selectWhere = querySql;

        StringBuilder whereSql = new StringBuilder(" where ");
        int validArgsCount = 0;
        if (StringUtils.hasLength(sqlConfig.getFirstCondition())) {
            whereSql.append(sqlConfig.getFirstCondition()).append(" ");
            validArgsCount ++;
        } else {
            whereSql.append("1 = 1 ");
            validArgsCount ++;
        }

        boolean dbFieldMode = sqlConfig.isDbFieldMode();
        Enumeration<String> enumeration = request.getParameterNames();
        List<String> extFields = new ArrayList<String>();
        Map<String,Object> columnMap = new HashMap<String,Object>();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            FieldMapper fieldMapper;
            if (dbFieldMode) {
                fieldMapper = entityMapper.getFieldMapper(name);
            } else {
                fieldMapper = entityMapper.getFieldMapperByProperty(name);
            }
            if (fieldMapper == null) {
                extFields.add(name);
                continue;
            }

            Class fieldType = fieldMapper.getPropertyType();
            if (fieldType == Class.class) {
                continue;
            }
            String nameValue = request.getParameter(name);
            Object fieldValue = nameValue;
            //XXX: value is null or value is ""  break
            if (fieldValue == null || fieldValue.equals("")) {
                continue;
            }
            // value format
            if(Number.class.isAssignableFrom(fieldType)){
                fieldValue = NumberUtils.parseNumber(nameValue,fieldType);
            }else if(Date.class.isAssignableFrom(fieldType)){
                fieldValue =  DateFPUtils.parseDate(nameValue, DateConstant.DEFAULT_FORMATS);
            }
            //按照配置追加条件
            appendCondition(whereSql, fieldValue,validArgsCount, fieldMapper, sqlConfig,null);
            columnMap.put(name,fieldValue);
        }
        //扩展属性条件查询 in / between
        extFieldsCondition(request,columnMap, extFields, entityMapper, whereSql);

        String orderBy = RequestQueryUtils.getOrderBy(request);
        if (orderBy != null) {
            sqlConfig.orderBy(orderBy);
            columnMap.put("orderBy",orderBy);
        }
        //设置分组 排序字段
        appendGroupOrder(whereSql, sqlConfig);

        selectWhere.append(whereSql);

        String sql = selectWhere.toString();
        SqlModel<Map<String,Object>> model = new SqlModel<>(sql, columnMap, entityMapper, sqlConfig);
        RowMapper rowMapper = getRowMapper(sqlConfig);
        if(rowMapper != null){
            model.setRowMapper(rowMapper);
        }
        return model;
    }

    public <T> RowMapper<T> getRowMapper(SqlConfig sqlConfig) {
        GroupQuery groupQuery = sqlConfig.getGroupQuery();
        // group by  query
        if (groupQuery != null && groupQuery.getMappingClass() != null) {
            return getRowMapper(groupQuery.getMappingClass());
        }
        // query
        Class<?> mappingClass = sqlConfig.getMappingClass();
        return getRowMapper(mappingClass);
    }
    @Override
    public <T> RowMapper<T> getRowMapper(GroupQuery groupQuery) {
        if(groupQuery != null && groupQuery.getMappingClass() != null){
            return getRowMapper(groupQuery.getMappingClass());
        }
        return  null;
    }

    private <T> RowMapper<T> getRowMapper(Class<?> mappingClass) {
        if (mappingClass == null) {
            return null;
        }
        RowMapper<T> rowMapper = null;
        if (Map.class.isAssignableFrom(mappingClass)) {
            rowMapper = (RowMapper<T>) new ColumnMapRowMapper();
        } else {
            EntityMapper<T> entityMapper = (EntityMapper<T>) MappingBuilder.ME.getTableMapper(mappingClass);
            if (entityMapper != null) {
                rowMapper = entityMapper.getRowMapper();
            }
        }
        return rowMapper;
    }

    /**
     * 获取查询sql  where 之前的语句 select * from  test
     * <ul>
     * <li>构建字段列表</li>
     * <li>是否去重</li>
     * </ul>
     *
     * @param object 实体对象
     * @param entityMapper 实体映射对象
     * @param sqlConfig    sql查询配置
     * @return 返回sql  fragment
     */
    private StringBuilder getSelectSqlBeforeWhere(Object object, EntityMapper<?> entityMapper, SqlConfig sqlConfig) {
        String[] fieldNames = sqlConfig.getFieldNames();
        StringBuilder query;
        boolean isDistinct    = sqlConfig.isDistinct();
        GroupQuery groupQuery = sqlConfig.getGroupQuery();
        String tableName = sqlConfig.getTableName();
        if(groupQuery == null){
            //1.优先使用自定义查询
            //2.字段级别次之
            FieldLevel fieldLevel = sqlConfig.getFieldLevel();
            String[] excludeFields = sqlConfig.getExcludeFields();
            boolean dbFieldMode = sqlConfig.isDbFieldMode();
            query = entityMapper.getSelectAllSql(object, fieldNames,excludeFields, fieldLevel, dbFieldMode, isDistinct, tableName);
        }else {
            //分组查询 构建分组查询字段
            query = entityMapper.getGroupSelectAllSql(object,groupQuery.getQueryColumnBySql(),tableName);
        }
        return query;
    }

    private StringBuilder getWhereSql(Object object, EntityMapper<?> entityMapper, SqlConfig sqlConfig) {
        StringBuilder whereSql = new StringBuilder(" where ");
        int validArgsCount = 0;
        boolean dbFieldMode = sqlConfig.isDbFieldMode();
        SqlOperation firstOperation = sqlConfig.getFirstOperation();
        if (StringUtils.hasLength(sqlConfig.getFirstCondition())) {
            whereSql.append(sqlConfig.getFirstCondition()).append(" ");
            validArgsCount++;
        } else if (firstOperation != null) {
            FieldMapper fieldMapper = entityMapper.getFieldMapperByProperty(firstOperation.getFieldName());
            validArgsCount = appendCondition(whereSql, validArgsCount, firstOperation, fieldMapper);
        } else {
            whereSql.append("1 = 1 ");
            validArgsCount ++;
        }
        List<String> ignoreFields = sqlConfig.getIgnoreFields();
        Set<String> extFields = null;
        if (object != null && object instanceof Map) {
            Map<String,?> columnMap = (Map<String, ?>) object;
            for (Map.Entry<String,?> entry : columnMap.entrySet()) {
                FieldMapper fieldMapper;
                if(dbFieldMode){
                    fieldMapper = entityMapper.getFieldMapper(entry.getKey());
                }else {
                    fieldMapper = entityMapper.getFieldMapperByProperty(entry.getKey());
                }
                if (fieldMapper == null && sqlConfig.isUsedExtField()) {
                    if(extFields == null){
                        extFields = new HashSet<String>();
                    }
                    extFields.add(entry.getKey());
                    continue;
                }
                if(fieldMapper == null){
                    continue;
                }
                Class<?> fieldType = fieldMapper.getPropertyType();
                if (fieldType == Class.class) {
                    continue;
                }
                Object fieldValue = entry.getValue();
                //XXX: value is null or value is ""  break
                if (fieldValue == null || fieldValue.equals("")) {
                    //try append SqlOperation in / between
                    validArgsCount = appendCondition(whereSql,validArgsCount,sqlConfig.getSqlOperation(fieldMapper.getFieldName()),fieldMapper);
                    continue;
                }
                //按照配置追加条件
                appendCondition(whereSql,fieldValue,validArgsCount,fieldMapper,sqlConfig,null);
                validArgsCount ++;
            }
        }
        else if(object != null && !ObjectUtils.isEmpty(sqlConfig.getConditionFields())){
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(object);
            for (String conditionField : sqlConfig.getConditionFields()) {
                FieldMapper fieldMapper;
                if(dbFieldMode){
                    fieldMapper = entityMapper.getFieldMapper(conditionField);
                }else {
                    fieldMapper = entityMapper.getFieldMapperByProperty(conditionField);
                }
                if (fieldMapper == null && sqlConfig.isUsedExtField()) {
                    if(extFields == null){
                        extFields = new HashSet<String>();
                    }
                    extFields.add(conditionField);
                    continue;
                }
                if(fieldMapper == null){
                    continue;
                }
                String fieldName = fieldMapper.getFieldName();
                Object fieldValue = bw.getPropertyValue(fieldName);
                if (fieldValue == null || fieldValue.equals("")) {
                    //try append SqlOperation in / between
                    validArgsCount = appendCondition(whereSql,validArgsCount,sqlConfig.getSqlOperation(fieldName),fieldMapper);
                    continue;
                }
                //按照配置追加条件
                appendCondition(whereSql,fieldValue,validArgsCount,fieldMapper,sqlConfig,null);
                validArgsCount ++;
            }
        }
        else if(object != null && sqlConfig.getEntityClass() == null){
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(object);
            Collection<FieldMapper> fields = entityMapper.getFieldMapperList();
            for (FieldMapper fieldMapper : fields) {
                String fieldName = fieldMapper.getFieldName();
                Class<?> fieldType = fieldMapper.getPropertyType();
                if (fieldType == Class.class) {
                    continue;
                }
                Object fieldValue = bw.getPropertyValue(fieldName);
                //XXX: value is null or value is ""  break
                if (fieldValue == null || fieldValue.equals("")) {
                    //try append SqlOperation in / between
                    validArgsCount = appendCondition(whereSql,validArgsCount,sqlConfig.getSqlOperation(fieldName),fieldMapper);
                    continue;
                }
                //按照配置追加条件
                appendCondition(whereSql,fieldValue,validArgsCount,fieldMapper,sqlConfig,null);
                validArgsCount ++;
            }
        }
        //非映射实体类实行查询
        else if (object != null && sqlConfig.getEntityClass() != null) {
            PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(object.getClass());
            Map<String,QueryOperation> operationMap =  entityMapper.getExtFieldsMap(object.getClass());
            for (PropertyDescriptor descriptor : descriptors) {
                String name = descriptor.getName();
                if (IGNORE_PROPERTY.contains(name)) {
                    continue;
                }
                if (ignoreFields != null && ignoreFields.contains(name)) {
                    continue;
                }
                Object fieldValue = ReflectionUtils.invokeMethod(descriptor.getReadMethod(), object);
                if (fieldValue == null || fieldValue.equals("")) {
                    continue;
                }
                FieldMapper fieldMapper = entityMapper.getFieldMapperByProperty(name);

                QueryOperation queryOperation = null;
                if (operationMap != null && operationMap.size() > 0) {
                    queryOperation = operationMap.get(name);
                }
                if (fieldMapper == null && queryOperation != null) {
                    fieldMapper = entityMapper.getFieldMapperByProperty(queryOperation.getName());
                }
                // query for in / between
                if (fieldValue instanceof Collection || fieldValue.getClass().isArray()
                        || (fieldMapper == null && sqlConfig.isUsedExtField())) {
                    if (extFields == null) {
                        extFields = new HashSet<String>();
                    }
                    extFields.add(name);
                    continue;
                }
                if(fieldMapper == null){
                    continue;
                }
                //按照配置追加条件
                appendCondition(whereSql, fieldValue,validArgsCount, fieldMapper, sqlConfig,queryOperation);
                validArgsCount ++;
            }

        } else {
            //append sqlOperations {fieldName,operationType,value}
            appendConditions(whereSql,validArgsCount, entityMapper, sqlConfig);
        }
        //append Condition
        if (StringUtils.hasLength(sqlConfig.getCondition())) {
            whereSql.append(sqlConfig.getCondition()).append(" ");
        }
        if (sqlConfig.isUsedExtField()) {
            String[] extFieldNames = sqlConfig.getExtFields();
            if (extFields != null) {
                if (extFieldNames != null) {
                    for (String extField : extFieldNames) {
                        extFields.add(extField);
                    }
                }
                extFieldsCondition(object, extFields, entityMapper, whereSql);
            } else {
                //扩展属性条件查询 in / between
                extFieldsCondition(object, extFieldNames, entityMapper, whereSql);
            }
        }
        //设置分组 排序字段
        appendGroupOrder(whereSql,sqlConfig);
        return whereSql;
    }

    private StringBuilder appendConditions(StringBuilder whereSql,int argsCount, EntityMapper<?> entityMapper, SqlConfig sqlConfig) {
        Map<String,SqlOperation>  operationAsMap = sqlConfig.getSqlOperationAsMap();
        if(operationAsMap == null || operationAsMap.isEmpty()){
            return whereSql;
        }
        int validArgsCount = argsCount;
        for (SqlOperation operation : operationAsMap.values()) {
            String fieldName  = operation.getFieldName();
            FieldMapper fieldMapper = entityMapper.getFieldMapperByProperty(fieldName);
            if(fieldMapper == null){
                throw  new IllegalArgumentException("class [" + entityMapper.getEntityName() + "] Cannot resolve field: " +fieldName);
            }
            validArgsCount = appendCondition(whereSql, validArgsCount, operation,fieldMapper);
        }

        return whereSql;
    }
    private int appendCondition(StringBuilder whereSql,int argsCount,SqlOperation sqlOperation,FieldMapper fieldMapper) {
        String fieldName = fieldMapper.getFieldName();
        if (sqlOperation != null && StringUtils.hasLength(sqlOperation.getOperationType()) && sqlOperation.getValue() != null) {
            String operationType = sqlOperation.getOperationType();
            Object fieldValue = sqlOperation.getValue();
            if (sqlOperation.isIn() || sqlOperation.isBetween()) {
                QueryType queryType = QueryType.of(operationType);
                StringBuilder sql = fieldValueCondition(fieldValue, fieldValue.getClass(), fieldMapper, argsCount > 0 ? "and":"", queryType);
                if (sql != null) {
                    whereSql.append(sql);
                    argsCount ++ ;
                }
            }else {
                String dbFieldName = fieldMapper.getDbFieldName();
                if(Number.class.isAssignableFrom(fieldValue.getClass())){
                    if(argsCount > 0){
                        whereSql.append(" and ");
                    }
                    whereSql.append(dbFieldName).append(" ").append(operationType).append(" ");
                    whereSql.append(fieldValue);
                    argsCount ++ ;
                }else if (CharSequence.class.isAssignableFrom(fieldValue.getClass())) {
                    if(argsCount > 0){
                        whereSql.append(" and ");
                    }
                    whereSql.append(dbFieldName).append(" ").append(operationType).append(" ");
                    whereSql.append("'").append(fieldValue).append("'");
                    argsCount ++ ;
                }
            }
        }
        return argsCount;
    }

    private StringBuilder appendCondition(StringBuilder whereSql,Object value,int argsCount,FieldMapper fieldMapper,
                                          SqlConfig sqlConfig,QueryOperation fieldConfig){
        Object fieldValue = value;
        String fieldName = fieldMapper.getFieldName();
        String dbFieldName = fieldMapper.getDbFieldName();
        Class<?> fieldType = fieldMapper.getPropertyType();
        int sqlMode = sqlConfig.getSqlMode();
        if (argsCount > 0) {
            whereSql.append(" and ");
        }
        // and id = :id;
        SqlOperation sqlOperation = sqlConfig.getSqlOperation(fieldName);
        if (sqlOperation != null && StringUtils.hasLength(sqlOperation.getOperationType())) {
            String operationType = sqlOperation.getOperationType();
            whereSql.append(dbFieldName).append(" ").append(operationType).append(" ");
            if (QueryType.LIKE == QueryType.of(operationType)) {
                whereSql.append("'%").append(fieldValue).append("%'");
            } else {
                whereSql.append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
            }
            return whereSql;
        }
        if (fieldConfig != null) {
            QueryType queryType = fieldConfig.getQueryType();
            whereSql.append(dbFieldName).append(" ").append(queryType.getOperation()).append(" ");
            whereSql.append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
            return whereSql;
        }
        if (sqlMode == SqlConfig.PrimaryMode) {
            if (SimpleTypeMapper.isPrimitiveWithString(fieldType)) {

                whereSql.append(dbFieldName).append(" like  '%").append(fieldValue).append("%'");

            /*	whereSql.append(" and ").append(dbFieldName).append(" like  '%");
                whereSql.append(placeholderPrefix).append(fieldMapper.getFieldName()).append(placeholderSuffix);
                whereSql.append("%'");*/

            } else {
                whereSql.append(dbFieldName).append(" = ");
                whereSql.append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
            }


        } else if (sqlMode == SqlConfig.MiddleMode) {


        } else if (sqlMode == SqlConfig.HighMode) {
            if (sqlOperation == null) {
                sqlOperation = new SqlOperation(fieldName);
            }
            //SqlUtils.getSpringJdbcWhere(sqlOperation, fieldValue, dbFieldName, namedParameters);


        } else {
            whereSql.append(dbFieldName).append(" = ");
            whereSql.append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
        }
        return  whereSql;
    }

    private StringBuilder appendGroupOrder(StringBuilder whereSql,SqlConfig sqlConfig){
        GroupQuery groupQuery = sqlConfig.getGroupQuery();
        if(groupQuery != null){
            if(groupQuery.getOrderBySql() != null && sqlConfig.getOrderBy() != null){
                throw  new IllegalArgumentException("Invalid order configuration on 'groupQuery.orderby' and 'sqlConfig.orderby' attributes have been set.These attributes are mutually exclusive.");
            }
            whereSql.append(groupQuery.getGroupBySql());
            whereSql.append(groupQuery.getOrderBySql());
        }
        if(StringUtils.hasLength(sqlConfig.getOrderBy())){
            whereSql.append( " order by ").append(sqlConfig.getOrderBy());
        }
        return  whereSql;
    }

    /**
     *  extFieldNames<li> string name
     *               <li> string[] names
     *               <li> Collection names
     * extPropertysCondition
     */
    @SuppressWarnings("uncheck")
    private StringBuilder extFieldsCondition(Object object, Object extFieldNames, EntityMapper<?> entityMapper, StringBuilder whereSql){
        if(object == null){
            return whereSql;
        }
        if (object instanceof Map && extFieldNames instanceof Collection) {
            return extFieldsCondition((Map<String, ?>) object, (Collection<String>) extFieldNames, entityMapper, whereSql);
        }
        if(!ObjectUtils.isEmpty(extFieldNames)){
            if(extFieldNames.getClass().isArray()){
                for (String extFieldName : (String[])extFieldNames) {
                    StringBuilder extFieldSql =  extFieldCondition(object,extFieldName,null, entityMapper,"and");
                    if(extFieldSql != null){
                        whereSql.append(" ") .append(extFieldSql);
                    }
                }
            }else if( extFieldNames instanceof Collection){
                for (String extFieldName : (Collection<String>)extFieldNames) {
                    StringBuilder extFieldSql =  extFieldCondition(object,extFieldName,null, entityMapper,"and");
                    if(extFieldSql != null){
                        whereSql.append(" ") .append(extFieldSql);
                    }
                }
            }
            else if(extFieldNames instanceof String){
                StringBuilder extFieldSql =  extFieldCondition(object, (String) extFieldNames,null, entityMapper,"and");
                if(extFieldSql != null){
                    whereSql.append(" ") .append(extFieldSql);
                }
            }
        }else {
            Map<String,QueryOperation> extFields = entityMapper.getExtFieldsMap(object.getClass());
            if(extFields == null || extFields.size() == 0){
                return whereSql;
            }
            for (Map.Entry<String,QueryOperation> entry : extFields.entrySet()) {
                StringBuilder extSql =  extFieldCondition(object,entry.getKey(),entry.getValue(), entityMapper,"and");
                if(extSql != null){
                    whereSql.append(extSql);
                }
            }
        }
        return whereSql;

    }
    private StringBuilder extFieldsCondition(Map<String,?> columnMap, Collection<String> extFieldNames,
                                             EntityMapper<?> entityMapper, StringBuilder whereSql){
        if(columnMap == null){
            return whereSql;
        }
        for (String extFieldName : extFieldNames) {
            Object value  = columnMap.get(extFieldName);
            if (value == null) {
                continue;
            }
            QueryOperation field = entityMapper.getExtField(extFieldName);
            if(field == null){
                parameterConvention(extFieldName,value,false,entityMapper,whereSql);
                logger.warn("class [" + entityMapper.getEntityName() + "] Cannot resolve field: " + extFieldName);
                continue;
            }
            StringBuilder extFieldSql =  extFieldValueCondition(value,field, entityMapper,"and");
            if(extFieldSql != null){
                whereSql.append(" ") .append(extFieldSql);
            }
        }
        return whereSql;
    }

    private StringBuilder extFieldsCondition(ServletRequest request,Map<String,Object> columnMap,List<String> extFieldNames,
                                             EntityMapper<?> entityMapper, StringBuilder whereSql) {
        for (String extFieldName : extFieldNames) {
            Object value  = request.getParameter(extFieldName);
            if (value == null) {
                continue;
            }
            columnMap.put(extFieldName,value);
            QueryOperation field = entityMapper.getExtField(extFieldName);
            if(field == null){
                parameterConvention(extFieldName,value,true,entityMapper,whereSql);
                logger.warn("class [" + entityMapper.getEntityName() + "] Cannot resolve field: " + extFieldName);
                continue;
            }
            StringBuilder extFieldSql =  extFieldValueCondition(value,field, entityMapper,"and");
            if(extFieldSql != null){
                whereSql.append(" ") .append(extFieldSql);
            }
        }
        return whereSql;
    }


    /**
     * 按照参数规约查询
     */
    private StringBuilder parameterConvention(String extFieldName, Object value,boolean format, EntityMapper<?> entityMapper, StringBuilder whereSql) {
        // try smart  matching
        if (extFieldName.endsWith(EntityMapper.S)) {
            String fieldName = extFieldName.substring(0, extFieldName.length() - 1);
            FieldMapper fieldMapper = entityMapper.getFieldMapperByProperty(fieldName);
            if (fieldMapper != null) {
                String dbFieldName = fieldMapper.getDbFieldName();
                if(format && value instanceof String &&  String.class.isAssignableFrom(fieldMapper.getPropertyType())){
                    value = StringUtils.commaDelimitedListToStringArray((String) value);
                }
                StringBuilder in = SqlUtils.in("and", dbFieldName, fieldMapper.getPropertyType(), value, true);
                if (in != null) {
                    whereSql.append(in);
                }
            }
        } else if (extFieldName.endsWith(EntityMapper.BETWEEN)) {
            String fieldName = extFieldName.substring(0, extFieldName.length() - EntityMapper.BETWEEN.length());
            FieldMapper fieldMapper = entityMapper.getFieldMapperByProperty(fieldName);
            between("and", value, fieldMapper, whereSql);

        } else if (extFieldName.endsWith(EntityMapper._BETWEEN)) {
            String fieldName = extFieldName.substring(0, extFieldName.length() - EntityMapper._BETWEEN.length());
            FieldMapper fieldMapper = entityMapper.getFieldMapperByProperty(fieldName);
            between("and", value, fieldMapper, whereSql);
        }

        return whereSql;
    }


    private StringBuilder between(String andOr,Object value, FieldMapper fieldMapper, StringBuilder whereSql) {
        if (value == null || fieldMapper == null) {
            return whereSql;
        }
        String dbFieldName = fieldMapper.getDbFieldName();
        StringBuilder between = SqlUtils.between(andOr, dbFieldName, value, true);
        if (between != null) {
            whereSql.append(" ").append(between);
        }
        return whereSql;
    }

    private StringBuilder extFieldCondition(Object object, String extFieldName,QueryOperation queryOperation,
                                            EntityMapper<?> entityMapper, String andOr)  {
        Object value = getProperty(object, extFieldName);
        if(queryOperation == null){
            queryOperation =  entityMapper.getExtField(object.getClass(),extFieldName);
        }
        if(queryOperation == null){
            throw  new IllegalArgumentException("class [" + object.getClass().getName() + "] Cannot resolve field: " +extFieldName );
        }
        return extFieldValueCondition(value, queryOperation, entityMapper, andOr);
    }

    private StringBuilder extFieldValueCondition(Object fieldValue, QueryOperation queryOperation,
                                                 EntityMapper<?> entityMapper, String andOr) {
        if (fieldValue == null) {
            return null;
        }
        QueryType queryType = queryOperation.getQueryType();
        Class<?> fieldType = queryOperation.getFieldType();
        FieldMapper fieldMapper = entityMapper.getFieldMapperByProperty(queryOperation.getName());
        return fieldValueCondition(fieldValue,fieldType,fieldMapper,andOr,queryType);
    }
    private StringBuilder fieldValueCondition(Object fieldValue,Class<?> valueType, FieldMapper fieldMapper,
                                              String andOr,QueryType queryType) {
        if (fieldValue == null) {
            return null;
        }
        if (valueType == null) {
            valueType = fieldValue.getClass();
        }
        String dbFieldName = fieldMapper.getDbFieldName();
        StringBuilder extSql = null;
        switch (queryType) {
            case IN:
                if (valueType.isArray() || Collection.class.isAssignableFrom(valueType)
                        || (Number.class.isAssignableFrom(fieldMapper.getPropertyType()) && fieldValue instanceof String)) {
                    extSql = SqlUtils.in(andOr, dbFieldName, fieldMapper.getPropertyType(), fieldValue, true);
                } else if ((String.class.isAssignableFrom(fieldMapper.getPropertyType()) && fieldValue instanceof String)) {
                    String[] array = StringUtils.commaDelimitedListToStringArray((String) fieldValue);
                    extSql = SqlUtils.in(andOr, dbFieldName, fieldMapper.getPropertyType(), array, true);
                }
                break;
            case NOT_IN:
                if (valueType.isArray() || Collection.class.isAssignableFrom(valueType)
                        || (Number.class.isAssignableFrom(fieldMapper.getPropertyType()) && fieldValue instanceof String)) {
                    extSql = SqlUtils.in(andOr, dbFieldName, fieldMapper.getPropertyType(), fieldValue, false);
                } else if ((String.class.isAssignableFrom(fieldMapper.getPropertyType()) && fieldValue instanceof String)) {
                    String[] array = StringUtils.commaDelimitedListToStringArray((String) fieldValue);
                    extSql = SqlUtils.in(andOr, dbFieldName, fieldMapper.getPropertyType(), array, true);
                }
                break;
            case BETWEEN:
                if (valueType.isArray() || Collection.class.isAssignableFrom(valueType)) {
                    extSql = SqlUtils.between(andOr, dbFieldName, fieldValue, true);
                } else if (fieldValue instanceof String) {
                    String[] array = StringUtils.commaDelimitedListToStringArray((String) fieldValue);
                    if (array.length == 2) {
                        extSql = SqlUtils.between(andOr, dbFieldName, array, true);
                    }
                }
                break;
            case NOT_BETWEEN:
                if (valueType.isArray() || Collection.class.isAssignableFrom(valueType)) {
                    extSql = SqlUtils.between(andOr, dbFieldName, fieldValue, false);
                } else if (fieldValue instanceof String) {
                    String[] array = StringUtils.commaDelimitedListToStringArray((String) fieldValue);
                    if (array.length == 2) {
                        extSql = SqlUtils.between(andOr, dbFieldName, array, false);
                    }
                }
                break;
            default:
                break;
        }
        return extSql;
    }

    /**
     * 获取对象的属性值
     *
     * @param bean entity 实例对象
     * @param name 字段名称
     * @return 返回对象的属性值
     */
    private  Object getProperty(Object bean, String name) {
        Field field = ReflectionUtils.findField(bean.getClass(),name);
        if(field == null){
            throw  new IllegalArgumentException("class [" + bean.getClass().getName() + "] Cannot resolve field: " +name );
        }
        //boolean accessible = field.isAccessible();
        ReflectionUtils.makeAccessible(field);
        Object value  =  ReflectionUtils.getField(field,bean);

        //field.setAccessible(accessible);
        return value;
    }

    //-----------------------------------------------------------------------------
    //
    //-----------------------------------------------------------------------------

    /**
     * @see #primaryKeysCondition(Class, EntityMapper)
     */
    private StringBuilder primaryKeysCondition(Object object, EntityMapper<?> entityMapper) throws BuilderSQLException {
        Assert.notNull(object, "build sql for a null object,object must not be null.!");

        // object PrimaryKey value validata
        List<FieldMapper> primaryKeys = entityMapper.getPrimaryKeyFields();
        for (int i = 0; i < primaryKeys.size(); i++) {
            FieldMapper fieldMapper = primaryKeys.get(i);
            String fieldName = fieldMapper.getFieldName();
            Object value = PropertyUtil.getProperty(object, fieldName);
            if (value == null) {
                throw new RuntimeException("Unique key '" + fieldMapper.getDbFieldName() + "' value can't be null.");
            }

        }

        return this.primaryKeysCondition(object.getClass(), entityMapper);
    }

    /**
     * where主键条件
     * <p> eg: id = 1 and name = chen
     *
     * @param entityClass  实体类型
     * @param entityMapper 实体映射对象
     * @return 返回主键条件sql
     */
    private StringBuilder primaryKeysCondition(Class<?> entityClass, EntityMapper<?> entityMapper) throws BuilderSQLException {
        List<FieldMapper> primaryKeys = entityMapper.getPrimaryKeyFields();
        StringBuilder sql = new StringBuilder();
        for (int i = 0; i < primaryKeys.size(); i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            FieldMapper fieldMapper = primaryKeys.get(i);
            String fieldName = fieldMapper.getFieldName();
            String dbFieldName = fieldMapper.getDbFieldName();

            sql.append(dbFieldName);
            sql.append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
            //sql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
        }

        return sql;
    }

    /**
     * where 属性条件
     * <p> eg: id = 1 and name = chen
     *
     * @param object  按实体对象
     * @param entityMapper  实体映射对象
     * @param conditionFields 属性条件 可为空， 当为空时自动使用主键条件
     * @param appendSql  追加条件sql   and status !=1
     * @return StringBuilder
     */
    private StringBuilder propertiesCondition(Object object, EntityMapper<?> entityMapper, String[] conditionFields, String appendSql) throws BuilderSQLException {
        StringBuilder sql = new StringBuilder();
        if(conditionFields != null){
            List<String> extFields = new ArrayList<String>();
            int hasValueCount = 0;
            for (int i = 0; i < conditionFields.length; i++) {
                String propertyName = conditionFields[i];
                FieldMapper fieldMapper = entityMapper.getFieldMapperByProperty(propertyName);
                //extFields process
                if(fieldMapper ==null){
                    extFields.add(propertyName);
                    continue;
                }
                Object value  = getProperty(object,propertyName);
                if(value == null){
                    logger.warn("class [" + object.getClass().getName() + "] field " +propertyName +" = null.thinking");
                    continue;
                }

                if (hasValueCount > 0) {
                    sql.append(" and ");
                }
                String fieldName = fieldMapper.getFieldName();
                sql.append(fieldMapper.getDbFieldName());
                sql.append(" = ").append(placeholderPrefix).append(fieldName).append(placeholderSuffix);
                hasValueCount ++;
                //sql.append(",").append("jdbcType=").append(fieldMapper.getJdbcType().toString());
            }
            // not input extField use default config
            extFieldsCondition(object,extFields, entityMapper,sql);
        }

        SqlUtils.appendCondition(sql,appendSql);
        return sql;
    }


    public static <E> EntityMapper<E> buildEntityMapper(Class<E> entityClass) {
        EntityMapper<E> entityMapper;
        //load
        entityMapper = MappingBuilder.ME.buildEntityMapper(entityClass);

        return entityMapper;
    }

    public static <E> EntityMapper<E> buildTableMapper(Class<E> entityClass, String namespace, String id) {
        Assert.notNull(namespace, "namespace is not null.thinking");
        Assert.notNull(id, "id is not null.thinking");
        EntityMapper<E> entityMapper;
        //get cache
        if (entityClass != null) {
            entityMapper = MappingBuilder.ME.getEntityCache(entityClass);
            if (entityMapper != null) {
                return entityMapper;
            }
        }
        synchronized (MappingBuilder.ME) {
            //load
            entityMapper = MyMapperUtils.resultMap2TableMapper(entityClass, namespace, id);
            MappingBuilder.ME.addEntityMapper(entityMapper.getEntityClass(), entityMapper);

        }
        return entityMapper;
    }

    public static <E> EntityMapper<E> buildTableMapper(String namespace, String id) {
        return buildTableMapper(null,namespace,id);
    }

}
