package org.nbone.persistence.db;

import java.sql.*;

import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
/**
 * 
 * @author uap
 * @author thinking
 * @version 1.0 
 */
public class DBAdapter {

	private static final Log log = LogFactory.getLog(DBAdapter.class);

	  public static String getDbName(DataSource ds)
	  {
	    Connection con = null;
	    String internalDbName = "";
	    try
	    {
	      con = DataSourceUtils.getConnection(ds);
	      if (con != null)
	      {
	        DatabaseMetaData dbmd = con.getMetaData();
	        if (dbmd != null) {
	          internalDbName = dbmd.getDatabaseProductName();
	          if ((internalDbName != null) && (internalDbName.startsWith("Oracle"))) {
	            internalDbName = "oracle";
	          } else if ((internalDbName != null) && (internalDbName.startsWith("PostgreSQL"))) {
	            internalDbName = "postgre";
	          } else if ((internalDbName != null) && (internalDbName.startsWith("MySQL"))) {
	            internalDbName = "mysql";
	          } else if ((internalDbName != null) && (internalDbName.startsWith("DB2/"))) {
	            internalDbName = "db2";
	          } else if ((internalDbName != null) && (internalDbName.startsWith("Adaptive"))) {
	            internalDbName = "sybase";
	          } else if ((internalDbName != null) && (internalDbName.startsWith("KingbaseES"))) {
	            internalDbName = "kingbase";
	          } else if ((internalDbName != null) && (internalDbName.startsWith("DM DBMS"))) {
	            internalDbName = "dm";
	          } else if ((internalDbName != null) && (internalDbName.startsWith("Microsoft"))) {
	            String dbVersion = dbmd.getDatabaseProductVersion();
	            if (dbVersion.startsWith("9"))
	              internalDbName = "ms2005-sql";
	            else if (dbVersion.startsWith("10"))
	              internalDbName = "ms2008-sql";
	            else
	              internalDbName = "ms-sql";
	          }
	          else if ((internalDbName != null) && (internalDbName.startsWith("HSQL"))) {
	            internalDbName = "hsql";
	          } else {
	            String message = "不支持该数据库类型";
	            log.error(message);
	            throw new PersistenceException(message);
	          }
	        }
	        else {
	          log.warn("无法获取元数据");
	        }
	      }
	    } catch (SQLException e) {
	      String message = "数据库连接出现异常";
	      log.error(message);
	      throw new PersistenceException(message);
	    } finally {
	      if (con != null) {
	        DataSourceUtils.releaseConnection(con, ds);
	      }
	    }
	    return internalDbName;
	  }
}
