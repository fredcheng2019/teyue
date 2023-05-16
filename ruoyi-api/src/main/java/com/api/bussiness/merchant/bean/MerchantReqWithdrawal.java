package com.api.bussiness.merchant.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantReqWithdrawal {
	
	private String mch_id;
	
	private String out_trade_no;
	
	private String total_fee;
	
	private String bank_car_name;
	
	private String bank_car_no;
	
	private String bank_type;
	
	private String bank_address;
	
	private String branch;
	
	private String bank_province;
	
	private String bank_city;
	
	private String unionBankNum;
	
	private String wallet_type;
	
	private String sign;
	
	private String bank_account_type;
	
	private String call_back_url;
	
	private String secret_key;
	

}
