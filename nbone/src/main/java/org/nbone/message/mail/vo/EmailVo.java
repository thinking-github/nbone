package org.nbone.message.mail.vo;

import java.util.List;
import java.util.Set;


/**
 * 发送邮件参数实体
 * @author Thinking 2015-08-01
 *               
 *               <p>
 *               <br>
 *               实体使用说明：<br>
 *               mailServerVo （必填）邮件服务器信息、发送者信息<br>
 *               toAddressList:（必填）接收号码列表<br>
 *  			 subject：（必填）邮件主题<br>
 * 				 content：（必填）邮件内容<br>
 *               其他参数为选填项。。
 *
 */
public class EmailVo {
	
    /**
     * 邮件服务器和发送者基本信息
     * @see MailServerVo
     */
	private MailServerVo mailServerVo;
	/**
	 * 邮件接收者地址列表
	 */
	private List<String> toAddressList;
	/**
	 * 邮件抄送地址列表
	 */
	private List<String> ccAddressList;
	/**
	 *  邮件密送地址列表
	 */
	private List<String> bccAddressList;
	
	/**
	 * 邮件主题
	 */
	private  String subject;
	
	/**
	 * 邮件正文
	 */
	private  String content;
	/**
	 * 邮件的附件列表
	 */
	private Set<String> attachFiles;
	
	

	public EmailVo(List<String> toAddressList, String subject, String content) {
		this.toAddressList = toAddressList;
		this.subject = subject;
		this.content = content;
	}

	public EmailVo(String subject, String content) {
		this(null, subject, content);
	}
	
	public EmailVo() {
		
	}

	public MailServerVo getMailServerVo() {
		return mailServerVo;
	}

	public void setMailServerVo(MailServerVo mailServerVo) {
		this.mailServerVo = mailServerVo;
	}

	public List<String> getToAddressList() {
		return toAddressList;
	}

	public void setToAddressList(List<String> toAddressList) {
		this.toAddressList = toAddressList;
	}

	public List<String> getCcAddressList() {
		return ccAddressList;
	}

	public void setCcAddressList(List<String> ccAddressList) {
		this.ccAddressList = ccAddressList;
	}

	public List<String> getBccAddressList() {
		return bccAddressList;
	}

	public void setBccAddressList(List<String> bccAddressList) {
		this.bccAddressList = bccAddressList;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<String> getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(Set<String> attachFiles) {
		this.attachFiles = attachFiles;
	}


}
