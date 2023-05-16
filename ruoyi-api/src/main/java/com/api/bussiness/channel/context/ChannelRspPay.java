package com.api.bussiness.channel.context;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 18319
 * 支付订单统一返回
 */
@Data
@NoArgsConstructor
public class ChannelRspPay {

    public static final int RSP_STATUS_PAY_SUCCESS = 0;//下单成功
    public static final int RSP_STATUS_PAY_FAIL = 1;// 下单失败
    public static final int RSP_STATUS_PAY_TIME_OUT = 2;//下单超时

    //下单结果,0成功、1下单失败、2请求失败
    private int status;

    //渠道订单号
    private String ch_code;

    //二维码参数
    private String code_url;

    //信息
    private String msg;


    public ChannelRspPay(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ChannelRspPay(int status, String ch_code, String msg) {
        this.status = status;
        this.ch_code = ch_code;
        this.msg = msg;
    }

    public ChannelRspPay(int status, String ch_code, String code_url, String msg) {
        this.status = status;
        this.ch_code = ch_code;
        this.code_url = code_url;
        this.msg = msg;
    }
}
