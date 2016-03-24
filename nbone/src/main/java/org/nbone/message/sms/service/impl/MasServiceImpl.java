package org.nbone.message.sms.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.message.IUseCallback;
import org.nbone.message.sms.exception.SmsException;
import org.nbone.message.sms.service.IMasService;
import org.nbone.message.sms.vo.SmsVo;

/**
 *功能说明:向短信平台发送短信接口实现
 *
 *@author Thinking  2013-9-10 下午8:40:50 <br>
 *
 *Copyright (c)2013 thinking-版权所有
 */
public class MasServiceImpl implements IMasService {
	
	private static final Log log = LogFactory.getLog(MasServiceImpl.class);
	private static final String TIP = "短信发送-";
	
	public void submitSmsReqList(List<SmsVo> listsmsVo) {
		try{
			if(listsmsVo == null){
				log.error(new StringBuilder().append(TIP).append("MAS_传入参数为空"));
				return ;
			}
			for(SmsVo smsVo:listsmsVo){
					submitSmsReq(smsVo);
			}
		}catch(Exception e){
			log.error(new StringBuilder().append(TIP).append("MAIL_遍历短信出现异常"), e);
		}
	}
	
	public void submitSmsReq(SmsVo smsVo) throws SmsException {
		this.submitSmsReq(smsVo,new SmsUseCallback());
	
	}

	public void submitSmsReq(SmsVo smsVo,IUseCallback<SmsVo, Object> useCallback) throws SmsException {
		boolean isflag = false;
		if(useCallback != null) {
			isflag = useCallback.preHandle(smsVo);
		}
		if(isflag){
			//TODO:send SMS();
		}
		
		if(useCallback != null) {
			useCallback.postHandle(smsVo,null);
		}
	}

}