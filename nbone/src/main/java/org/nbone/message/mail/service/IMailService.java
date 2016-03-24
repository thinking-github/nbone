package org.nbone.message.mail.service;

import java.util.List;

import org.nbone.message.IUseCallback;
import org.nbone.message.mail.exception.MailException;
import org.nbone.message.mail.vo.EmailVo;


/**
 *功能说明: 发送邮件接口 <br>
 *
 *@author Thinking 2013-9-17 下午8:01:51 <br>
 *
 *Copyright (c)2013 thinking-版权所有
 */
public interface IMailService {
	
	/**
	 * 邮件发送
	 * @param emailVo 邮件参数实体 {@link EmailVo}
	 */
	public boolean sendMailService(EmailVo emailVo) throws MailException;
	
	/**
	 * 邮件批量发送 （给不同号码发送不同内容，采用此方法）<br>
	 * 创建时间:2013-9-13 上午10:53:16
	 * 
	 * @param emailVoList 前台传入参数
	 */
	public void sendMailServiceList(List<EmailVo> emailVoList) throws MailException;
	
	
	public boolean sendMailService(EmailVo emailVo,IUseCallback<EmailVo,Object> useCallback) throws MailException;
	
	
}
