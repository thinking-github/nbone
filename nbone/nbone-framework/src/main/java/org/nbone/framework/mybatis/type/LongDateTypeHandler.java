package org.nbone.framework.mybatis.type;

import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.*;

/**
 * 将字符串格式的时间:<code>1457142290175</code>转换为JDBC能够识别的类型。</br>
 * <p>
 * 1.Long timestamp  to Timestamp <br>
 * 2.Timestamp to Long Timestamp <br>
 *
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
@Alias("LongDateTypeHandler")
@MappedJdbcTypes({JdbcType.TIMESTAMP, JdbcType.DATE, JdbcType.TIME})
public class LongDateTypeHandler extends BaseTypeHandler<Long> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType)
            throws SQLException {
        if (jdbcType == null) {
            ps.setTimestamp(i, new Timestamp(parameter));
        } else {
            switch (jdbcType) {
                case TIMESTAMP:
                    ps.setTimestamp(i, new Timestamp(parameter));
                    break;

                case DATE:
                    ps.setDate(i, new Date(parameter));
                    break;

                case TIME:
                    ps.setTime(i, new Time(parameter));
                    break;
                case BIGINT:
                    ps.setLong(i, parameter);
                    break;

                default:
                    ps.setObject(i, parameter, jdbcType.TYPE_CODE);
                    break;
            }
            //ps.setObject(i, parameter, jdbcType.TYPE_CODE);
        }

    }

    @Override
    public Long getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return timestamp.getTime();
    }

    @Override
    public Long getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp timestamp = cs.getTimestamp(columnIndex);
        return timestamp.getTime();
    }

    @Override
    public Long getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnIndex);
        return timestamp.getTime();
    }

}
