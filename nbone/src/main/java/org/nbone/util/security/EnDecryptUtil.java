/*
 * <p>Title: 方天服务平台系统</p>
 * <p>Description: 方天服务平台系统</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 江苏方天电力技术有限公司</p>
 */
package org.nbone.util.security;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 *	提供MD5、SHA、BASE64、DES加解密算法 
 *
 */
public class EnDecryptUtil {

	/** 十六进制*/
    private static final int HEX_NUMBER = 16;
    
    /** Byte补码*/
    private static final int BYTE_MAX = 256;
    
    /** 编码*/
	private static final  String ENCODING = "UTF-8";
	
	/** base 64 */
	private static final BASE64Encoder BASE64ENCODER = new BASE64Encoder();
	
	/** base 64 */
	private static final BASE64Decoder BASE64DECODER = new BASE64Decoder();

    
    /**
     * MD5加密算法
     * 
     * @param source 明文
     * @return MD5密文
     */
    public static String md5Encrypt(String source) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// 明确指定MD5算法，不可能出现该异常
			e.printStackTrace();
			return "";
		}
		md5.update(source.getBytes());
		byte[] md5Bytes = md5.digest();

		StringBuilder result = new StringBuilder();
		int i;
		for (byte md5Byte : md5Bytes) {
			i = md5Byte;
			if (i < 0) {
				i += BYTE_MAX;
			}
			result.append(i < HEX_NUMBER ? "0" + Integer.toHexString(i) : Integer
					.toHexString(i));
		}
		return result.toString();
	}
    
    /**
	 * sha加密算法
	 * 
	 * @param source 明文
	 * @return sha密文
	 */
    public static String shaEncrypt(String source) {
        MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			// 明确指定MD5算法，不可能出现该异常
			e.printStackTrace();
			return "";
		}
        sha.update(source.getBytes());
        byte[] sha1Bytes = sha.digest();
        StringBuilder result = new StringBuilder();
        int i;
        for (byte sha1Byte : sha1Bytes) {
            i = sha1Byte;
            if (i < 0) {
                i += BYTE_MAX;
            }
            result.append(i < HEX_NUMBER ? "0" + Integer.toHexString(i) : Integer
                    .toHexString(i));
        }
        return result.toString();
    }
        
	/**
	 * 加字符串
	 * @param str 加密字符串
	 * @return 加密后的字符串
	 */
	public static String getEncString(String str) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = symmetricEncrypto(str.getBytes(ENCODING));
				result = BASE64ENCODER.encode(encodeByte);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 解密字符串
	 * @param str 解密字符串
	 * @return 解密后的字符串
	 */
	public static String getDesString(String str) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = BASE64DECODER.decodeBuffer(str);
				byte[] decoder = EnDecryptUtil.symmetricDecrypto(encodeByte);
				result = new String(decoder, ENCODING);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 对称加密方法
	 * @param byteSource 需要加密的数据
	 * @return 经过加密的数据
	 * @throws Exception 异常
	 */
	public static byte[] symmetricEncrypto(byte[] byteSource) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int mode = Cipher.ENCRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			byte[] keyData = { 1, 9, 8, 2, 0, 8, 2, 1 };
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			baos.close();
		}
	}

	/**
	 * 对称解密方法
	 * @param byteSource 需要解密的数据
	 * @return 经过解密的数据
	 * @throws Exception 异常
	 */
	public static byte[] symmetricDecrypto(byte[] byteSource) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int mode = Cipher.DECRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			byte[] keyData = { 1, 9, 8, 2, 0, 8, 2, 1 };
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			baos.close();
		}
	}

	/**
	 * 散列算法
	 * @param byteSource 需要散列计算的数据
	 * @return 经过散列计算的数据
	 * @throws Exception 异常
	 */
	public static byte[] hashMethod(byte[] byteSource) throws Exception {
		try {
			MessageDigest currentAlgorithm = MessageDigest.getInstance("SHA-1");
			currentAlgorithm.reset();
			currentAlgorithm.update(byteSource);
			return currentAlgorithm.digest();
		} catch (Exception e) {
			throw e;
		}
	}    
	
	/**
	 * 
	 * @param args 字符串
	 */
	public static void main(String[] args) {
		String a = "1";
		for (byte i : a.getBytes()) {
			int j = i;
			System.out.println(Integer.toBinaryString(j));
			System.out.println(i);
		}
		
		
		
	}
	
}
