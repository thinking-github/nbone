/*
 * <p>Title: 方天服务平台系统</p>
 * <p>Description: 方天服务平台系统</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 江苏方天电力技术有限公司</p>
 */
package org.nbone.util.security;

import java.io.UnsupportedEncodingException;

/**
 * 提供常用十六进制转换处理的类。
 */
public class HexUtil {

	/**
	 * 转换 byte[] 为双十六进制字符串
	 * @param bytes 字节数组
	 * @return 转换后的字符串
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		if (bytes != null) {
			for (int i = 0; i < bytes.length; i++) {
				sb.append(byteToHex(bytes[i]));
			}
		}
		return sb.toString();
	}

	/**
	 * 转换字符串为十六进制字串（用于采用文本传输）
	 * @param str 待转换的字符串
	 * @return 转换后的字符串 
	 */
	public static String stringToHex(String str) {
		String hex = "";
		try {
			hex = bytesToHex(str.getBytes("GB2312"));
		} catch (UnsupportedEncodingException e) {
			// 此异常不需抛出
			e.printStackTrace();
		}
		return hex;
	}

	/**
	 * 转换 byte 为双十六进制字符
	 * @param b 字节
	 * @return 转换后的字符串 
	 */
	private static String byteToHex(byte b) {
		char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = digit[(b >>> 4) & 0X0F];
		ob[1] = digit[b & 0X0F];
		return new String(ob);
	}

	/**
	 * 转换双十六进制字符串为 byte[]
	 * @param hex 十六进制字符串
	 * @return 转换后的字节数组
	 */
	public static byte[] hexToBytes(String hex) {
		int length = hex.length() / 2;
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = hexToByte(hex.substring(2 * i, 2 * i + 2));
		}
		return bytes;
	}

	/**
	 * 转换双十六进制字母为字符串
	 *
	 * @param hex String
	 * @return String
	 */
	public static String hexToString(String hex) {
		String str = "";
		try {
			str = new String(hexToBytes(hex), "GB2312");
		} catch (Exception ex) {
			// 此异常不需抛出
			ex.printStackTrace();
		}
		return str;
	}

	/**
	 * 转换双十六进制字母为 byte
	 * 
	 * @param hex String
	 * @return byte
	 */
	private static byte hexToByte(String hex) {
		byte high = Byte.parseByte(hex.substring(0, 1), 16);
		byte low = Byte.parseByte(hex.substring(1), 16);
		byte result = (byte) ((high << 4) | low);
		return result;
	}
}
