package com.api.bussiness.manager.bean;

import com.api.bussiness.dao.table.withdrawal.Withdrawal;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ManagerRspCheckWithdrawal {

	public static final int RSP_STATUS_WITHDRAWAL_CHANGE = 0;//代付订单改变
	public static final int RSP_STATUS_WITHDRAWAL_KEEP = 1;//代付订单保持

	private Withdrawal withdrawal;

	private int status;
	private String msg;

}
