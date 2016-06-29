package org.nbone.component.logger;




/**
 * 日志模块全局常量
 * @author Thinking
 * @version 2014-08-08
 *
 */
public interface LoggerStatic {
	
	//******************************Logger模块功能**************************************
	/**
	 * 日志模块标识(用于后缀)
	 */
	public final static String ID_SUFFIX = ".ID";
	public final static String ID = ID_SUFFIX.substring(1);
	
	/**
	 * 日志信息描述(用于后缀)
	 */
	public final static String DESC_SUFFIX = ".DESC";
	public final static String DESC = DESC_SUFFIX.substring(1);
	
	/**
	 * 操作类型(用于后缀)
	 */
	public final static String OPERATION_TYPE_SUFFIX = ".OPERATION_TYPE";
	public final static String OPERATION_TYPE = OPERATION_TYPE_SUFFIX.substring(1);
	
	//*****************************Logger模块默认值*************************************
	
	/** Default placeholder prefix: {@value} */
	public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

	/** Default placeholder suffix: {@value} */
	public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
	/**
	 * 默认的操作类型-枚举型(String) 0:QUERY,1:CREATE,2:DELETE,3:UPDATE
	 */
	public static  String DEFAULT_LOGGER_OPERATION_TYPE = "0:QUERY,1:CREATE,2:DELETE,3:UPDATE";
	
	public static  String DEFAULT_LOGGER_IS_LOG_OUT = "false";
	
	//********************************Logger模块其他************************************
	
	/**
	 * JSON Object 前缀
	 */
	public static final String JSON_OBJECT_PREFIX = "{";
	
	/**
	 * JSON Object 后缀
	 */
	public static final String JSON_OBJECT_SUFFIX = "}";
	

	
	

}
