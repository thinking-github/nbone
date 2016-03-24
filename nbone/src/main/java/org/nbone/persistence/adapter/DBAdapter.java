package org.nbone.persistence.adapter;

import java.sql.*;

import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class DBAdapter {

	public DBAdapter() {
	}

	public static String getDbName() {
		return dbName;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public void init()
    {
        Connection con = null;
        try
        {
            String internalDbName = "";
            con = DataSourceUtils.getConnection(ds);
            if(con != null)
            {
                DatabaseMetaData dbmd = con.getMetaData();
                if(dbmd != null)
                {
                    internalDbName = dbmd.getDatabaseProductName();
                    if(internalDbName != null && internalDbName.startsWith("Oracle"))
                        internalDbName = "oracle";
                    else
                    if(internalDbName != null && internalDbName.startsWith("DB2/"))
                        internalDbName = "db2";
                    else
                    if(internalDbName != null && internalDbName.startsWith("Adaptive"))
                        internalDbName = "sybase";
                    else
                    if(internalDbName != null && internalDbName.startsWith("Microsoft"))
                    {
                        String dbVersion = dbmd.getDatabaseProductVersion();
                        if(dbVersion.startsWith("9"))
                            internalDbName = "ms2005-sql";
                        else
                        if(dbVersion.startsWith("10"))
                            internalDbName = "ms2008-sql";
                        else
                            internalDbName = "ms-sql";
                    } else
                    if(internalDbName != null && internalDbName.startsWith("HSQL"))
                    {
                        internalDbName = "hsql";
                    } else
                    {
                        String message = "\u4E0D\u652F\u6301\u8BE5\u6570\u636E\u5E93\u7C7B\u578B";
                        log.error(message);
                        throw new SQLException(message);
                    }
                } else
                {
                    log.warn("\u65E0\u6CD5\u83B7\u53D6\u5143\u6570\u636E");
                }
            }
            dbName = internalDbName;
        }
        catch(SQLException e)
        {
            String message = "\u6570\u636E\u5E93\u8FDE\u63A5\u51FA\u73B0\u5F02\u5E38";
            log.error(message);
        }
        if(con != null){
        	  DataSourceUtils.releaseConnection(con, ds);
        }
          
    }

	private static final Log log = LogFactory.getLog(DBAdapter.class);
	private static String dbName = "";
	private DataSource ds;
	
  public static void main(String[] args) {
	DBAdapter.getDbName();
}

}
