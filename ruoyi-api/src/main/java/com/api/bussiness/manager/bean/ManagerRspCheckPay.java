package com.api.bussiness.manager.bean;

import com.api.bussiness.dao.table.order.MpPay;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerRspCheckPay {
	public static final int RSP_STATUS_PAY_CHANGE = 0;//支付订单改变
	public static final int RSP_STATUS_PAY_KEEP = 1;//支付订单保持

	private int status;
	private String msg;
	private MpPay mpPay;

}
