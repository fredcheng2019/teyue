package com.api.bussiness.manager.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerReqWithdrawalApplying {
	
	public static int MANAGER_REQ_WITHDRAWAL_APPLYING_PASS    = 1;//通过
	
	public static int MANAGER_REQ_WITHDRAWAL_APPLYING_REFUSE  = 2;//拒绝
	
	/**
	 * 1通过
	 * 2拒绝
	 */
	private int type;
	
	/**
	 * 代付订单号
	 */
	private String code;

}
