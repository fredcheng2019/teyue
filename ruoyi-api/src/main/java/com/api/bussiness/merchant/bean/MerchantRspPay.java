package com.api.bussiness.merchant.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantRspPay {

    private String code;

    private String msg;

    private String mch_id;

    private String out_trade_no;

    private String order_sn;

    private String code_url;
}
