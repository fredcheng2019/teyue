package com.ruoyi.merchant.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/11 14:41
 */
public class MpMerchantMethodRSP extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 状态 */
    private Long id;
    /* 渠道名称*/
    @Excel(name = "渠道名称")
    private String channelName;
    /* 钱包类型 */
    @Excel(name = "钱包类型")
    private String walletKindName;
    /* 渠道-支付方法费率*/
    @Excel(name = "渠道支付方法费率")
    private Integer channelPayRate;
    /** 渠道支付方法标识 */
    @Excel(name = "渠道支付方法标识")
    private Long channelMethodId;
    /* 商户名称 */
    @Excel(name = "商户名称")
    private String merchanName;
    /** 商户标识 */
    @Excel(name = "商户标识")
    private Long merchantId;
    /** 支付费率（以万记，比如1表示费率0.01%，10表示0.1%，100表示1%） */
    @Excel(name = "商户-支付方法费率")
    private Integer payRate;
    /** 状态 */
    @Excel(name = "商户-支付方法状态")
    private Integer status;
    /** 优先级，值越大优先使用 */
    @Excel(name = "商户-支付方法优先级")
    private Integer level;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getChannelPayRate() {
        return channelPayRate;
    }

    public void setChannelPayRate(Integer channelPayRate) {
        this.channelPayRate = channelPayRate;
    }

    public Long getChannelMethodId() {
        return channelMethodId;
    }

    public void setChannelMethodId(Long channelMethodId) {
        this.channelMethodId = channelMethodId;
    }

    public String getMerchanName() {
        return merchanName;
    }

    public void setMerchanName(String merchanName) {
        this.merchanName = merchanName;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getPayRate() {
        return payRate;
    }

    public void setPayRate(Integer payRate) {
        this.payRate = payRate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getWalletKindName() {
        return walletKindName;
    }

    public void setWalletKindName(String walletKindName) {
        this.walletKindName = walletKindName;
    }

    @Override
    public String toString() {
        return "MpMerchantMethodRSP{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", walletKindName='" + walletKindName + '\'' +
                ", channelPayRate=" + channelPayRate +
                ", channelMethodId=" + channelMethodId +
                ", merchanName='" + merchanName + '\'' +
                ", merchantId=" + merchantId +
                ", payRate=" + payRate +
                ", status=" + status +
                ", level=" + level +
                '}';
    }
}
