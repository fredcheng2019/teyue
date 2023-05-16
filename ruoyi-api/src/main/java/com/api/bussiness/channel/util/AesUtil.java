package com.api.bussiness.channel.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author 18319
 *
 */
public class AesUtil {

	private static Logger log = LoggerFactory.getLogger(AesUtil.class);

	public static String encrypt(String input, String key) {
		byte[] crypted = (byte[]) null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(1, skey);
			crypted = cipher.doFinal(input.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("加密错误", e);
		}
		return new String(parseByte2HexStr(crypted));
	}

	public static String decrypt(String hexStr, String key) {
		byte[] output = (byte[]) null;
		try {
			byte[] decByte = parseHexStr2Byte(hexStr);
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(2, skey);
			output = cipher.doFinal(decByte);
		} catch (Exception e) {
			log.error("解密错误", e);
			return null;
		}
		try {
			return new String(output, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("解密错误", e);
		}
		return null;
	}

	public static String parseByte2HexStr(byte[] buf) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = ((byte) (high * 16 + low));
		}
		return result;
	}
}
