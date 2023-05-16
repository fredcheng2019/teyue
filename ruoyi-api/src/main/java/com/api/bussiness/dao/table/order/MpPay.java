package com.api.bussiness.dao.table.order;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MpPay {


    public static final int STATUS_ORDER_FAIL = 1;//1下单失败

    public static final int STATUS_ORDER_TIME_OUT = 2;//2下单超时

    public static final int STATUS_WAIT_TO_PAY = 3;//3待支付、

    public static final int STATUS_PAY_FAIL = 4;//4未支付、

    public static final int STATUS_PAY_SUCCESS = 5;//5已支付


    private long id;

    private String code;

    private String ch_code;

    private String mch_code;

    private BigDecimal fee;

    private BigDecimal sub_fee;

    private BigDecimal fin_fee;

    private BigDecimal ch_fee;

    private BigDecimal mch_fee;

    private String goods;

    private Date begin;

    private Date end;

    private Date expires;

    private int status;

    private long merchant_method_id;

    private long merchant_id;

    private BigDecimal merchant_method_pay_rate;

    private String merchant_name;

    private String merchant_code;

    private long channel_method_id;

    private BigDecimal channel_method_pay_rate;

    private long channel_id;

    private String channel_name;

    private String channel_code;

    private String channel_class_name;

    private long pay_kind_id;

    private String pay_kind_name;

    private String pay_kind_code;

    private long wallet_kind_id;

    private String wallet_kind_name;

    private String wallet_kind_code;

    private String callback_url;

    private String redirect_url;

    private int merchant_notify_status;

    private int merchant_notify_times;

    private String remarks;

    private Long agent_id;

    private BigDecimal agent_fee;

    private BigDecimal agent_pay_rate;

    private String client_ip;

    private BigDecimal usdt_rate;
}
