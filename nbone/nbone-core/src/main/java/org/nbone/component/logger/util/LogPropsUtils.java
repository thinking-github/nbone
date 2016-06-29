package org.nbone.component.logger.util;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.util.PropertiesUtil;



/**
 * 此类使用范围:用于此日志模块的全局变量（配置文件全局参数）
 * @author Thinking  2014-8-9
 *
 */
public class LogPropsUtils {
	
	private static final Log logger = LogFactory.getLog(LoggerUtils.class);
	
	public static String location = "com/sgcc/hst/logger/Logger.properties";
	private static Properties properties;
	private static boolean hasLoad = false;

	private static Properties loadProperties() {
		properties = PropertiesUtil.loadProperties(location);
		hasLoad = true;
		return properties;
	}
	
	public static String getString(String key) {
		if(!hasLoad){
			loadProperties();
		}
		return properties.getProperty(key);
	}
	public static String getString(String key,String defValue) {
		String val = getString(key);
		if(val == null){
			val = defValue;
		}
		return val;
	}

	

}
