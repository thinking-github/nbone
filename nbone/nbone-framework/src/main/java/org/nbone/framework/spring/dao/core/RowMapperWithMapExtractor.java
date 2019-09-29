package org.nbone.framework.spring.dao.core;

import org.nbone.persistence.mapper.FieldMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
     * key name 当此值为空时默认使用主键
     */
    private String name;


    /**
     * Create a new RowMapperResultSetExtractor.
     *
     * @param rowMapper the RowMapper which creates an object for each row
     */
    public RowMapperWithMapExtractor(RowMapper<T> rowMapper, String name) {
        this(rowMapper, 0, name);
    }

    /**
     * Create a new RowMapperResultSetExtractor.
     *
     * @param rowMapper    the RowMapper which creates an object for each row
     * @param rowsExpected the number of expected rows
     *                     (just used for optimized collection handling)
     */
    public RowMapperWithMapExtractor(RowMapper<T> rowMapper, int rowsExpected, String name) {
        Assert.notNull(rowMapper, "RowMapper is required");
        this.rowMapper = rowMapper;
        this.rowsExpected = rowsExpected;
        this.name = name;
    }


    @Override
    public Map<?, T> extractData(ResultSet rs) throws SQLException {
        Map<? super Object, T> results = (this.rowsExpected > 0 ? new LinkedHashMap<Object, T>(this.rowsExpected) : new LinkedHashMap<Object, T>());
        int rowNum = 0;
        if (rowMapper instanceof EntityPropertyRowMapper) {
            EntityPropertyRowMapper entityRowMapper = (EntityPropertyRowMapper<T>) rowMapper;
            FieldMapper fieldMapper;
            // name empty def use pk
            if (StringUtils.isEmpty(name)) {
                fieldMapper = entityRowMapper.getEntityMapper().getPrimaryKeyFieldMapper();
            } else {
                fieldMapper = entityRowMapper.getEntityMapper().getFieldMapperByProperty(name);
            }
            if (fieldMapper == null) {
                throw new IllegalArgumentException("class [" + entityRowMapper.getMappedClass() + "] MapKey name Cannot resolve field: " + name);
            }

            while (rs.next()) {
                T entity = this.rowMapper.mapRow(rs, rowNum++);
                Object nameKey = rs.getObject(fieldMapper.getDbFieldName());
                results.put(nameKey, entity);
            }
            return results;
        } else {
            throw new IllegalArgumentException("current rowMapper [" + rowMapper.getClass().getName() + "] must use [" + EntityPropertyRowMapper.class.getName() + "]");
        }


    }


}
