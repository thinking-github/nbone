package org.nbone.util.lang;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 
 * @author SmallRaccoon
 *
 */
public class StringUtil {
	private static Logger log = Logger.getLogger(StringUtil.class);
	
	private static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String[] group(String input, String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(input);
		String[] strGrp = null;
		if (matcher.find()) {
			int count = matcher.groupCount();
			strGrp = new String[count];
			for (int i = 1; i <= count; i++) {
				strGrp[i - 1] = matcher.group(i);
			}
		}

		return strGrp;
	}

	public static String getClassSimpleName(Class<?> clazz) {
		String name = clazz.getName();
		if (StringUtils.isEmpty(name))
			return null;
		int lastIndex = name.lastIndexOf(".");
		return name.substring(lastIndex + 1, name.length());
	}

	public static String nvl(String str1, String str2) {
		if (StringUtils.isEmpty(str1))
			return str2;
		return str1;
	}

	public static String toString(String value, String defaultValue) {
		if (value == null)
			return defaultValue;
		return value;
	}

	public static String toStringLP(String value, String defaultValue,
			int size, String delim) {
		if (value == null)
			value = defaultValue;
		if (value == null)
			return null;
		return StringUtils.leftPad(value, size, delim);
	}

	public static String toStringRP(String value, String defaultValue,
			int size, String delim) {
		if (value == null)
			value = defaultValue;
		if (value == null)
			return null;
		return StringUtils.rightPad(value, size, delim);
	}

	public static String maxLength(String str, int maxLength) {
		if (str != null && str.length() > maxLength) {
			return str.substring(0, maxLength);
		}

		return str;
	}

	public static String indexOf(String str, String startPattern,
			String endPattern) {
		int sidx = str.indexOf(startPattern);
		if (sidx == -1)
			return null;
		int eidx = str.indexOf(endPattern, sidx);
		if (eidx == -1)
			return null;
		if (sidx > eidx)
			return null;
		return str.substring(sidx + startPattern.length(), eidx).trim();
	}

	public static String lastIndexOf(String str, String startPattern,
			String endPattern) {
		int sidx = str.lastIndexOf(startPattern);
		if (sidx == -1)
			return null;
		int eidx = str.lastIndexOf(endPattern);
		if (eidx == -1)
			return null;
		if (sidx > eidx)
			return null;
		return str.substring(sidx + startPattern.length(), eidx);
	}

	public static String baIndexOf(String str, String startPattern,
			String endPattern) {
		int sidx = str.indexOf(startPattern);
		if (sidx == -1)
			return null;
		int eidx = str.lastIndexOf(endPattern);
		if (eidx == -1)
			return null;
		if (sidx > eidx)
			return null;
		return str.substring(sidx + startPattern.length(), eidx);
	}

	public static String convertString(String gbk) {
		String utf8 = "";
		try {
			utf8 = new String(gbk2utf8(gbk), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.info(e.getMessage(), e);
		}
		return utf8;
	}

	public static byte[] gbk2utf8(String chenese) {
		char c[] = chenese.toCharArray();
		byte[] fullByte = new byte[3 * c.length];
		for (int i = 0; i < c.length; i++) {
			int m = (int) c[i];
			String word = Integer.toBinaryString(m);

			StringBuffer sb = new StringBuffer();
			int len = 16 - word.length();
			for (int j = 0; j < len; j++) {
				sb.append("0");
			}
			sb.append(word);
			sb.insert(0, "1110");
			sb.insert(8, "10");
			sb.insert(16, "10");
			String s1 = sb.substring(0, 8);
			String s2 = sb.substring(8, 16);
			String s3 = sb.substring(16);
			byte b0 = Integer.valueOf(s1, 2).byteValue();
			byte b1 = Integer.valueOf(s2, 2).byteValue();
			byte b2 = Integer.valueOf(s3, 2).byteValue();
			byte[] bf = new byte[3];
			bf[0] = b0;
			fullByte[i * 3] = bf[0];
			bf[1] = b1;
			fullByte[i * 3 + 1] = bf[1];
			bf[2] = b2;
			fullByte[i * 3 + 2] = bf[2];
		}
		return fullByte;
	}

	public static String subString(String message, int start) {
		return message.substring(start);
	}

	public static String ToSBC(String input) {
		if (input == null) {
			return null;
		}
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			char code = c[i];
			if (code >= 65281 && code <= 65373) {
				c[i] = (char) (c[i] - 65248);
			} else if (code == 12288) {
				c[i] = (char) (c[i] - 12288 + 32);
			}
		}
		String returnString = new String(c);
		return returnString;
	}

	/**
	 * 中文字符转换
	 * 
	 * @param s
	 * @return
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0) {
						k += 256;
					}
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * @description:控制Object[]的各个维不为null
	 */
	public static Object[] replaceNullObj(Object[] v_obj) {
		Object[] obj = v_obj;
		int len = obj.length;
		for (int i = 0; i < len; i++) {
			if (obj[i] == null) {
				obj[i] = "";
			}
		}
		return obj;
	}

	/**
	 * 字符串加密
	 * 
	 * @version V1.0
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * 字符串解密
	 * 
	 * @version V1.0
	 */
	public static String decryptBASE64(String key) throws Exception {
		return new String((new BASE64Decoder()).decodeBuffer(key));
	}
	/**
	 * url加密
	 * @author liuxd
	 * @date 2013-10-29 下午02:59:56
	 * @version V1.0
	 */
	public static String urlEncoder(String value){
	        String code="";
			try {
				code = java.net.URLEncoder.encode(value,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return code;
	}
	/**
	 * url解密
	 * @author liuxd
	 * @date 2013-10-29 下午03:00:08
	 * @version V1.0
	 */
	public static String urlDecoder(String value){
        String code="";
		code = java.net.URLDecoder.decode(value);
		return code;
}
	/**
	 * 字符串MD5加密
	 */
	public static String doMD5Encrypt(String oriMessage) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(oriMessage.getBytes());
			return byteToHexString(b);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * byte转String
	 */
	public static String byteToHexString(byte[] srcbyte) {
		char[] str = new char[32];
		int k = 0;
		for (int i = 0; i < 16; i++) {
			byte byte0 = srcbyte[i];
			str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
			str[(k++)] = hexDigits[(byte0 & 0xF)];
		}
		String s = new String(str);
		return s;
	}

}
