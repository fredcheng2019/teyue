package com.api.bussiness.merchant.bean;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantRspCheckPay {
	
	
	private String code;
	
	private String msg;
	
	private String mch_id;
	
	private String out_trade_no;
	
	private String order_sn;
	
	private String channel_pay_code;
	
	private BigDecimal total_fee;
	
	private String time_start;
	
	private String time_expire;
	
	private String finish_time;
	
	private int status;
	
	private String create_time;
	
	private int response_status;

	private String remark;

}
