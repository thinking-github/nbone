package org.nbone.framework.spring.dao.core;

import org.nbone.persistence.mapper.FieldMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询数据返回Map 默认按照主键作为Key @MapKey
 *
 * @author thinking
 * @version 1.0
 * @since 2019-09-26
 */
public class RowMapperWithMapExtractor<T> implements ResultSetExtractor<Map<?, T>> {

    private final RowMapper<T> rowMapper;

    private final int rowsExpected;

    /**
     * key name 可为空 当此值为空时默认使用主键
     */
    private String keyName;

    /**
     * 可为空, 为空时使用目标实体字段类型
     */
    private Class<?> keyType;

    /**
     * key value name 可为空
     */
    private String valueName;


    /**
     * Create a new RowMapperResultSetExtractor.
     *
     * @param rowMapper the RowMapper which creates an object for each row
     */
    public RowMapperWithMapExtractor(RowMapper<T> rowMapper, String keyName,Class<?> keyType, String valueName) {
        this(rowMapper, 0, keyName,keyType, valueName);
    }

    /**
     * Create a new RowMapperResultSetExtractor.
     *
     * @param rowMapper    the RowMapper which creates an object for each row
     * @param rowsExpected the number of expected rows
     *                     (just used for optimized collection handling)
     */
    public RowMapperWithMapExtractor(RowMapper<T> rowMapper, int rowsExpected, String keyName,Class<?> keyType,String valueName) {
        Assert.notNull(rowMapper, "RowMapper is required");
        this.rowMapper = rowMapper;
        this.rowsExpected = rowsExpected;
        this.keyName = keyName;
        this.keyType = keyType;
        this.valueName = valueName;
    }

    @Override
    public Map<?, T> extractData(ResultSet rs) throws SQLException {
        Map<? super Object, T> results = (this.rowsExpected > 0 ? new LinkedHashMap<Object, T>(this.rowsExpected) : new LinkedHashMap<Object, T>());
        if (StringUtils.hasLength(keyName) && StringUtils.hasLength(valueName)) {
            while (rs.next()) {
                Object nameKey = JdbcUtils.getResultSetValue(rs,1 ,keyType);
                T value = (T) rs.getObject(2);
                results.put(nameKey, value);
            }
            return results;
        }
        int rowNum = 0;
        if (rowMapper instanceof EntityPropertyRowMapper) {
            EntityPropertyRowMapper entityRowMapper = (EntityPropertyRowMapper<T>) rowMapper;
            FieldMapper fieldMapper;
            // name empty def use pk
            if (StringUtils.isEmpty(keyName)) {
                fieldMapper = entityRowMapper.getEntityMapper().getPrimaryKeyFieldMapper();
            } else {
                fieldMapper = entityRowMapper.getEntityMapper().getFieldMapperByProperty(keyName);
            }
            if (fieldMapper == null) {
                throw new IllegalArgumentException("class [" + entityRowMapper.getMappedClass() + "] MapKey name Cannot resolve field: " + keyName);
            }

            Class<?> keyType = this.keyType != null ? this.keyType : fieldMapper.getPropertyType();
            while (rs.next()) {
                T entity = this.rowMapper.mapRow(rs, rowNum++);
                int index = rs.findColumn(fieldMapper.getDbFieldName());
                Object nameKey = JdbcUtils.getResultSetValue(rs, index, keyType);
                results.put(nameKey, entity);
            }
            return results;
        } else {
            throw new IllegalArgumentException("current rowMapper [" + rowMapper.getClass().getName() + "] must use [" + EntityPropertyRowMapper.class.getName() + "]");
        }


    }


}
