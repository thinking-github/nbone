/*
 * <p>Title: 方天服务平台系统</p>
 * <p>Description: 方天服务平台系统</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 江苏方天电力技术有限公司</p>
 */
package org.nbone.persistence.util;

/**
 * 数据库工具类
 *
 */
public class DatabaseUtil {
	/**
	 * 数据库类型db2
	 */
	public static final String DATABASE_TYPE_DB2="db2";
	/**
	 * 数据库类型oracle
	 */
	public static final String DATABASE_TYPE_ORACLE="oracle";
	/**
	 * db2数据库连接驱动
	 */
	private static final String DATABASE_DRIVER_DB2="com.ibm.db2.jcc.DB2Driver";
	/**
	 * oracle数据库连接驱动
	 */
	private static final String DATABASE_DRIVER_ORACLE="oracle.jdbc.driver.OracleDriver";
	/**
	 * mysql数据库连接驱动
	 */
	private static final String DATABASE_DRIVER_MYSQL="com.mysql.jdbc.Driver";
	/**
	 * 当前驱动
	 */
	private static String currentDriver="";

	

}
