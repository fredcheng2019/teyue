package com.api.bussiness.channel.context;

import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.order.MpPay;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author 18319
 *订单查询统一返回
 */
@Setter
@Getter
public class ChannelReqCheckPay {
    private MpPay mpPay;
    private MpChannel mpChannel;
}
