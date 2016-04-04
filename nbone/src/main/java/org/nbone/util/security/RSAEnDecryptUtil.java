/*
 * <p>Title: 方天服务平台系统</p>
 * <p>Description: 方天服务平台系统</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 江苏方天电力技术有限公司</p>
 */
package org.nbone.util.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA非对称加密算法实现类
 */
public class RSAEnDecryptUtil {

	/**
	 * 使用RSA公钥加密数据
	 * 
	 * @param pubKeyInByte 打包的byte[]形式公钥
	 * @param data 要加密的数据
	 * @return 加密数据
	 * @throws Exception  异常处理
	 */
	public static byte[] rsaEncrypt(byte[] pubKeyInByte, byte[] data)
			throws Exception {
		KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyInByte);
		PublicKey pubKey = mykeyFactory.generatePublic(pubSpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		return cipher.doFinal(data);
	}

	/**
	 * 用RSA私钥解密
	 * 
	 * @param privKeyInByte 私钥打包成byte[]形式
	 * @param data 要解密的数据
	 * @return 解密数据
	 * @throws Exception 异常处理
	 */
	public static byte[] rsaDecrypt(byte[] privKeyInByte, byte[] data)
			throws Exception {
		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKeyInByte);
		KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privKey = mykeyFactory.generatePrivate(privSpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privKey);
		return cipher.doFinal(data);
	}

	/**
	 * 用一个已打包成byte[]形式的私钥加密数据，即数字签名
	 * 
	 * @param keyInByte 打包成byte[]的私钥
	 * @param source 要签名的数据，一般应是数字摘要
	 * @return 签名 byte[]
	 * @throws Exception 异常处理
	 */
	public static byte[] sign(byte[] keyInByte, byte[] source) throws Exception {
		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(keyInByte);
		KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privKey = mykeyFactory.generatePrivate(privSpec);
		Signature sig = Signature.getInstance("SHA1withRSA");
		sig.initSign(privKey);
		sig.update(source);
		return sig.sign();
	}

	/**
	 * 验证数字签名
	 * 
	 * @param keyInByte 打包成byte[]形式的公钥
	 * @param source 原文的数字摘要
	 * @param sign 签名（对原文的数字摘要的签名）
	 * @return 是否证实 boolean
	 * @throws Exception 异常处理
	 */
	public static boolean verify(byte[] keyInByte, byte[] source, byte[] sign)
			throws Exception {
		KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
		Signature sig = Signature.getInstance("SHA1withRSA");
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(keyInByte);
		PublicKey pubKey = mykeyFactory.generatePublic(pubSpec);
		sig.initVerify(pubKey);
		sig.update(source);
		return sig.verify(sign);
	}

	/**
	 * 建立新的密钥对，返回打包的byte[]形式私钥和公钥
	 * 
	 * @return 包含打包成byte[]形式的私钥和公钥的object[],其中，object[0]为私钥byte[],object[1]为工要byte[]
	 * @throws Exception 处理
	 */
	public static Object[] giveRSAKeyPairInByte() throws Exception {
		KeyPair newKeyPair = creatmyKey();
		if (newKeyPair == null){
		    return null;
		}
		Object[] re = new Object[2];
		if (newKeyPair != null) {
			PrivateKey priv = newKeyPair.getPrivate();
			byte[] bpriv = priv.getEncoded();
			PublicKey pub = newKeyPair.getPublic();
			byte[] bpub = pub.getEncoded();
			re[0] = bpriv;
			re[1] = bpub;
			return re;
		}
		return new Object[0];
	}

	/**
	 * 新建密钥对
	 * 
	 * @return KeyPair对象
	 * @throws Exception 处理
	 */
	public static KeyPair creatmyKey() throws Exception {
		long mySeed = System.currentTimeMillis();
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		random.setSeed(mySeed);
		keyGen.initialize(1024, random);
		return keyGen.generateKeyPair();
	}
}
