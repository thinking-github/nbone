package org.nbone.message.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.message.mail.exception.MailException;
import org.nbone.message.mail.util.EmailUtils;
import org.nbone.message.mail.vo.MailServerVo;
import org.nbone.message.sms.util.SmsUtils;



/**
 * 用于业务系统发送通知（短信通知和邮件通知）
 * @author thinking
 *
 */
public class NoticeUtils {
	public static Log logger = LogFactory.getLog(NoticeUtils.class);
	
	public static void sendSms(String content,Object  phoneList){
		SmsUtils.send(content, phoneList);
		
	}
	
	
	public static void sendEmail(String subject,String content,Object  emailUserList,MailServerVo mailServerVo){
		try {
			EmailUtils.send(subject, content, emailUserList,mailServerVo);
		} catch (MailException e) {
			logger.error("邮件发送失败.", e);
		}
		
	}
	
	

}
