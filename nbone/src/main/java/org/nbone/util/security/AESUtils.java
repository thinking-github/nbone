package org.nbone.util.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * <b>概述</b>：<br>
 * AES加解密的工具类
 * <p>
 * <b>功能</b>：<br>
 * 提供AES加解密的工具方法
 * 
 * @author thinking 2012-8-27
 * @since 1.0
 */
public class AESUtils {

	public static String keySeed = "c32ad1415f6c89fee76d8457c31efb4b";

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param keyseed
	 *            密钥
	 * @return
	 */
	public static String encrypt(String content, String keyseed)
			throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		// 解决linux环境下密码解密问题
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(keyseed.getBytes());
		kgen.init(128, secureRandom);
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");// 创建密码器
		byte[] byteContent = content.getBytes("utf-8");
		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(byteContent);
		return byte2hex(result); // 加密
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String keyseed)
			throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		// 解决linux环境下密码解密问题
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(keyseed.getBytes());
		kgen.init(128, secureRandom);
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(hex2byte(content));
		return result; // 加密
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static String decrypt(String content, String keyseed)
			throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		// 解决linux环境下密码解密问题
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(keyseed.getBytes());
		kgen.init(128, secureRandom);
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
		String result = new String(cipher.doFinal(hex2byte(content
				.getBytes("UTF-8"))));
		return result.trim();
	}

	public static String encryptForJS(String content, String keyseed)
			throws Exception {
		SecretKeySpec key = getKeySpecFromBytes(keyseed.toUpperCase());
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] byteEnc = cipher.doFinal(content.getBytes("UTF-8"));
		return byte2hex(byteEnc);
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static String decryptForJS(String content, String keyseed)
			throws Exception {
		SecretKeySpec key = getKeySpecFromBytes(keyseed.toUpperCase());
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		String result = new String(cipher.doFinal(hex2byte(content
				.getBytes("UTF-8"))));
		return result.trim();

	}

	// 字节到十六进制字符串转换
	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs += ("0" + stmp);
			else
				hs += stmp;
		}
		return hs.toUpperCase();
	}

	// 十六进制字符串到字节转换
	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数!");

		byte[] b2 = new byte[b.length / 2];

		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	// 从十六进制字符串生成Key
	private static SecretKeySpec getKeySpecFromBytes(String strBytes)
			throws NoSuchAlgorithmException {
		SecretKeySpec spec = new SecretKeySpec(hex2byte(strBytes.getBytes()),
				"AES");
		return spec;
	}

	public static void main(String[] args) throws Exception {

		String md5 = Md5encyptUtil.getEncryptedPwd("novell");
		System.out.println(md5);
		String aes = AESUtils.encrypt(md5, AESUtils.keySeed);
		System.out.println(aes);
		String apString = AESUtils.decrypt(aes, AESUtils.keySeed);
		System.out.println(apString);

	}
}
