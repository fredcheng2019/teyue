package com.api.bussiness.manager.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerReqCheckWithdrawal {

	private String code;//平台订单号
	private String mch_id;
	private String out_trade_no;
}
