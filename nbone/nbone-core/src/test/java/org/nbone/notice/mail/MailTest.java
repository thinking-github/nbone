package org.nbone.notice.mail;

import org.nbone.message.mail.exception.MailException;
import org.nbone.message.mail.service.impl.MailServiceImpl;
import org.nbone.message.mail.vo.EmailVo;
import org.nbone.message.mail.vo.MailServerVo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MailTest {

    public static EmailVo testin() {

        String from = "test@163.com";
        MailServerVo mailServerVo = new MailServerVo("host", 25);
        mailServerVo.setFromAddress(from);
        mailServerVo.setUserName(from);
        mailServerVo.setPassword("test");
        mailServerVo.setValidate(true);
        List<String> to = new ArrayList<String>();
        to.add(from);
        EmailVo emailVo = new EmailVo(to, "test", "test");
        //邮件服务器信息
        emailVo.setMailServerVo(mailServerVo);

        return emailVo;
    }

    public static EmailVo testnet() {

        String from = "18655166563@163.com";
        MailServerVo mailServerVo = new MailServerVo("smtp.163.com", 25);
        mailServerVo.setFromAddress(from);
        mailServerVo.setUserName(from);
        mailServerVo.setPassword("");
        mailServerVo.setValidate(true);
        List<String> to = new ArrayList<String>();
        to.add(from);
        to.add("12345@qq.com");
        List<String> cc = new ArrayList<String>();
        cc.add("12345@qq.com");
        cc.add("12345@qq.com");
        List<String> bcc = new ArrayList<String>();
        bcc.add("12345@qq.com");
        EmailVo emailVo = new EmailVo(to, "subject-", "i am content ");
        //emailVo.setCcAddressList(cc);
        //emailVo.setBccAddressList(bcc);
        //邮件服务器信息
        emailVo.setMailServerVo(mailServerVo);


        String fileName1 = "I:\\.sql";
        String fileName2 = "I:\\test.sql";
        Set<String> files = new HashSet<String>();
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
        System.out.println("000000000000000" + MailTest.class);


    }

}
