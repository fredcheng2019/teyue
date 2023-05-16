package com.api.bussiness.manager.bean;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ManagerRspCheckBalance {
	
	public static final int RSP_STATUS_BALANCE_SUCCESS = 0;//成功
	
	public static final int RSP_STATUS_FAIL    = 1;//失败
	
	private int status;
	
	private String msg;
	
	/**
	 * 总余额
	 */
	private BigDecimal total_balance;
	
	/**
	 * 总冻结
	 */
	private BigDecimal total_fro_balance;
	
	private Object balance;

}
