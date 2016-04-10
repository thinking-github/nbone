package org.nbone.notice.mail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nbone.message.mail.exception.MailException;
import org.nbone.message.mail.service.impl.MailServiceImpl;
import org.nbone.message.mail.vo.EmailVo;
import org.nbone.message.mail.vo.MailServerVo;



public class MailTest {
	
	public static  EmailVo  testin() {
		
		String from = "test.163.com         # replace with 'examplePass' instead";
		MailServerVo mailServerVo  = new MailServerVo("10.1.101.40",25);
		mailServerVo.setFromAddress(from);
		mailServerVo.setUserName(from);
		mailServerVo.setPassword("test             # replace with the empty string");
		mailServerVo.setValidate(true);
		List<String> to = new ArrayList<String>();
		to.add(from);
		EmailVo emailVo = new EmailVo(to,"开会","开会TEST");
		//邮件服务器信息
		emailVo.setMailServerVo(mailServerVo);
		
		return emailVo;
	}
	public static EmailVo  testnet() {
		
		String from = "18655166563@163.com";
		MailServerVo mailServerVo  = new MailServerVo("smtp.163.com",25);
		mailServerVo.setFromAddress(from);
		mailServerVo.setUserName(from);
		mailServerVo.setPassword("12345      # replace with the empty string");
		mailServerVo.setValidate(true);
		List<String> to = new ArrayList<String>();
		to.add(from);
		to.add("945077285@qq.com");
		List<String> cc = new ArrayList<String>();
		cc.add("945077285@qq.com");
		cc.add("945077285@qq.com");
		List<String> bcc = new ArrayList<String>();
		bcc.add("945077285@qq.com");
		EmailVo emailVo = new EmailVo(to,"开会","开会mmTEST");
		//emailVo.setCcAddressList(cc);
		//emailVo.setBccAddressList(bcc);
		//邮件服务器信息
		emailVo.setMailServerVo(mailServerVo);
		
		
		String fileName1 = "I:\\sguap-grants.sql";
		String fileName2 = "I:\\HST_HST_HST_HST_KKKKKKKKKKKK_oooooooooooooooooooooo_授.sql";
		Set<String> files =  new HashSet<String>();
		files.add(fileName1);
		files.add(fileName2);
		
		emailVo.setAttachFiles(files);
		
		return emailVo;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MailServiceImpl hstUse = new MailServiceImpl();
		try {
			
			hstUse.sendMail(MailTest.testnet());
			//hstUse.sendHtmlMail(MailTest.testnet());
		} catch (MailException e) {
			e.printStackTrace();
		}
		System.out.println("000000000000000"+MailTest.class);
		
		

	}

}
