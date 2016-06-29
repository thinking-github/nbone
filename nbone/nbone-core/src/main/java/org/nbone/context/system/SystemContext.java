package org.nbone.context.system;


import javax.servlet.FilterConfig;

import org.nbone.persistence.JdbcConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 

/**
 * 用于系统全局参数的上下文
 * 
 * @version v1.1 2010-06-19
 * @author xun
 * @author Thinking  2014-6-19
 * 
 */
public class SystemContext {
	
	//*************************************************分页 begin***************************************//
	/**
	 * 分页参数，offset是表示页码
	 */
	private static ThreadLocal<Integer> offset = new ThreadLocal<Integer>();
	
	/**
	 * 分页参数，offset是表示页数
	 */
	private static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();
	/**
	 * 一页显示的最大值
	 */
	public static final  int PAGE_SIZE_MAX = 100;
	/**
	 * 获取页码
	 * 
	 * @return 
	 */
	public static int getOffset() {
		Integer os =  offset.get();
		if (os == null) {
			return 1;
		}
		return os;
	}

	/**
	 * 设置页码
	 * 
	 * @param offsetvalue  当前页码
	 *           
	 */
	public static void setOffset(int offsetvalue) {
		offset.set(offsetvalue);
	}
	
	/**
	 * 清空offset参数对象
	 */

	public static void removeOffset() {
		offset.remove();
	}
	
	/**
	 * @return 获取页的大小
	 */
	public static int getPageSize() {
		Integer ps =  pageSize.get();
		if (ps == null) {
			return PAGE_SIZE_MAX;
		}
		return ps;
	}

	/**
	 * 设置页数
	 * 
	 * @param pageSizevalue
	 */
	public static void setPageSize(int pageSizevalue) {
		pageSize.set(pageSizevalue);
	}

	/**
	 * 清空pagesize
	 */
	public static void removePageSize() {
		pageSize.remove();
	}
	
	//*************************************************分页 end****************************************//
	
	
	
	//******************************************Spring ApplicationContext begin**************************************//
	/**
	 * spring appicationContext  
	 */
	private static ApplicationContext appicationContext = null;
	/**
	 * @param beanName
	 *            spring中申明的bean名称 <br/>bean为*Service.*的时候,表示
	 * @return 返回对象类
	 */
	public static Object getBean(String beanName) {
		if (getAppicationContext() == null) {
			// 这里可以用于JUnit白盒测试
			ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "spring/applicationContext*.xml" });
			setAppicationContext(appContext);
			getAppicationContext().getBean(beanName);
			return null;
		}
		return getAppicationContext().getBean(beanName);
	}

	/**
	 * 设置连接对象类
	 * @param appicationContext
	 */
	public static void setAppicationContext(ApplicationContext appicationContext) {
		SystemContext.appicationContext = appicationContext;
	}

	/**
	 * 获取连接对象类
	 * @return
	 */
	public static ApplicationContext getAppicationContext() {
		return appicationContext;
	}
	//******************************************Spring ApplicationContext end*******************************************//
	
	
	/**
	 * 异常页面URL变量
	 */
	public static String errorUrl = "errorurl";
	/**
	 * 异常编号变量
	 */
	public static final String errorCode = "errorcode";
	/**
	 * 异常信息变量
	 */
	public static final String errorMessage = "errormessage";

	public static final String defaultErrorCode = "9999";
	
	/**
	 * 日志显示级别
	 */
	public static String logLevel = "info";
	/**
	 * 系统的根目录
	 */
	public static String rootPath = null;

	//******************************************database info begin*******************************************//
	
	/**
	 * 当前系统使用的数据库。默认是oracle
	 */
	public static String CurrentUse_DB_TYPE = JdbcConstants.ORACLE;
	
	/**
	 * 数据库insert记录数
	 */
	public static int countInsert;

	/**
	 * jdbc属性地址
	 */
	public static String paramfile = "/WEB-INF/classes/jdbc.properties";
	/**
	 * 主键的默认值
	 */
	public static Integer seq_num = 0;
	
	//******************************************database info end*******************************************//

	/**
	 * 默认文本编辑框
	 */
	public static final String PGEditType_Text = "text";
	/**
	 * 数字编辑框
	 */
	public static final String PGEditType_numberbox = "numberbox";
	/**
	 * 时间编辑框
	 */
	public static final String PGEditType_datebox = "datebox";
	/**
	 * 下拉选择框
	 */
	public static final String PGEditType_validatebox = "validatebox";

	/**
	 * 单选框
	 */
	public static final String PGEditType_checkbox = "checkbox";

	/**
	 * 系统的上下文confing对象
	 */

	public static FilterConfig filterConfig;
	
	public static String Js_File_path=null;
	public static String Jsp_File_path=null;
	public static String css_File_path=null;


}
