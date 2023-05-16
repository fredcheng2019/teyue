package com.api.bussiness.manager.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerReqCheckPay {

	private String code;//平台订单号 Manager发起
	private String mch_code;//商户订单号 Merchant发起
	private String merchant_code;//商户表示 Merchant发起
}
