package org.nbone.message.sms.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.message.sms.exception.SmsException;
import org.nbone.message.sms.service.impl.MasServiceImpl;
import org.nbone.message.sms.vo.SmsVo;
import org.nbone.message.util.CheckUtils;
import org.springframework.util.StringUtils;

/**
 * 
 * 短信发送接口统一入口
 * @author thinking 2013-09-30
 *
 */
public class SmsUtils {
	
	public static Log logger = LogFactory.getLog(SmsUtils.class);
	
	/**
	 * 
	 * @param content 短信内容
	 * @param phoneList 手机号列表 <br> 
	 *                  <li>
	 *                  1.{@link String} (18655166563,18555166563)
	 *                  <li>
	 *                  2.{@link List }
	 *@param isFilter   是否过滤非法号码
	 *                   
	 *               
	 *               
	 */
	@SuppressWarnings("unchecked")
	public static void  send(String content,Object  phoneList,boolean isFilter) {
		
		List<String> mobileList = null;
		if(phoneList instanceof List){
			mobileList = (List<String>) phoneList;
		}
		else if(phoneList instanceof String){
			String[] ss = StringUtils.commaDelimitedListToStringArray((String) phoneList);
			mobileList  = Arrays.asList(ss);
		}
		else{
			throw new IllegalArgumentException("手机号列表参数有误. 原参数："+phoneList);
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("短信内容: >>>>>>>>>>>>>>>>>>"+content);
			logger.debug("手机号列表: >>>>>>>>>>>>>>>>"+phoneList);
		}
		
		List<String> userNumberList = mobileList ;
		if(isFilter){
			List<String> usableMobileList = CheckUtils.filterMobilePhone(mobileList);
			userNumberList = usableMobileList;
		}
		
	
		SmsVo smsVo = new SmsVo(userNumberList,content);
		try {
			new MasServiceImpl().submitSmsReq(smsVo);
		} catch (SmsException e) {
			logger.error("短信发送失败.",e);
		}
	}
	
	/**
	 * 此方法过滤非法号码
	 * @see #send(String, Object, boolean)
	 */
	public static void  send(String content,Object  phoneList){
		send(content, phoneList, true);
	} 
	public static void  asyncSend(final String content,final Object  phoneList) {
		
		Thread thread=new  Thread(){
			@Override
			public void run() {
				send(content, phoneList);
			}
		};
		thread.start();
		
	}
	

}
