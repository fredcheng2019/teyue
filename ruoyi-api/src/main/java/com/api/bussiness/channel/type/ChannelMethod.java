package com.api.bussiness.channel.type;

import com.api.bussiness.channel.context.*;

/**
 * 	所有渠道需要实现这个接口
 * @author 首信易支付
 *
 */
public interface ChannelMethod {

	//
	public ChannelRspPay pay(ChannelReqPay channelReqPay);

	//查询
	public ChannelRspCheckPay checkPay(ChannelReqCheckPay channelReqCheckPay);

	//
	public ChannelRspWithdrawal withdrawal(ChannelReqWithdrawal channelReqWithdrawal);

	//查询
	public ChannelRspCheckWithdrawal checkWithdrawal(ChannelReqCheckWithdrawal channelReqCheckWithdrawal);

	//余额查询
	public ChannelRspCheckBalance checkBalance(ChannelReqCheckBalance channelReqCheckBalance);

	//支付异步回调处理
	public ChannelRspPayNotify parsePayNotify(String msg, String channel_code, String channel_secret_key);

	//代付异步回调处理
	public ChannelRspWithdrawalNotify parseWithDrawalNotify(String msg, String channel_code, String channel_secret_key);
}
