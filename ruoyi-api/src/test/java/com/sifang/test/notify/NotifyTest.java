package com.sifang.test.notify;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotifyTest {
	
	public static String POST_URL = "http://localhost:9082/sifang/notify/pay/PoduslChannelMethodImpl";
	
	public static String POST_DATA = "amount=10.00&tradeNo=11006363&payTime=2019-06-09+12%3A21%3A10&sign=207975EC64060EE35818208E86D641A6&version=1.5.1&orderTime=2019-06-09+12%3A16%3A48&extendParam=sifang&accountNo=780200001&outTradeNo=201906091216230001&signType=MD5&currency=CNY&payStatus=1&returnType=back_notify";
	
	public static void main(String[] args) throws IOException {
		
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
		out.write(POST_DATA.getBytes());
		//out.write(post_data.getBytes());
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

}
