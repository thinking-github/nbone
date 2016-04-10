package org.nbone.message.mail.vo;


import java.io.Serializable;
import java.util.Properties;
/**
 * 邮件服务器和发送者基本信息 
 * @author thinking 2015-08-01
 */
public class MailServerVo implements Serializable {

	private static final long serialVersionUID = -4282209291104902927L;
	
	// 发送邮件的服务器的IP和端口
	private String serverHost;
    /**
     * 发送邮件服务器默认端口
     */
	private int serverPort = 25;
	// 邮件发送者的地址
	private String fromAddress;

	// 登陆邮件发送服务器的用户名和密码
	private String userName;
	private String password;
	// 是否需要身份验证
	private boolean validate = true;
	
	// 别名昵称
	private String nickname;
	
	
	

	public MailServerVo() {
	}

	public MailServerVo(String serverHost, int serverPort) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;
	}

	public MailServerVo(String serverHost, int serverPort, String fromAddress) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.fromAddress = fromAddress;
	}


	/**
	 * 获得邮件会话属性
	 */
	public Properties getProperties() {
		Properties p = new Properties();
		//全局设置主机
		p.put("mail.host", this.serverHost);
		p.put("mail.port", this.serverPort);
		
		p.put("mail.debug","true");
		//p.put("mail.mime.encodefilename","true");
		
		//根据协议设置主机
		p.put("mail.smtp.host", this.serverHost);
		p.put("mail.smtp.port", this.serverPort);
		p.put("mail.smtp.user", this.userName);
		p.put("mail.smtp.password", this.password);
		p.put("mail.smtp.auth", validate ? "true" : "false");
		return p;
	}

	
	
	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}