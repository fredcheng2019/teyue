package com.api.bussiness.channel.context;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChannelRspWithdrawalNotify {
	
	public static final int RSP_STATUS_NOTIFY_WITHDRAWAL_SUCCESS = 0;//成功
	
	public static final int RSP_STATUS_NOTIFY_WITHDRAWAL_FAIL = 1;//失败
	
	public static final int RSP_STATUS_NOTIFY_WITHDRAWAL_OTHER = 2;//其他
	
	//状态，0成功，1失败
	private int status;
	
	//商户号
	private String channel_code;
	
	//扣除金额
	private BigDecimal fee;
	
	//平台订单号
	private String code;
	
	//渠道订单号
	private String ch_code;
	
	//响应报文
	private String rspMsg;

	//是否校验扣除金额
	private boolean checkFee = true;

}
