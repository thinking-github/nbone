package org.nbone.component.logger.config;

import java.util.Map;
import java.util.Properties;

import org.nbone.component.logger.LoggerConstants;
import org.nbone.component.logger.LoggerContext;
import org.nbone.component.logger.LoggerStatic;
import org.nbone.component.logger.util.BizcLogPropsUtil;
import org.nbone.component.logger.util.LogPropsUtils;
import org.nbone.component.logger.util.LoggerUtils;
import org.nbone.util.json.JSONOperUtils;

import net.sf.json.JSONObject;


/**
 * 
 * @author Thinking  2014-8-9
 *
 */
public class LoggerConfiguration implements LoggerStatic {
     private Properties props;
     private LoggerContext context  = LoggerContext.getCurrentLoggerContext();
     
	/**
	 * 自定义的操作类型-枚举型(String)
	 */
	private String loggerOperationType;
	
	/**
	 * 初始化Logger模块参数变量
	 */
	public void init() {
		
		 String operationType = getInitConfigParameter(LoggerConstants.LOGGER_OPERATION_TYPE, DEFAULT_LOGGER_OPERATION_TYPE);
	     setLoggerOperationType(operationType);
	     context.put(LoggerConstants.LOGGER_OPERATION_TYPE,formatOperationType(operationType));
	     
	     String isLogOut = getInitConfigParameter(LoggerConstants.LOGGER_IS_LOG_OUT, DEFAULT_LOGGER_IS_LOG_OUT);
	     context.put(LoggerConstants.LOGGER_IS_LOG_OUT,isLogOut);
	     
	     
	     
		
	}
    /**
     * 将日志操作类型(枚举)转化成Map的键值对
     * @param loggerOperationType
     */
	public Map formatOperationType(String loggerOperationType) {
	 String json =  LoggerUtils.getStandardJSONObject(loggerOperationType);	
	 return   JSONObject.fromObject(json);
	}
	/**
	 * 获取初始化参数(如果自定义参数,将覆盖默认的配置参数)
	 * @param key
	 * @param defValue
	 * @return
	 */
	private String getInitConfigParameter(String key,String defValue){
		String value = LogPropsUtils.getString(key,defValue);
		String newValue = BizcLogPropsUtil.getString(key);
		
		if(newValue == null){
			newValue = value;
		} 
		return newValue;
	}
	

	public String getLoggerOperationType() {
		return loggerOperationType;
	}


	public void setLoggerOperationType(String loggerOperationType) {
		this.loggerOperationType = loggerOperationType;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
