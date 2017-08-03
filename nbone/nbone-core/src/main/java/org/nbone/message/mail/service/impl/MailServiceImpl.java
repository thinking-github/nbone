package org.nbone.message.mail.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.message.IUseCallback;
import org.nbone.message.mail.MailAuthenticator;
import org.nbone.message.mail.exception.MailException;
import org.nbone.message.mail.service.IMailService;
import org.nbone.message.mail.vo.EmailVo;
import org.nbone.message.mail.vo.MailServerVo;



/**
 * 发送邮件接口实现 [base by javax.mail]
 *
 *@author thinking 2013-9-17 下午8:01:51  <br>
 *@version  1.0
 * Copyright (c)2013 thinking-版权所有
 */
public class MailServiceImpl implements IMailService {

	private static final Log logger = LogFactory.getLog(MailServiceImpl.class);
	private static final String TIP =  "邮件发送-";
	
	public static final  String  Content_Type_Plain = "plain";
	public static final  String  Content_Type_Html = "html";
	
	public static final  String  Charset_UTF_8 = "UTF-8";
	
	
	public void sendMailServiceList(List<EmailVo> emailVoList) throws MailException{
		try{
			if(emailVoList == null){
				logger.error(new StringBuilder().append(TIP).append("MAIL_传入参数为空"));
				return ;
			}
			for(EmailVo emailVo:emailVoList){
				sendMailService(emailVo);
		    }
		}catch(Exception e){
			logger.error(new StringBuilder().append(TIP).append("MAIL_遍历邮件出现异常"), e);
		}
	}
	
	public boolean sendMailService(EmailVo emailVo) throws MailException {
		return sendMailService(emailVo,new MailUseCallback());
	}
	
	public  boolean sendMailService(EmailVo emailVo,IUseCallback<EmailVo,Object> useCallback) throws MailException {
        boolean isflag = false;
        boolean result = false;
        
		if(useCallback != null){
			isflag = useCallback.preHandle(emailVo);
		}
		
		if(isflag) {
			result = this.sendMail(emailVo);
		}
		
		if(useCallback != null){
			useCallback.postHandle(emailVo, result);
		}
				
	    return result;
	}
	
	/**
	 * 发送邮件方法  [Content-Type: text/plain;]
	 */
	public final boolean sendMail(EmailVo emailVo) throws MailException {
		
		return this.doSendMail(emailVo,null);
	}
	/**
	 * 发送邮件方法  [Content-Type: text/html;]
	 */
	public final boolean sendHtmlMail(EmailVo emailVo) throws MailException {
	
		return this.doSendMail(emailVo, Content_Type_Html);
	}
	
	/**
	 * 发送邮件方法  [Content-Type: text/****?;]
	 * @param emailVo
	 * @return boolean 是否成功
	 * @throws MailException
	 */
	protected boolean doSendMail(EmailVo emailVo,String contentType) throws MailException {
		boolean result = true;
		MimeMessage msg = this.createMessage(emailVo);
		
		String subject = emailVo.getSubject();
		String  content = emailVo.getContent();
		Set<String>  attachFiles =  emailVo.getAttachFiles();
		try {
			if(attachFiles == null || attachFiles.size() == 0){
				
				if(contentType == null || contentType =="" || Content_Type_Plain.equals(contentType)){
					//简化版方法
					msg.setText(content, Charset_UTF_8);
					
				}else if(Content_Type_Html.equals(contentType)){
					//[此方法的简化版]
					msg.setContent(content,"text/html;charset="+Charset_UTF_8);
				}else{
					msg.setText(content);
				}
				
				
			}else{
				
				if(contentType == null || contentType =="" || Content_Type_Plain.equals(contentType)){
				    //邮件正文
					BodyPart bp = new MimeBodyPart();
					bp.setText(content);
					
					Multipart mm = new MimeMultipart();
					mm.addBodyPart(bp);
					
					//邮件附件
					mm = addAttachFile(mm, attachFiles);
					msg.setContent(mm);
					
				}else if(Content_Type_Html.equals(contentType)){
				    BodyPart bp = new MimeBodyPart();
					bp.setContent(content, "text/html;charset="+Charset_UTF_8);
					
					Multipart mm = new MimeMultipart();
					mm.addBodyPart(bp);
					//添加附件
					mm = addAttachFile(mm, attachFiles);
					msg.setContent(mm);
					
					//[此方法的简化版]
					//msg.setContent(content,"text/html;charset="+Charset_UTF_8);
				}else{
					msg.setText(content);
				}
				
			}
			
			msg.setSubject(subject, "UTF-8");
			msg.setSentDate(new Date());
			msg.saveChanges();
			Transport.send(msg);
			
			// 发送信件 [与此方法功能一样:Transport.send(msg)]
			/*Transport transport = session.getTransport("smtp");
			transport.connect(mailServerHost,mailServerPort,userName,password);
			transport.sendMessage(msg,msg.getAllRecipients());
			transport.close();*/
			
		} catch (Exception e) {
			result = false;
			throw new MailException(e);
		}
		return result;
	}

	
	//****************************private********************************************
	private MimeMessage  createMessage(EmailVo emailVo) throws MailException{
		
		MailServerVo mailServerVo = emailVo.getMailServerVo();
		if(mailServerVo == null){
		  throw new MailException(MailException.MAIL_SERVER_NOT_CONFIG);
		}
		
		List<String> toAddressList = emailVo.getToAddressList();
		List<String> ccAddressList = emailVo.getCcAddressList();
		List<String> bccAddressList = emailVo.getBccAddressList();
		
		String fromAddress  = mailServerVo.getFromAddress();
		String mailServerHost  = mailServerVo.getServerHost();
		int    mailServerPort  = mailServerVo.getServerPort();
		String userName = mailServerVo.getUserName();
		String password =mailServerVo.getPassword();
		
		String nick = mailServerVo.getNickname();

		
		Properties props = mailServerVo.getProperties();
		Authenticator authenticator = null ;
		if(mailServerVo.isValidate()){
			authenticator = new MailAuthenticator(mailServerVo.getUserName(),mailServerVo.getPassword());
		}
		
		// 得到默认的对话对象
		Session session = Session.getDefaultInstance(props,authenticator);
		// 创建一个消息，并初始化该消息的各项元素
		MimeMessage msg = new MimeMessage(session);
		try {
		    InternetAddress[] toAddress = this.parse(toAddressList);
		    InternetAddress[] ccAddress = this.parse(ccAddressList);
		    InternetAddress[] bccAddress = this.parse(bccAddressList);
		    
			//msg.setFrom(new InternetAddress(fromAddress));
			msg.setFrom(new InternetAddress(fromAddress, nick));
			msg.setRecipients(Message.RecipientType.TO, toAddress);
			msg.setRecipients(Message.RecipientType.CC, ccAddress);
			msg.setRecipients(Message.RecipientType.BCC, bccAddress);
			
		} catch (Exception e) {
			throw new MailException(e);
		}
		return msg;
	}
	/**
	 * 
	 * 发送邮件时添加附件列表
	 * @param mp
	 * @param attachFiles
	 * @return
	 * @throws MailException
	 * @throws IOException
	 * @throws MessagingException
	 */
	private Multipart  addAttachFile(Multipart mp, Set<String> attachFiles) throws MailException, IOException, MessagingException{
		
		if(mp == null){
			throw new MailException("bean Multipart is null .");
		}
		
		//没有附件时直接返回
		if(attachFiles == null || (attachFiles.size())== 0){
			return mp;
		}
		Iterator<String> iterator = attachFiles.iterator();
		while (iterator.hasNext()) {
			String fileName  = iterator.next();
			MimeBodyPart mbp_file = new MimeBodyPart();
			mbp_file.attachFile(fileName);
			mp.addBodyPart(mbp_file);
			//防止乱码
			String encode = MimeUtility.encodeText(mbp_file.getFileName());
            mbp_file.setFileName(encode); 
			
		}
		
		return mp;
	}
	
	private Multipart  addAttachFile(Set<String> attachFiles) throws MailException, IOException, MessagingException{
		Multipart mp = new MimeMultipart();
		addAttachFile(mp, attachFiles);
		return mp;
	}
	
	private InternetAddress[]  parse(List<String> addressList) throws AddressException{
	  if(addressList == null || addressList.size() == 0){
		return null;	
	  }
	  int size  = addressList.size(); 
	  InternetAddress[] address = new InternetAddress[size];
	  for (int i = 0; i < size; i++) {
		  address[i] = new InternetAddress(addressList.get(i));
	  }
		
		return address;
	}


	
	
}
