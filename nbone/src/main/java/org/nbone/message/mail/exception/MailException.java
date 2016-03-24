package org.nbone.message.mail.exception;

public class MailException extends Exception {
	
	
	public final static String MAIL_SERVER_NOT_CONFIG =" please conifg mail server info [MailServerVo].";

	public MailException() {
		super();
	}
 
	public MailException(String message) {
		super(message);
	}
	
	public MailException(Throwable cause) {
		 super(cause);
	}
	
	public MailException(String message, Throwable cause) {
		 super(message, cause);
	}
	

}
