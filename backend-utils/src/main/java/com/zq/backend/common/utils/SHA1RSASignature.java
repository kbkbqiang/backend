package com.zq.backend.common.utils;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 
 * @ClassName: SHA1RSASignature 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:47:56
 */
public class SHA1RSASignature {

	public static byte[] compute(String privateKey, byte[] data) {
		privateKey = privateKey.replaceAll("\r\n", "");
		privateKey = privateKey.replaceAll("\n", "");
		privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
		privateKey = privateKey.replace("-----END PRIVATE KEY-----", "");
		// byte[] key = Base64.getMimeDecoder().decode(privateKey);
		byte[] key = null;

		PKCS8EncodedKeySpec kspec = new PKCS8EncodedKeySpec(key);
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PrivateKey privKey = kf.generatePrivate(kspec);

			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(privKey);

			signature.update(data);

			return signature.sign();
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean verify(String publicKey, byte[] sign, byte[] data) {

		publicKey = publicKey.replaceAll("\r\n", "\n");
		publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----\n", "");
		publicKey = publicKey.replace("\n-----END PUBLIC KEY-----", "");
		// byte[] key = Base64.getMimeDecoder().decode(publicKey);
		byte[] key = null;

		X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PublicKey pubKey = kf.generatePublic(spec);

			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(pubKey);

			signature.update(data);

			return signature.verify(sign);
		} catch (Exception e) {
			return false;
		}
	}
}
