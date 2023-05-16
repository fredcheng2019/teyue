package com.api.bussiness.channel.context;

import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.withdrawal.Withdrawal;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author 18319
 *代付请求
 */

@Setter
@Getter
public class ChannelReqWithdrawal {
	private Withdrawal withdrawal;
	private MpChannel mpChannel;
	private String channel_notify_url;
	private String provinceCode;
	private String cityCode;
}
