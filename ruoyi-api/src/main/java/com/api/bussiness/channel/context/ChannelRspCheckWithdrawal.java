package com.api.bussiness.channel.context;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChannelRspCheckWithdrawal {
	public static final int RSP_STATUS_WITHDRAWAL_SUCCESS = 0;//代付成功
	public static final int RSP_STATUS_WITHDRAWAL_FAIL= 1;//代付失败
	public static final int RSP_STATUS_WITHDRAWAL_OTHER= 2;//其它
	private int status;
	
	private BigDecimal fee;//实际交易金额
	
	private String code;//平台订单号
	
	private String ch_code;//渠道订单号
	
	private String msg;

}
