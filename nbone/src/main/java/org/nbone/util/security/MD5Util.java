/*
 * <p>Title: 统一服务平台</p>
 * <p>Description: 统一服务平台</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: 江苏方天电力技术有限公司</p>
 */
package org.nbone.util.security;

import java.security.MessageDigest;
/**
 * MD5加密工具类
 *
 * @author gaoLi
 *
 */
public class MD5Util {

	/**
	 * MD5加密 32位
	 * @param inStr String
	 * @return 加密后的字符串
	 */
	public static String md5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++){
		    byteArray[i] = (byte) charArray[i];
		}

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = (md5Bytes[i]) & 0xff;
			if (val < 16){
			    hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

	// 测试主函数
//	public static void main(String args[]) {
//		String s = new String("frontier");
//		System.out.println("原始：" + s);
//		System.out.println("MD5后：" + MD5(s));
//	}

}
