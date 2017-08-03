package org.nbone.component.logger.util;

import org.nbone.util.PropertiesUtil;



/**
 * 此类使用范围:用于业务系统的配置文件参数
 * @author Thinking  2014-8-9
 *
 */
public class BizcLogPropsUtil {
	
	public static String location = "com/sgcc/hst/logger/hstaopLogger.properties";
	
	public static String getString(String key) {
		String val = PropertiesUtil.getString(key, location);
		return val;
	}

}
