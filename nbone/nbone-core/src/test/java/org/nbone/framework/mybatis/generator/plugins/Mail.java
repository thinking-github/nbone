package org.nbone.framework.mybatis.generator.plugins;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="MESSAGE_MAIL_INFO")
public class Mail implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name="ID",strategy="uuid")
    @GeneratedValue(generator="ID")
	@Column(name="ID")
	String id;
	
	/**
	 * 接收方
	 */
	@Column(name="RECEIVE")
	String receive;
	
	/**
	 * 邮件主题
	 */
	@Column(name="SUBJECT")
	String subject;
	
	/**
	 * 邮件内容
	 */
	@Column(name="CONTENT")
	String content;
	
	/**
	 * 发送状态
	 */
	@Column(name="STATUS")
	int status;
	
	/**
	 * 发送成功时间
	 */
	@Column(name="SEND_TIME")
	Date sendTime;
	
	/**
	 * 创建时间  yyyy-MM-dd HH:mm:ss
	 */
	@Column(name="CREATE_TIME")
	Date createTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
