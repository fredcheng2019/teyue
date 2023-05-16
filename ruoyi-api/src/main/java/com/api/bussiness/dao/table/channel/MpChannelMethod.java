package com.api.bussiness.dao.table.channel;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MpChannelMethod {

    /**
     * 状态
     */
    private long id;
    /**
     * 状态
     */
    private int status;
    /**
     * 支付费率（%）
     */
    private BigDecimal pay_rate;
    /**
     * 支付限额左
     */
    private BigDecimal pay_limit_left;
    /**
     * 支付限额右
     */
    private BigDecimal pay_limit_right;
    /**
     * 渠道标识
     */
    private long channel_id;
    /**
     * 渠道名称
     */
    private String channel_name;
    /**
     * 渠道编码
     */
    private String channel_code;
    /**
     * 渠道支付方法编码
     */
    private String channel_method_code;
    /**
     * 渠道状态
     */
    private int channel_status;
    /**
     * 渠道代付优先级
     */
    private int channel_withdrawal_level;
    /**
     * 渠道代付手续费
     */
    private BigDecimal channel_withdrawal_fee;
    /**
     * 渠道代付限额
     */
    private BigDecimal channel_withdrawal_limt;
    /**
     * 渠道密钥
     */
    private String channel_secret_key;
    /**
     * 渠道请求地址
     */
    private String channel_req_url;
    /**
     * 渠道操作类
     */
    private int channel_class_name;
    /**
     * 渠道商户代付手续费
     */
    private BigDecimal withdrawal_limt_left;
    /**
     * 渠道商户代付限额
     */
    private BigDecimal withdrawal_limt_right;
    /**
     * 支付类型标识
     */
    private long pay_kind_id;
    /**
     * 支付类型名称
     */
    private String pay_kind_name;
    /**
     * 支付类型编码
     */
    private String pay_kind_code;
    /**
     * 钱包类型标识
     */
    private long wallet_kind_id;
    /**
     * 钱包类型名称
     */
    private String wallet_kind_name;
    /**
     * 钱包类型编码
     */
    private String wallet_kind_code;


}
