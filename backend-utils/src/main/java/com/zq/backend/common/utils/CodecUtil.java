package com.zq.backend.common.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * @ClassName: CodecUtil
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午11:44:35
 */
public class CodecUtil {
	private static String algorithmName = "md5";
	private static int hashIterations = 2;

	/*
	 * public static String md5(String content) { try { MessageDigest md =
	 * MessageDigest.getInstance("MD5"); md.update(content.getBytes()); byte[]
	 * digest = md.digest(); return Hex.encodeHexString(digest); } catch
	 * (NoSuchAlgorithmException e) { return null; } }
	 */

	public static String md5(String content) {
		return DigestUtils.md5Hex(content);
	}

	/*
	 * public static String ramdomHexString(int count) { return
	 * Hex.encodeHexString(RandomStringUtils.randomNumeric(count).getBytes()); }
	 * 
	 * public static String ramdomHexString() { return ramdomHexString(32); }
	 * 
	 * public static String ramdomNumericString(int count) { return
	 * RandomStringUtils.randomNumeric(count); }
	 * 
	 * public static String ramdomNumericString() { return ramdomHexString(32);
	 * }
	 */

	public static String genHexRandomToken() {
		char[] allowed = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		StringBuilder result = new StringBuilder();
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			for (int i = 0; i < 32; i++) {
				result.append(allowed[Math.abs(random.nextInt()) % allowed.length]);
			}
			return result.toString();
		} catch (NoSuchAlgorithmException e) {
			SecureRandom sr = new SecureRandom();
			byte[] rndBytes = new byte[16];
			sr.nextBytes(rndBytes);
			return Hex.encodeHexString(rndBytes);
		}
	}

//	public static String encryptPassword(String password, String salt) {
//		return new SimpleHash(algorithmName, password, ByteSource.Util.bytes(salt), hashIterations).toHex();
//	}
}
