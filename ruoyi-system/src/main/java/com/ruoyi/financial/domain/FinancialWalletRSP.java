package com.ruoyi.financial.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/12 17:49
 */
public class FinancialWalletRSP extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;
    /** 渠道标识 */
    private Long channelId;
    /** 渠道名称 */
    @Excel(name = "渠道名称")
    private String channelName;
    /** 余额 */
    @Excel(name = "余额")
    private BigDecimal accountBalance;
    /** 冻结金额 */
    @Excel(name = "冻结金额")
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
        return "FinancialWalletRSP{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", channelName=" + channelName +
                ", accountBalance=" + accountBalance +
                ", frozenBalance=" + frozenBalance +
                '}';
    }


}
