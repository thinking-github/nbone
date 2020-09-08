package org.nbone.framework.mybatis.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.*;
import org.nbone.framework.spring.support.ComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 将字符串格式的Json转换为Java对象 和 java 对象转化成string 类型。</br>
 *
 * <p>
 * spring boot config
 * <p>
 *
 * <pre>
 *   mybatis.type-aliases-package=org.nbone.framework.mybatis.type
 *   mybatis.type-handlers-package=org.nbone.framework.mybatis.type
 * </pre>
 * </p>
 *
 * @author thinking
 * @since 2015-12-12
 */
@SuppressWarnings("unused")
@Alias("JsonTypeHandler")
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.LONGVARCHAR})
@MappedTypes({Map.class, List.class, Object.class})
public class JsonTypeHandler extends BaseTypeHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(JsonTypeHandler.class);

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
            throws SQLException {
        if (objectMapper == null) {
            objectMapper = ComponentFactory.getBean(ObjectMapper.class);
        }
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("query column name: " + columnName);
        }
        String json = rs.getString(columnName);
        return readValue(json);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("query column index: " + columnIndex);
        }

        String json = cs.getString(columnIndex);
        return readValue(json);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("query column index: " + columnIndex);
        }
        String json = rs.getString(columnIndex);
        return readValue(json);
    }

    private Object readValue(String json) {
        if (StringUtils.hasLength(json)) {
            if (objectMapper == null) {
                objectMapper = ComponentFactory.getBean(ObjectMapper.class);
            }
            try {
                return objectMapper.readValue(json, Object.class);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return json;
    }

}
