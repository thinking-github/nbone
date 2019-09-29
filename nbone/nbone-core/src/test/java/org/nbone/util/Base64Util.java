package org.nbone.util;

import com.google.common.io.BaseEncoding;
import com.sun.mail.util.BASE64EncoderStream;
import org.nbone.util.security.AbstractASCII;
import org.springframework.util.Base64Utils;


/**
 * @author Thinking  2014-04-26
 * @see BASE64EncoderStream
 * @see com.sun.mail.util.BASE64DecoderStream
 * @see BaseEncoding
 * @see org.springframework.util.Base64Utils
 */

public class Base64Util extends AbstractASCII {

    public static void main(String[] args) {
        String input_USER_NAME = "isc21";//此处输入数据库用户名
        String input_pwd = "isc21";//此处输入数据库密码
        byte[] name = Base64Utils.encode(input_USER_NAME.getBytes());
        byte[] pwd = Base64Utils.encode(input_pwd.getBytes());
        byte[] test = BASE64EncoderStream.encode(input_USER_NAME.getBytes());
        System.out.println("BASE64EncoderStream:=#" + new String(test));
        System.out.println("BASE64Util:=#" + new String(name));


        System.out.println("加密后的密码=" + Base64Utils.encodeToString(name));
        System.out.println("加密后的密码=" + Base64Utils.encodeToString(pwd));


        System.out.println("--------------BaseEncoding.base64()-------------------");
        String demo1 = BaseEncoding.base64().encode(input_USER_NAME.getBytes());
        System.out.println(demo1);
        System.out.println(BaseEncoding.base64().encode(demo1.getBytes()));

    }
}
