package com.api.bussiness.channel.context;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChannelRspCheckBalance {

	public static final int RSP_STATUS_CHECKBALANCE_SUCCESS = 0;

	public static final int RSP_STATUS_CHECKBALANCE_FAIL = 1;


	private int status;

	private String msg;

	private BigDecimal total;//总金额
	private BigDecimal frozen;//冻结金额
	private BigDecimal unliquidated;//未结算金额
	private BigDecimal avaiable;//可用金额

	private Object balance;

}
