package com.ruoyi.merchant.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/11 23:01
 */
public class MerchantBankRSP extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 标识 */
    private Long id;
    /** 商户标识 */
    @Excel(name = "商户标识")
    private Long merchantId;
    /*商户名称*/
    @Excel(name = "商户名称")
    private String merchantName;
    /** 银行标识 */
    @Excel(name = "银行标识")
    private Long bankId;
    /*银行名称*/
    @Excel(name = "银行名称")
    private String bankName;
    /** 分行 */
    @Excel(name = "分行")
    private String bankBranch;
    /** 支行 */
    @Excel(name = "支行")
    private String bankSubBranch;
    /** 所在省 */
    @Excel(name = "所在省")
    private String bankProvince;
    /** 所在市 */
    @Excel(name = "所在市")
    private String bankCity;
    /** 联行号 */
    @Excel(name = "联行号")
    private String bankUnionNo;
    /** 持卡人 */
    @Excel(name = "持卡人")
    private String bankAccountOwner;
    /** 卡号 */
    @Excel(name = "卡号")
    private String bankAccountNo;
    /** 对公 对私 */
    private Integer bankAccountType;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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


    @Override
    public String toString() {
        return "MerchantBankRSP{" +
                "id=" + id +
                ", merchantId=" + merchantId +
                ", merchantName='" + merchantName + '\'' +
                ", bankId=" + bankId +
                ", bankName='" + bankName + '\'' +
                ", bankBranch='" + bankBranch + '\'' +
                ", bankSubBranch='" + bankSubBranch + '\'' +
                ", bankProvince='" + bankProvince + '\'' +
                ", bankCity='" + bankCity + '\'' +
                ", bankUnionNo='" + bankUnionNo + '\'' +
                ", bankAccountOwner='" + bankAccountOwner + '\'' +
                ", bankAccountNo='" + bankAccountNo + '\'' +
                ", bankAccountType=" + bankAccountType +
                '}';
    }
}
