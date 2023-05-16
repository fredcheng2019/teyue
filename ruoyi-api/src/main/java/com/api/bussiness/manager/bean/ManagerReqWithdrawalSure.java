package com.api.bussiness.manager.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerReqWithdrawalSure {
	
	public static int MANAGER_REQ_WITHDRAWAL_SURE_FAIL    = 1;//确认失败
	
	public static int MANAGER_REQ_WITHDRAWAL_SURE_SUCCESS = 2;//确认成功
	
	/**
	 * 1失败
	 * 2成功
	 */
	private int type;
	
	/**
	 * 代付订单号
	 */
	private String code;

}
