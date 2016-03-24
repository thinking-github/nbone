package org.nbone.message.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.util.CheckStrUtils;

/**
 * 
 * @author Thinking  2013-09-30
 * 验证notice 
 *
 */
public class CheckUtils {
	public static Log logger = LogFactory.getLog(CheckUtils.class);
	
	public static final int MOBILE_PHONE = 0;
	public static final int EMAIL = 1;
	
	public static  List<String> filter(List<String> list,int filterType) {
		if(list == null ){
			
			return null;
		}
		List<String> usableList = new ArrayList<String>();
		List<String> unusableList = new ArrayList<String>();
		String msg  = null;
		
		switch (filterType) {
		case MOBILE_PHONE:
			msg = "手机号码不正确. ";
			for (String string : list) {
				if (CheckStrUtils.isHandset(string)) {
					usableList.add(string);
				}else{
					unusableList.add(string);
				}
				
			}
			
			break;
		case EMAIL:
			msg = "电子邮件不正确. ";	
			for (String string : list) {
				if (CheckStrUtils.isEmail(string)) {
					usableList.add(string);
				}else{
					unusableList.add(string);
				}
				
			}
					
			break;
		

		default:
			break;
		}
		int oldsize = list.size();
		int unusableCount =  unusableList.size();
		if(unusableCount > 0){
			logger.warn(new StringBuilder(msg).append(unusableCount).append("/").append(oldsize).append(unusableList));
		}
		
		return usableList;
	}
	
	public static  List<String> filterEmail(List<String> list) {
		return filter(list, EMAIL);
	}
	
	public static  List<String> filterMobilePhone(List<String> list) {
		return filter(list, MOBILE_PHONE);
	}
	
	

}
