package org.nbone.demo.jodd;

import jodd.mail.Email;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;

public class MailDemo {
	
	public static void main(String[] args) {
		
		String from = "test@163.com";
		String user  = from;
		Email email1 = Email.create()
		        .from(from)
		        .to("test@qq.com")
		        .subject("Hello!")
		        .addText("A plain text message...");
		
		
		SmtpServer smtpServer = SmtpServer.create("smtp.163.com",25).authenticateWith(user, "test");
    
	    SendMailSession session = smtpServer.createSession();
	    session.open();
	    session.sendMail(email1);
	    session.close();
		
	}

}
