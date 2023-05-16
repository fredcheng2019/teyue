package com.sifang.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.api.base.util.SignUtil;


public class Test {

	//public static final String key = "2b8b43d1a0a24172a0d16278d8307f0b";105031616015正式
	public static final String key = "7MWU2D2IDQWEMDUR";
	
	
	//交易接口
	public static final String POST_URL = "http://localhost:8081/sifang/gateway/payment/payTransReq";
	public static String post_data = "mch_id=36XUOP57ZWPWTOJN&service=aliqr&out_trade_no=155728405052100135&total_fee=12000&body=xiaomi8&notify_url=http://localhost:9082/sifang/merchant/simulateNotifyPay";
	
	//交易查询接口
	//public static final String POST_URL = "http://localhost:9082/sifang/gateway/payment/queryTransInfo";
	//public static String post_data = "mch_id=36XUOP57ZWPWTOJN&out_trade_no=201905311501370010";
	
	//代付接口
	//public static final String POST_URL = "http://localhost:8081/sifang/gateway/payment/withdrawal";
	//public static String post_data = "mch_id=36XUOP57ZWPWTOJN&out_trade_no=15572845505220058&total_fee=11000&bank_car_name=揭光迪&bank_car_no=6235757000003454288&bank_type=BOC&bank_address=广州分行&branch=中国银行广州新宝利大厦支行&bank_province=广东省&bank_city=深圳市&unionBankNum=&wallet_type=wx&bank_account_typ=0&call_back_url=http://localhost:9082/sifang/merchant/simulateNotifyWithdrawal";
	
	//代付查询接口
	//public static final String POST_URL = "http://localhost:9082/sifang/gateway/payment/checkwithdrawal";
	//public static String post_data      = "mch_id=36XUOP57ZWPWTOJN&out_trade_no=201905311505460009";
	
	
	//回调接口
	//支付渠道通知
	//public static final String POST_URL = "http://localhost:9082/PaymentPlatformCloud/callback/pay/TestImpl";
	//public static String post_data = "channel_code=mch29190518001&mchCode=15572840505210015&chCode=201905161415110001&fee=20000&cusValue=hello";
	
	

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
		out.write((streamToBytes(post_data,key)));
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
