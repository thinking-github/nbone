package org.nbone.framework.spring.dao.namedparam;

import org.nbone.framework.spring.data.domain.PageImpl;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.SqlBuilder;
import org.nbone.persistence.SqlConfig;
import org.nbone.persistence.enums.JdbcFrameWork;
import org.nbone.persistence.exception.BuilderSQLException;
import org.nbone.persistence.model.SqlModel;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author thinking
 * @version 1.0
 * @since spring 2.0
 */
@SuppressWarnings("unused")
public class NamedJdbcTemplate extends NamedParameterJdbcTemplate {

    private JdbcOperations jdbcTemplate;
    private SqlBuilder sqlBuilder;

    public NamedJdbcTemplate(DataSource dataSource, SqlBuilder sqlBuilder) {
        super(dataSource);
        this.jdbcTemplate = this.getJdbcOperations();
        this.sqlBuilder = sqlBuilder;

        if (sqlBuilder == null) {
            this.sqlBuilder = new BaseSqlBuilder(JdbcFrameWork.SPRING_JDBC) {

            };
        }
    }

    public NamedJdbcTemplate(JdbcOperations classicJdbcTemplate, SqlBuilder sqlBuilder) {
        super(classicJdbcTemplate);
        this.jdbcTemplate = this.getJdbcOperations();
        this.sqlBuilder = sqlBuilder;

        if (sqlBuilder == null) {
            this.sqlBuilder = new BaseSqlBuilder(JdbcFrameWork.SPRING_JDBC) {

            };
        }

    }

    public <T> void checkSqlModel(SqlModel<T> sqlModel) {
        if (sqlModel == null) {
            throw new BuilderSQLException("build param sqlModel is null.");
        }
    }

    public <T> List<T> query(SqlModel sqlModel, Object object)
            throws DataAccessException {
        checkSqlModel(sqlModel);
        if (object == null) {
            return jdbcTemplate.query(sqlModel.getSql(), sqlModel.getRowMapper());
        }
        return query(sqlModel.getSql(), getSqlParameterSource(object), sqlModel.getRowMapper());
    }

    public <T> List<T> query(SqlModel sqlModel, SqlParameterSource paramSource)
            throws DataAccessException {
        checkSqlModel(sqlModel);
        if (paramSource == null) {
            return jdbcTemplate.query(sqlModel.getSql(), sqlModel.getRowMapper());
        }
        return query(sqlModel.getSql(), paramSource, sqlModel.getRowMapper());
    }

    public <T> T query(SqlModel sqlModel, Object entity, ResultSetExtractor<T> rse)
            throws DataAccessException {
        checkSqlModel(sqlModel);
        if (entity == null) {
            return jdbcTemplate.query(sqlModel.getSql(), rse);
        }
        SqlParameterSource paramSource = getSqlParameterSource(entity);
        return query(sqlModel.getSql(), paramSource, rse);
    }

    public <T> List<T> query(SqlModel sqlModel, Object entity, RowMapper<T> rowMapper)
            throws DataAccessException {
        checkSqlModel(sqlModel);
        if (entity == null) {
            return jdbcTemplate.query(sqlModel.getSql(), rowMapper);
        }
        SqlParameterSource paramSource = getSqlParameterSource(entity);
        return query(sqlModel.getSql(), paramSource, rowMapper);
    }

    public <T> SqlParameterSource getSqlParameterSource(Object parameter) {
        SqlParameterSource paramSource;
        if (parameter == null) {
            return null;
        }
        if (parameter instanceof Map) {
            Map<String, ?> paramMap = (Map<String, ?>) parameter;
            paramSource = new MapSqlParameterSource(paramMap);
        } else {
            paramSource = new BeanPropertySqlParameterSource(parameter);
        }
        return paramSource;
    }

    public <T> SqlParameterSource getSqlParameterSource(SqlModel<T> sqlModel) {
        T parameter = sqlModel.getParameter();
        return getSqlParameterSource(parameter);
    }

    /**
     * 单表数据分页
     *
     * @param object
     * @param sqlConfig java字段名称 可为空,为空返回全部字段
     * @param pageNum
     * @param pageSize
     * @return
     */
    public <T> Page<T> getForPage(Object object, SqlConfig sqlConfig, int pageNum, int pageSize) {
        SqlModel<Object> sqlModel = sqlBuilder.selectSql(object, sqlConfig);
        return processPage(sqlModel, pageNum, pageSize);
    }

    /**
     * 单表数据分页
     *
     * @param columnMap
     * @param sqlConfig java字段名称 可为空,为空返回全部字段
     * @param pageNum
     * @param pageSize
     * @return
     */
    public <T> Page<T> getForPage(Map<String, ?> columnMap, SqlConfig sqlConfig, int pageNum, int pageSize) {
        SqlModel<Map<String, ?>> sqlModel = (SqlModel<Map<String, ?>>) sqlBuilder.selectSql(columnMap, sqlConfig);
        return processPage(sqlModel, pageNum, pageSize);
    }

    /**
     * 单表数据分页
     *
     * @param object
     * @param pageNum
     * @param pageSize
     * @return
     */
    public <T> Page<T> queryForPage(Object object, SqlConfig sqlConfig, int pageNum, int pageSize) {
        SqlModel<Object> sqlModel = sqlBuilder.selectSql(object, sqlConfig);
        return processPage(sqlModel, pageNum, pageSize);
    }

    /**
     * 单表数据分页 （支持多种组合查询）
     *
     * @param object
     * @param pageNum
     * @param pageSize
     * @return
     */
    public <T> Page<T> findForPage(Object object, int pageNum, int pageSize, SqlConfig sqlConfig) {
        SqlModel<Map<String, ?>> sqlModel = sqlBuilder.objectModeSelectSql(object, sqlConfig);
        return processPage(sqlModel, pageNum, pageSize);
    }

    public long queryForLong(String sql, SqlParameterSource paramSource) throws DataAccessException {
        Number number = queryForObject(sql, paramSource, Long.class);
        return (number != null ? number.longValue() : 0);
    }

    public long queryForLong(String sql, Map<String, ?> paramMap) throws DataAccessException {
        Number number = queryForObject(sql, paramMap, Long.class);
        return (number != null ? number.longValue() : 0);
    }


    /**
     * @param object    参数实体对象，不为空的作为参数查询
     * @param sqlConfig sqlConfig对象， 可为空
     * @param limit     限制返回数量
     * @param <T>
     * @return
     */
    public <T> List<T> listLimit(Object object, SqlConfig sqlConfig, int limit) {

        SqlModel<Object> sqlModel = sqlBuilder.selectSql(object, sqlConfig);
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(object);
        return queryList(sqlModel, paramSource, 1, limit);
    }

    public <T> List<T> listLimit(Object object, SqlConfig sqlConfig, long offset, int limit) {
        SqlModel<Object> sqlModel = sqlBuilder.selectSql(object, sqlConfig);
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(object);
        return queryList(sqlModel, paramSource, offset, limit);
    }

    //parameterMap
    @SuppressWarnings("unchecked")
    protected <T> Page<T> processPage(SqlModel<?> sqlModel, int pageNum, int pageSize) {
        SqlParameterSource paramSource = getSqlParameterSource(sqlModel);
        String countSql = sqlModel.getCountSql();
        long count = this.queryForLong(countSql, paramSource);
        List<T> rows = Collections.emptyList();
        if (count > 0) {
            rows = queryList(sqlModel, paramSource, pageNum, pageSize);
        }
        //zero-based page index.
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize);
        Page<T> page = new PageImpl<T>(rows, pageRequest, count);
        return page;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryList(SqlModel<?> sqlModel, SqlParameterSource paramSource, int pageNum, int pageSize) {
        checkSqlModel(sqlModel);

        if (pageNum <= 0) {
            pageNum = 1;
        }

        if (pageSize <= 0) {
            pageSize = 10;
        }

        String pageSql = sqlModel.getPageSql(pageNum, pageSize);

        RowMapper<T> rowMapper = (RowMapper<T>) sqlModel.getRowMapper();

        List<T> rows = jdbcTemplate.query(getPreparedStatementCreator(pageSql, paramSource), rowMapper);
        return rows;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryList(SqlModel<?> sqlModel, SqlParameterSource paramSource, long offset, int pageSize) {
        checkSqlModel(sqlModel);
        if (pageSize <= 0) {
            pageSize = 10;
        }

        String pageSql = sqlModel.getOffsetPageSql(offset, pageSize);

        RowMapper<T> rowMapper = (RowMapper<T>) sqlModel.getRowMapper();

        List<T> rows = jdbcTemplate.query(getPreparedStatementCreator(pageSql, paramSource), rowMapper);
        return rows;
    }

}
