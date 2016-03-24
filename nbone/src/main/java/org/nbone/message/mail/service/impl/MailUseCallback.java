package org.nbone.message.mail.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.message.IUseCallback;
import org.nbone.message.mail.vo.EmailVo;

public class MailUseCallback implements IUseCallback<EmailVo,Object> {
	private static final Log logger = LogFactory.getLog(MailUseCallback.class);
	private static final String TIP =  "邮件发送-";
	@Override
	public boolean preHandle(EmailVo emailVo) {
		
		 boolean result = false;
			if(emailVo == null ){
				logger.error(new StringBuilder().append(TIP).append("bean emailVo must is not null.").append(EmailVo.class));
				return result;
			}
			List<String> users = emailVo.getToAddressList();
			String subject  = emailVo.getSubject();
			String content  = emailVo.getContent();
			int size;
			if(users == null || (size = users.size()) <= 0 ){
				logger.error(new StringBuilder().append(TIP).append("邮件接收用户列表为空,请输入邮件接收用户."));
				return result;
			}
			if(StringUtils.isEmpty(subject)){
				logger.error(new StringBuilder().append(TIP).append("邮件主题为空,请输入邮件主题."));
				return result;
			}
			if(StringUtils.isEmpty(content)){
				logger.error(new StringBuilder().append(TIP).append("邮件内容为空,请输入邮件内容."));
				return result;
			}
		
		return true;
	}

	@Override
	public EmailVo postHandle(EmailVo emailVo,Object result) {
		return emailVo;
	}

}
