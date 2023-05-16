package com.ruoyi.withdrawalParam.domain;

import java.math.BigDecimal;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/24 11:42
 */
public class PayCheckRsp {
    // 商户号
    private String mch_id;
    // 商户订单号
    private String out_trade_no;
    // 支付类型编码
    private String channel_pay_code;
    // 金额
    private BigDecimal total_fee;
    // 交易开始时间
    private String time_start;
    // 交易过期时间
    private String time_expire;
    // 回调时间  已支付才有   未支付为0
    private String finish_time;
    // 状态 0未支付  1已支付
    private int status;
    // 订单创建时间
    private String create_time;
    // 回调状态  0未支付   1通知    2通知成功
    private int response_status;

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

    public String getChannel_pay_code() {
        return channel_pay_code;
    }

    public void setChannel_pay_code(String channel_pay_code) {
        this.channel_pay_code = channel_pay_code;
    }

    public BigDecimal getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(BigDecimal total_fee) {
        this.total_fee = total_fee;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getResponse_status() {
        return response_status;
    }

    public void setResponse_status(int response_status) {
        this.response_status = response_status;
    }
}
