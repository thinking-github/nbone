package org.nbone.message.sms.service;

import java.util.List;

import org.nbone.message.IUseCallback;
import org.nbone.message.sms.exception.SmsException;
import org.nbone.message.sms.vo.SmsVo;

/**
 *功能说明:向短信平台发送短信接口
 *
 *@author Thinking 2013-9-10
 *
 * Copyright (c)2013 thinking-版权所有
 */
public interface IMasService {
	
	/**
	 * 发送短信
	 */
	public void submitSmsReq(SmsVo smsVo) throws SmsException;
	
	/**
	 * 发送短信,且使用含有带回调函数的处理
	 * @param smsVo
	 * @param useCallback
	 * @throws SmsException
	 */
	public void submitSmsReq(SmsVo smsVo,IUseCallback<SmsVo, Object> useCallback) throws SmsException;
	
	/**
	 * 批量发送短信（给不同号码发送不同内容，采用此方法）<br>
	 */
	public void submitSmsReqList(List<SmsVo> listsmsVo)throws SmsException;

}
