package com.ruoyi.withdrawalParam.domain;

import java.math.BigDecimal;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/24 10:45
 */
public class WithdrawalCheckRsp {
    // 商户号
    private String mch_id;
    // 商户订单号
    private String out_trade_no;
    // 金额
    private BigDecimal total_fee;
    // 持卡人名称
    private String bank_car_name;
    // 银行卡账户
    private String bank_car_no;
    // 开户银行代码
    private String bank_type;
    // 开户分行
    private String bank_address;
    // 支行名称
    private String branch;
    // 开户省
    private String bank_province;
    // 开户市
    private String bank_city;
    // 联行号
    private String unionBankNum;
    // 钱包类型
    private String wallet_type;
    // 状态 0代付失败  1未到账   2已到账
    private int status;
    // 平台订单号
    private String order_sn;
    // 订单创建时间
    private String create_time;
    // 订单完成时间
    private String finish_time;


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

    public BigDecimal getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(BigDecimal total_fee) {
        this.total_fee = total_fee;
    }

    public String getBank_car_name() {
        return bank_car_name;
    }

    public void setBank_car_name(String bank_car_name) {
        this.bank_car_name = bank_car_name;
    }

    public String getBank_car_no() {
        return bank_car_no;
    }

    public void setBank_car_no(String bank_car_no) {
        this.bank_car_no = bank_car_no;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getBank_address() {
        return bank_address;
    }

    public void setBank_address(String bank_address) {
        this.bank_address = bank_address;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBank_province() {
        return bank_province;
    }

    public void setBank_province(String bank_province) {
        this.bank_province = bank_province;
    }

    public String getBank_city() {
        return bank_city;
    }

    public void setBank_city(String bank_city) {
        this.bank_city = bank_city;
    }

    public String getUnionBankNum() {
        return unionBankNum;
    }

    public void setUnionBankNum(String unionBankNum) {
        this.unionBankNum = unionBankNum;
    }

    public String getWallet_type() {
        return wallet_type;
    }

    public void setWallet_type(String wallet_type) {
        this.wallet_type = wallet_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }
}
