package com.api.bussiness.merchant.service;

import com.api.bussiness.dao.table.order.MpPay;

public interface MerchantNotifyService {
	
	/**
	 * 支付结果异步通知
	 * @param code
	 */
	void payNotifySync(MpPay mpPay);
	
	/**
	 * 支付结果同步通知
	 * @param pay
	 */
	int payNotify(MpPay pay);
	
	
	/**
	 * 代付结果通知
	 * @param code
	 */
	void withDrawalCallBackNotify(String code);

}
