package com.api.bussiness.manager.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerRspWithdrawalSure {
	
	public static int MANAGER_RSP_WITHDRAWAL_SURE_FAIL    = 0;//失败
	
	public static int MANAGER_RSP_WITHDRAWAL_SURE_SUCCESS = 1;//成功
	
	/**
	 * 0操作成功
	 * 1操作失败
	 */
	private int status;
	
	/**
	 * 响应信息
	 */
	private String msg;

}
