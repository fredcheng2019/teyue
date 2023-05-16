package com.api.bussiness.manager.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerReqNotifyPay {
	private String code;//平台订单号

	/**
	 *  管理后台直接回调功能标识(状态不是已支付的，直接设置成已支付并通知商户)
	 *  1-是  0-否
	 */
	private Integer setPaySuccess = 0;
}
