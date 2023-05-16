package com.ruoyi.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class SignUtil {
	
	/**
	 * 签名算法
	 *参与签名的拼接报文数据对象
	 * @param key
	 * @return 签名
	 * @throws IllegalAccessException
	 * @throws UnsupportedEncodingException
	 */
	public static String getSignReq(String str, String key) {
		String[] levelOne = str.split("&");
		ArrayList<String> list = new ArrayList<String>();
		for(int i=0;i<levelOne.length;i++) {
			String[] levelTwo = levelOne[i].split("=");
			if(null != levelTwo && levelTwo.length==2) {
				if(!levelTwo[0].equals("sign")) {
					list.add(levelOne[i]+"&");
				}
				
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + key;
		result = MD5.MD5Encode(result).toUpperCase();//MD5
		return result;
	}
}
