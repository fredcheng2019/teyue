package com.ruoyi.merchant.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/14 21:49
 */
public class MerchantWithdrawal extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 渠道钱包id
     */
    private Long id;

    /**
     * 渠道id
     */
    private Long channelId;

    /*渠道名称*/
    private String channelName;

    /* 钱包类型id */
    private  Long walletKindId;

    /* 钱包类型名称 */
    private String walletKindName;

    /*余额*/
    private BigDecimal accountBalance;

    /*冻结金额*/
    private BigDecimal frozenBalance;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Long getWalletKindId() {
        return walletKindId;
    }

    public void setWalletKindId(Long walletKindId) {
        this.walletKindId = walletKindId;
    }

    public String getWalletKindName() {
        return walletKindName;
    }

    public void setWalletKindName(String walletKindName) {
        this.walletKindName = walletKindName;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
    }


    @Override
    public String toString() {
        return "MerchantWithdrawal{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", channelName='" + channelName + '\'' +
                ", walletKindId=" + walletKindId +
                ", walletKindName='" + walletKindName + '\'' +
                ", accountBalance=" + accountBalance +
                ", frozenBalance=" + frozenBalance +
                '}';
    }



}
