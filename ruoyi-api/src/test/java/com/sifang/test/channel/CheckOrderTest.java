package com.sifang.test.channel;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.api.base.util.MD5;

public class CheckOrderTest {

	public static final String key = "b92d73dc0587c4d7bf8d639f76fc4cecb3c6820d";
	

	//交易接口
	public static final String POST_URL = "http://wmpay.wshangfu.com/apiorderquery";
	public static String post_data = "customerid=10000116&sdorderno=201905191034120001&reqtime=201905191034120001&sign=";
	public static String sign_data = "customerid=10000116&sdorderno=201905191034120001&reqtime=201905191034120001&b92d73dc0587c4d7bf8d639f76fc4cecb3c6820d";
	
	

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
		
		String sign = MD5.MD5Encode(sign_data).toLowerCase();
		//String sign = DigestUtils.md5Hex(sign_data);;
		System.out.println("得到的sign:"+sign);
		String data = post_data+sign;
		System.out.println("请求报文:"+data);
		out.write(data.getBytes());
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
