package com.sifang.test.channel.wmf;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.api.base.util.SignUtil;

public class WithDrawTest {
	
		public static final String key = "kjdskflhsdfkalhfasdf";
		
		//代付接口
		public static final String POST_URL = "http://localhost:9082/sifang";
		//public static String post_data = "version=1.0&customerid=10000116&txmoney=8.00&bankcode=BOC&sn=201905222028330001&provice=广东省&city=深圳市&branchname=中国银行广州新宝利大厦支行&accountname=揭光迪&paychannel=2&cardno=6235757000003454288&tradeTime=20192822082834&paytype=bank&sign=db418a7cae59e3def6270fdb580c8d35";
		

		public static void readContentFromPost() throws Exception {
			long start = System.currentTimeMillis();
			// Post请求的url，与get不同的是不需要带参数
			URL postUrl = new URL(POST_URL);
			// 打开连接
			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
	        //产生随机数
			//out.write((streamToBytes(post_data,key)));
			String post_data = "version=1.0&customerid=10000116&txmoney=8.00&bankcode=BOC&sn=201905222028330001&provice="+gbEncoding("广东省")+"&city="+gbEncoding("深圳市")+"&branchname="+gbEncoding("中国银行广州新宝利大厦支行")+"&accountname="+gbEncoding("揭光迪")+"&paychannel=2&cardno=6235757000003454288&tradeTime=20192822082834&paytype=bank&sign=db418a7cae59e3def6270fdb580c8d35";
			out.write(post_data.getBytes());
			out.flush();
			out.close(); // flush and close
			connection.getInputStream();
			long end = System.currentTimeMillis();
			System.out.println("运行时间：" + (end - start) + "毫秒");// 应该是end - start
			// 获取响应头信息
			// 开始获取数据
			BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			System.out.println("结果：" + bos.toString("utf-8"));
			connection.disconnect();
		}

		/** */
		/**
		 * @param args
		 * @throws Exception
		 */
		public static void main(String[] args) throws Exception {
			
			readContentFromPost();
			
		}

		private static byte[] streamToBytes(String str,String key) throws IOException, IllegalAccessException {
			String sign = SignUtil.getSignReq(str, key);
			System.out.println("获得的sign："+sign);
			String result = str + "&sign="+sign;
			System.out.println("请求报文："+result);
			return result.getBytes();
		}
		
		public static String gbEncoding(final String gbString) {
	        char[] utfBytes = gbString.toCharArray();
	        String unicodeBytes = "";
	        for (int i = 0; i < utfBytes.length; i++) {
	            String hexB = Integer.toHexString(utfBytes[i]);
	            if (hexB.length() <= 2) {
	                hexB = "00" + hexB;
	            }
	            unicodeBytes = unicodeBytes + "\\u" + hexB;
	        }
	        return unicodeBytes;
	    }
	    /*
	     * unicode编码转中文
	     */
	    public static String decodeUnicode(final String dataStr) {
	        int start = 0;
	        int end = 0;
	        final StringBuffer buffer = new StringBuffer();
	        while (start > -1) {
	            end = dataStr.indexOf("\\u", start + 2);
	            String charStr = "";
	            if (end == -1) {
	                charStr = dataStr.substring(start + 2, dataStr.length());
	            } else {
	                charStr = dataStr.substring(start + 2, end);
	            }
	            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
	            buffer.append(new Character(letter).toString());
	            start = end;
	        }
	        return buffer.toString();
	    }

}
