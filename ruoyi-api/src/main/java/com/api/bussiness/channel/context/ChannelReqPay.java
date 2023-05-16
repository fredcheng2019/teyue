package com.api.bussiness.channel.context;

import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.order.MpPay;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author 18319
 *支付订单统一请求
 */
@Setter
@Getter
public class ChannelReqPay {
	private MpPay mpPay;
	private MpChannel mpChannel;
	private String channel_redirect_url;
	private String channel_notify_url;
	private String jump_pay_url;
}
