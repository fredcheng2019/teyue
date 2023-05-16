package com.api.bussiness.merchant.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantRspCheckWithdrawal {
	
	private String code;
	
	private String msg;
	
	private String mch_id;
	
	private String out_trade_no;
	
	private int total_fee;
	
	private String bank_car_name;
	
	private String bank_car_no;
	
	private String bank_type;
	
	private String bank_address;
	
	private String branch;
	
	private String bank_province ;
	
	private String bank_city;
	
	private String unionBankNum ;
	
	private String wallet_type;
	
	private int status;
	
	private String order_sn;
	
	private String create_time;
	
	private String finish_time;
	
}
