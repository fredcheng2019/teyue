package com.api.bussiness.manager.bean;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerReqPay {
	
	//商户支付方式id
	private long mp_merchant_method_id;
	
	//商户订单号
	private String mch_code;
	
	//渠道订单号
	private String ch_code;
	
	//订单金额，单位分
	private BigDecimal total_fee;

}
