package com.api.bussiness.channel.context;

import java.math.BigDecimal;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class ChannelRspPayNotify {

    public static final int RSP_STATUS_NOTIFY_PAY_SUCCESS = 0;//成功

    public static final int RSP_STATUS_NOTIFY_PAY_FAIL = 1;//失败

    public static final int RSP_STATUS_NOTIFY_PAY_OTHER = 2;//其他，未经过校验的

    //状态，0成功，1失败
    private int status;

    //渠道编号
    private String channel_code;

    //平台订单号
    private String code;

    //渠道订单号
    private String ch_code;

    //交易金额，单位：分;实际扣除金额
    private BigDecimal fee;

    //响应报文
    private String rspMsg;


    public ChannelRspPayNotify(int status, String rspMsg) {
        this.status = status;
        this.rspMsg = rspMsg;
    }


}
