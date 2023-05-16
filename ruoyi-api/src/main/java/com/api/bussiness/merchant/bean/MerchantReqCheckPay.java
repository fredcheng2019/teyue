package com.api.bussiness.merchant.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantReqCheckPay {
	
	
	private String mch_id;
	
	private String out_trade_no;
	
	private String sign;
	
	private String secret_key;

}
