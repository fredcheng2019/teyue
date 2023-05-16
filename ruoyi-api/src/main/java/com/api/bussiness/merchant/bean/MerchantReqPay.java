package com.api.bussiness.merchant.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantReqPay {
	//商户号
	private String mch_id; 
	
	//通道编码
	private String service;
	
	
	//商户平台订单号
	private String out_trade_no;
	
	
	//金额，单位：分
	private String total_fee;
	
	
	//商品描述
	private String body;
	
	
	//回调通知地址
	private String notify_url;
	
	
	//签名
	private String sign;
	
	private String secret_key;

	//备注，回调时返回
	private String remark;

	private String clientIP;

}
