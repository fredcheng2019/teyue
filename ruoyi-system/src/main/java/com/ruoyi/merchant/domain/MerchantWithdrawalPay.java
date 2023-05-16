package com.ruoyi.merchant.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/16 14:58
 */
public class MerchantWithdrawalPay extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     *  渠道id
     */
    private Long channelId;

    /**
     *  商户id
     */
    private Long merchantId;

    /**
     * 商户钱包类型id
     */
    private Long walletKindId;

    /**
     * 代付金额
     */
    private BigDecimal  accountBalance;

    /**
     * 银行类型
     */
    private Long bankId;

    /**
     * 分行
     */
    private String bankBranch;

    /**
     * 支行
     */
    private String bankSubBranch;

    /**
     * 所在省
     */
    private String bankProvince;

    /**
     * 所在市
     */
    private String bankCity;

    /**
     * 联行号
     */
    private String bankUnionNo;

    /**
     * 持卡人
     */
    private String bankAccountOwner;

    /**
     * 卡号
     */
    private String bankAccountNo;

    /**
     * 账户类型
     */
   private Integer bankAccountType;

    /**
     * 取款密码
     */
    private String withdrawalPassword;

    /**
     * 谷歌机器验证码
     */
    private String googleCode;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelid) {
        this.channelId = channelid;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getWalletKindId() {
        return walletKindId;
    }

    public void setWalletKindId(Long walletKindId) {
        this.walletKindId = walletKindId;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankSubBranch() {
        return bankSubBranch;
    }

    public void setBankSubBranch(String bankSubBranch) {
        this.bankSubBranch = bankSubBranch;
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getBankUnionNo() {
        return bankUnionNo;
    }

    public void setBankUnionNo(String bankUnionNo) {
        this.bankUnionNo = bankUnionNo;
    }

    public String getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(String bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public Integer getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(Integer bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public String getWithdrawalPassword() {
        return withdrawalPassword;
    }

    public void setWithdrawalPassword(String withdrawalPassword) {
        this.withdrawalPassword = withdrawalPassword;
    }

    public String getGoogleCode() {
        return googleCode;
    }

    public void setGoogleCode(String googleCode) {
        this.googleCode = googleCode;
    }

    @Override
    public String toString() {
        return "MerchantWithdrawalPay{" +
                "channelid=" + channelId +
                ", merchantId=" + merchantId +
                ", walletKindId=" + walletKindId +
                ", accountBalance=" + accountBalance +
                ", bankId=" + bankId +
                ", bankBranch='" + bankBranch + '\'' +
                ", bankSubBranch='" + bankSubBranch + '\'' +
                ", bankProvince='" + bankProvince + '\'' +
                ", bankCity='" + bankCity + '\'' +
                ", bankUnionNo='" + bankUnionNo + '\'' +
                ", bankAccountOwner='" + bankAccountOwner + '\'' +
                ", bankAccountNo='" + bankAccountNo + '\'' +
                ", bankAccountType=" + bankAccountType +
                ", withdrawalPassword=" + withdrawalPassword +
                ", googleCode=" + googleCode +
                '}';
    }
}
