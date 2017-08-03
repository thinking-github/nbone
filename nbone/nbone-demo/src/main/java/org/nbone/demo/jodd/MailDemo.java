package org.nbone.demo.jodd;

import jodd.mail.Email;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;

public class MailDemo {
	
	public static void main(String[] args) {
		
		String from = "18655166563@163.com";
		String user  = from;
		Email email1 = Email.create()
		        .from(from)
		        .to("945077285@qq.com")
		        .subject("Hello!")
		        .addText("A plain text message...");
		
		
		SmtpServer smtpServer = SmtpServer.create("smtp.163.com",25).authenticateWith(user, "12345      # replace with the empty string");
    
	    SendMailSession session = smtpServer.createSession();
	    session.open();
	    session.sendMail(email1);
	    session.close();
		
	}

}
