package com.sifang.test.channel;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChCallBackTest {
	
	public static final String POST_URL = "http://localhost:9082/sifang/callback/pay/WMFChannelMethodImpl";
	public static String post_data = "status=1&customerid=10000116&sdpayno=2019052020215197608444&sdorderno=201905202023270001&total_fee=1.00&paytype=bank_qrcode&remark=sifang&sign=bcf3a1149f287e35c0d5c7ab53cbe469&tmoney=1.00";
	
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
		//connection.setRequestProperty("Content-Type", "application/text");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

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

}
