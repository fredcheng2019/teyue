package com.api.bussiness.channel.context;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author 18319
 *代付统一返回
 */
@Setter
@Getter
public class ChannelRspWithdrawal {
	
	public static final int RSP_STATUS_WITHDRAWAL_SUCCESS = 0;//下单成功
	public static final int RSP_STATUS_WITHDRAWAL_FAIL = 1;//下单失败
	public static final int RSP_STATUS_WITHDRAWAL_TIME_OUT = 2;//请求超时

	private int status;

	//渠道订单号
	private String ch_code;

	//错误信息
	private String msg;
	
}
