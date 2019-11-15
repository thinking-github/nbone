package org.nbone.framework.spring.dao.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 返回单个实体结果集
 *
 * <li> 1.当返回<=0条数据时返回null
 * <li> 2.当返回=1条数据时直接返回
 * <li> 3.当返回>1数据时只取第一条
 *
 * @author thinking
 * @version 1.0
 * @since 2019-10-10
 */
public class SingleEntityResultSetExtractor<T> implements ResultSetExtractor<T> {

    private static final Logger logger = LoggerFactory.getLogger(ResultSetExtractor.class);

    private final RowMapper<T> rowMapper;

    public SingleEntityResultSetExtractor(RowMapper<T> rowMapper) {
        Assert.notNull(rowMapper, "RowMapper is required");
        this.rowMapper = rowMapper;
    }

    @Override
    public T extractData(ResultSet rs) throws SQLException, DataAccessException {
        T result = null;
        int rowNum = 0;
        if (rs.next()) {
            result = rowMapper.mapRow(rs, rowNum++);
        }
        if (rs.next()) {
            logger.warn("unique query result return multiple lines .thinking");
        }
        return result;
    }
}
