package com.ruoyi.payApi;

import java.math.BigDecimal;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/26 20:43
 */
public class PayTransReq {
    // 商户号
    private String mch_id;
    // 通道编码
    private String service;
    // 商户订单号
    private String out_trade_no;
    // 金额
    private BigDecimal total_fee;
    // 商品描述
    private String body;
    // 回调地址
    private String notify_url;
    //备注，回调时返回
    private String remark;
    // 签名
    private String sign;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public BigDecimal getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(BigDecimal total_fee) {
        this.total_fee = total_fee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public String toStringOther() {
        return "mch_id=" + mch_id + "&service=" + service + "&out_trade_no=" + out_trade_no + "&total_fee=" + total_fee + "&body=" + body + "&notify_url=" + notify_url+"&remark="+remark;
    }


    public String allToString() {
        return "mch_id=" + mch_id + "&service=" + service + "&out_trade_no=" + out_trade_no + "&total_fee=" + total_fee + "&body=" + body + "&notify_url=" + notify_url +"&remark="+remark + "&sign=" + sign;
    }
}
