package org.nbone.message.sms.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.message.IUseCallback;
import org.nbone.message.sms.vo.SmsVo;

public class SmsUseCallback implements IUseCallback<SmsVo,Object>{
	private static final Log logger = LogFactory.getLog(MasServiceImpl.class);
	private static final String TIP = "短信发送-";
	
	@Override
	public boolean preHandle(SmsVo vo)  {
		SmsVo smsVo = vo;
		if(smsVo == null ){
			logger.error(new StringBuilder().append(TIP).append("bean SmsVo must is not null .thinking"));
			return false;
		}
		
		List<String> phoneList  = smsVo.getUserNumberList();
		String content  =  smsVo.getSmsContent();
		int phoneSize;
		if(phoneList == null || (phoneSize = phoneList.size()) <= 0){
			logger.error(new StringBuilder().append(TIP).append("短信接收用户列表为空,请输入短信接收用户."));
			return false;
		}
		
		if(StringUtils.isEmpty(content)){
			logger.error(new StringBuilder().append(TIP).append("短信内容为空,请输入短信内容."));	
			return false;
		}
		
		return true;
	}

	@Override
	public SmsVo postHandle(SmsVo vo,Object result) {
		return vo;
	}



}
