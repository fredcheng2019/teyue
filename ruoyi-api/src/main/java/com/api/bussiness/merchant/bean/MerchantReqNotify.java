package com.api.bussiness.merchant.bean;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantReqNotify {
	
	private String mch_id;
	
	private String out_trade_no;
	
	private String order_sn;
	
	private BigDecimal total_fee;
	
	private String service;
	
	private String body;
	
	private String time_start;
	
	private String time_expire;
	
	private String finish_time;
	
	private int status;
	
	private String order_date;
	
	private String sign;

	private String remark;
}
