package com.ruoyi.payApi;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/26 20:57
 */
public class PayTransRsp {
    // 商户号
    private String mch_id;
    // 商户订单号
    private String out_trade_no;
    // 平台订单号
    private String order_sn;
    // 二维码参数
    private String code_url;
    // 签名
    private String sign;


    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
