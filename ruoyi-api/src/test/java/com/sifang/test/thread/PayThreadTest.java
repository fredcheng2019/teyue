package com.sifang.test.thread;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import com.api.base.util.CodeUtils;
import com.api.base.util.SignUtil;

public class PayThreadTest {

	public static final String key = "MUH3UYUMMSPVYAKV";

	// 支付接口
	public static final String POST_URL = "http://localhost:9082/sifang/pay/pay";

	public static void main(String[] args) {
		int count = 0;
		while (count < 1500) {
			System.out.println("第"+count+"个");
			count ++;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
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
						String orderNumber = CodeUtils.createUniqueCode();
						Random random = new Random();              
						//产生随机数
						int number = random.nextInt(40000 - 100 + 1) + 100;
						//String orderNumber = new Date().getTime()+""+number;
						
						String post_data = "mch_id=RER26R6AJYJDX5E3&service=wxqr&out_trade_no="+orderNumber+"&total_fee="+number+"&body=xiaomi8&notify_url=http://localhost:9082/sifang/merchant/simulateNotifyPay";
						out.write((streamToBytes(post_data,key)));
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
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

	}
	
	private static byte[] streamToBytes(String str,String key) throws IOException, IllegalAccessException {
		String sign = SignUtil.getSignReq(str, key);
		System.out.println("获得的sign："+sign);
		String result = str + "&sign="+sign;
		System.out.println("请求报文："+result);
		return result.getBytes();
	}

}
