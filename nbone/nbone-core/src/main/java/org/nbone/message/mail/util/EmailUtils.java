package org.nbone.message.mail.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.message.mail.exception.MailException;
import org.nbone.message.mail.service.IMailService;
import org.nbone.message.mail.service.impl.MailServiceImpl;
import org.nbone.message.mail.vo.EmailVo;
import org.nbone.message.mail.vo.MailServerVo;
import org.nbone.message.util.CheckUtils;
import org.springframework.util.StringUtils;



/**
 * 
 * 邮件发送接口统一入口<br>
 * 使用时请先初始化服务器信息和发送者信息
 * @author Thinking  2014-08-01
 * 
 *
 */
public class EmailUtils {
	
	public static Log logger = LogFactory.getLog(EmailUtils.class);
	
	private static boolean  isInitiate;
	
	private static IMailService mailService=  new MailServiceImpl();
	
	/**
	 * 发送邮件【参数全部属于必填项】
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @param emailUserList 发送邮件用户列表<br>
	 *                     <li>
	 *                     1.{@link String} (lang@163.com,lang9@163.com)
	 *                     <li>
	 *                     2.{@link List }
	 *@param mailServerVo 邮件服务器信息和发送者信息
	 *@param isFilter 是否过滤非法邮件用户
	 */
	@SuppressWarnings("unchecked")
	public static boolean  send(String subject,String content,Object  emailUserList,MailServerVo mailServerVo,boolean isFilter) throws MailException {
		
		if(mailServerVo == null){
			throw new MailException(MailException.MAIL_SERVER_NOT_CONFIG);
		}
		
		List<String> emailList = null;
		if(emailUserList instanceof List){
			emailList = (List<String>) emailUserList;
		}
		else if(emailUserList instanceof String){
			String[] ss = StringUtils.commaDelimitedListToStringArray((String) emailUserList);
			emailList  = Arrays.asList(ss);
		}
		else{
			throw new IllegalArgumentException("邮件用户列表参数有误. 原参数："+emailUserList);
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("邮件内容: >>>>>>>>>>>>>>>>>>"+content);
			logger.debug("邮件用户列表: >>>>>>>>>>>>>>>>"+emailUserList);
		}
		List<String> sendableemailList = emailList;
		if(isFilter){
			List<String> usableEmailList = CheckUtils.filterEmail(emailList);
			sendableemailList = usableEmailList;
		}
		
		EmailVo emailVo = new EmailVo(sendableemailList,subject,content);
		//邮件服务器信息
		emailVo.setMailServerVo(mailServerVo);
		boolean succeed = false ;
		try {
			succeed = mailService.sendMailService(emailVo);
		} catch (MailException e) {
			logger.error("邮件发送失败.", e);
		}
		return succeed;
    }
	/**
	 * 此方法过滤非法邮件用户
	 * @see #send(String, String, Object, MailServerVo, boolean)
	 */
	public static boolean  send(String subject,String content,Object  emailUserList,MailServerVo mailServerVo) throws MailException {
		return send(subject, content, emailUserList, mailServerVo, true);
	}
	
	
	public static void  asyncSend(final String subject,final String content,final Object  emailUserList,final MailServerVo mailServerVo) {
		
		Thread thread= new  Thread(){
			@Override
			public void run() {
				try {
					send(subject, content, emailUserList,mailServerVo);
				} catch (MailException e) {
					logger.error("邮件发送失败.", e);
				}
			}
		};
		thread.start();
		
	}
	
	@Deprecated
	public static void initiateMailServerVo(MailServerVo mailServer) {
	}
	@Deprecated
	public static void reinitiateMailServerVo(MailServerVo mailServer) {
	}



}
