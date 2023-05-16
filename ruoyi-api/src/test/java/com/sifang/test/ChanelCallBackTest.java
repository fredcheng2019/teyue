package com.sifang.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.api.base.util.SignUtil;

public class ChanelCallBackTest {

	    //public static final String key = "2b8b43d1a0a24172a0d16278d8307f0b";105031616015正式
		public static final String key = "kjdskflhsdfkalhfasdf";
		
		public static final String PORTAL_NAME = "place_an_order";
		
		
		//回调接口
		//支付渠道通知
		//public static final String POST_URL = "http://localhost:9082/PaymentPlatformCloud/callback/pay/TestImpl";
		//public static String post_data = "channel_code=jieguangdi&mchCode=201905161500300001&chCode=15572822062180001&fee=20000&cusValue=hello";
		
		//代付渠道通知
		public static final String POST_URL = "http://localhost:9082/PaymentPlatformCloud/callback/withdraw/TestImpl";
		public static String post_data = "channel_code=jieguangdi&mchCode=201905161612160001&chCode=15572822062180001&fee=1000&cusValue=hello";

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
			connection.setRequestProperty("Content-Type", "application/text");

			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			
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
}
