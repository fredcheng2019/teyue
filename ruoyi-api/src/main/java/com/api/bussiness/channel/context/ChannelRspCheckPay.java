package com.api.bussiness.channel.context;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 *
 * @author 18319
 *支付订单查询统一返回
 */
@Data
@NoArgsConstructor
public class ChannelRspCheckPay {

	public static final int RSP_STATUS_CHECKPAY_SUCCESS = 0;//支付成功
	public static final int RSP_STATUS_CHECKPAY_FAIL =1;//支付失败
	public static final int RSP_STATUS_CHECKPAY_OTHER = 2;//其它

	private int status;

	private BigDecimal fee;//实际交易金额

	private String code;//平台订单号

	private String ch_code;//渠道订单号

	private String msg;

    public ChannelRspCheckPay(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ChannelRspCheckPay(int status, String ch_code, String msg) {
        this.status = status;
        this.ch_code = ch_code;
        this.msg = msg;
    }


}
