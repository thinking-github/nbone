package org.nbone.framework.mybatis.type;

import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.*;

/**
 * 将字符串格式的日期:<code>yyyy-mm-dd</code>转换为JDBC能够识别的类型。<br>
 * 1.String format Date to Date <br>
 * 2.Date to String format Date <br>
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
@Alias("StringDateTypeHandler")
@MappedJdbcTypes({JdbcType.TIMESTAMP, JdbcType.DATE, JdbcType.TIME})
public class StringDateTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        if (jdbcType == null) {
            Date date = Date.valueOf(parameter.toString());
            ps.setDate(i, date);
        } else {
            switch (jdbcType) {
                case TIMESTAMP:
                    ps.setTimestamp(i, Timestamp.valueOf(parameter));
                    break;

                case DATE:
                    ps.setDate(i, Date.valueOf(parameter));
                    break;

                case TIME:
                    ps.setTime(i, Time.valueOf(parameter));
                    break;
                default:
                    ps.setObject(i, parameter, jdbcType.TYPE_CODE);
                    break;
            }
        }

    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Date date = rs.getDate(columnName);
        return date.toString();
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Date date = cs.getDate(columnIndex);
        return date.toString();
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Date date = rs.getDate(columnIndex);
        return date.toString();
    }

}
