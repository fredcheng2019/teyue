package com.ruoyi.merchant.domain;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 表 mp_merchant_method
 *
 * @author ruoyi
 * @date 2019-05-07
 */
public class MpMerchantMethod extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商户支付方法标识
     */
    @Excel(name = "商户支付方法标识")
    private Long id;
    /**
     * 商户支付方法费率
     */
    @Excel(name = "商户支付方法费率")
    private BigDecimal payRate;
    /**
     * 商户支付方法优先级
     */
    @Excel(name = "商户支付方法优先级")
    private Integer level;
    /**
     * 商户支付方法状态
     */
    @Excel(name = "商户支付方法状态")
    private Integer status;
    /**
     * 商户标识
     */
    @Excel(name = "商户标识")
    private Long merchantId;
    /**
     * 商户名称
     */
    @Excel(name = "商户名称")
    private String merchantName;
    /**
     * 商户编码
     */
    @Excel(name = "商户编码")
    private String merchantCode;
    /**
     * 商户状态
     */
    @Excel(name = "商户状态")
    private Integer merchantStatus;
    /**
     * 渠道支付方法标识
     */
    @Excel(name = "渠道支付方法标识")
    private Long channelMethodId;
    /**
     * 渠道支付方法状态
     */
    @Excel(name = "渠道支付方法状态")
    private Integer channelMethodStatus;
    /**
     * 渠道支付方法费率（以万记，比如1表示费率0.01%，10表示0.1%，100表示1%）
     */
    @Excel(name = "渠道支付方法费率（以万记，比如1表示费率0.01%，10表示0.1%，100表示1%）")
    private BigDecimal channelMethodPayRate;
    /**
     * 渠道支付方法限额左
     */
    @Excel(name = "渠道支付方法限额左")
    private BigDecimal channelMethodPayLimitLeft;
    /**
     * 渠道支付方法限额右
     */
    @Excel(name = "渠道支付方法限额右")
    private BigDecimal channelMethodPayLimitRight;
    /**
     * 渠道标识
     */
    @Excel(name = "渠道标识")
    private Long channelId;
    /**
     * 渠道名称
     */
    @Excel(name = "渠道名称")
    private String channelName;
    /**
     * 渠道编码
     */
    @Excel(name = "渠道编码")
    private String channelCode;
    /**
     * 渠道状态
     */
    @Excel(name = "渠道状态")
    private Integer channelStatus;
    /**
     * 渠道代付优先级
     */
    @Excel(name = "渠道代付优先级")
    private Integer channelWithdrawalLevel;
    /**
     * 渠道代付手续费
     */
    @Excel(name = "渠道代付手续费")
    private BigDecimal channelWithdrawalFee;
    /**
     * 渠道商户代付手续费
     */
    @Excel(name = "渠道商户代付手续费")
    private BigDecimal channelMchWithdrawalFee;
    /**
     * 渠道代付限额左
     */
    @Excel(name = "渠道代付限额左")
    private BigDecimal channelWithdrawalLimtLeft;
    /**
     * 渠道代付限额右
     */
    @Excel(name = "渠道代付限额右")
    private BigDecimal channelWithdrawalLimtRight;
    /**
     * 渠道操作类
     */
    @Excel(name = "渠道操作类")
    private String channelClassName;
    /* 请求渠道url */
    @Excel(name = "请求渠道url")
    private String channelReqUrl;
    /* 渠道秘钥 */
    @Excel(name = "渠道秘钥")
    private String channelSecretKey;
    /* 渠道代扣 */
    @Excel(name = "代扣: 0启用 1停用")
    private Integer channelWithdrawalInner;
    /**
     * 支付类型标识
     */
    @Excel(name = "支付类型标识")
    private Long payKindId;
    /**
     * 支付类型名称
     */
    @Excel(name = "支付类型名称")
    private String payKindName;
    /**
     * 支付类型编码
     */
    @Excel(name = "支付类型编码")
    private String payKindCode;

    /**
     * 钱包类型标识
     */
    @Excel(name = "钱包类型标识")
    private Long walletKindId;
    /**
     * 钱包类型名称
     */
    @Excel(name = "钱包类型名称")
    private String walletKindName;
    /**
     * 钱包类型编码
     */
    @Excel(name = "钱包类型编码")
    private String walletKindCode;

    @Excel(name = "代理ID")
    private Long agentId;

    @Excel(name = "代理提成")
    private BigDecimal agentRate;

    /** 商户支付方法限额左 */
    @Excel(name = "商户支付方法限额左")
    private BigDecimal methodPayLimitLeft;
    /** 商户支付方法限额右 */
    @Excel(name = "商户支付方法限额右")
    private BigDecimal methodPayLimitRight;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPayRate() {
        return payRate;
    }

    public void setPayRate(BigDecimal payRate) {
        this.payRate = payRate;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public Integer getMerchantStatus() {
        return merchantStatus;
    }

    public void setMerchantStatus(Integer merchantStatus) {
        this.merchantStatus = merchantStatus;
    }

    public Long getChannelMethodId() {
        return channelMethodId;
    }

    public void setChannelMethodId(Long channelMethodId) {
        this.channelMethodId = channelMethodId;
    }

    public Integer getChannelMethodStatus() {
        return channelMethodStatus;
    }

    public void setChannelMethodStatus(Integer channelMethodStatus) {
        this.channelMethodStatus = channelMethodStatus;
    }

    public BigDecimal getChannelMethodPayRate() {
        return channelMethodPayRate;
    }

    public void setChannelMethodPayRate(BigDecimal channelMethodPayRate) {
        this.channelMethodPayRate = channelMethodPayRate;
    }

    public BigDecimal getChannelMethodPayLimitLeft() {
        return channelMethodPayLimitLeft;
    }

    public void setChannelMethodPayLimitLeft(BigDecimal channelMethodPayLimitLeft) {
        this.channelMethodPayLimitLeft = channelMethodPayLimitLeft;
    }

    public BigDecimal getChannelMethodPayLimitRight() {
        return channelMethodPayLimitRight;
    }

    public void setChannelMethodPayLimitRight(BigDecimal channelMethodPayLimitRight) {
        this.channelMethodPayLimitRight = channelMethodPayLimitRight;
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

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Integer getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(Integer channelStatus) {
        this.channelStatus = channelStatus;
    }

    public Integer getChannelWithdrawalLevel() {
        return channelWithdrawalLevel;
    }

    public void setChannelWithdrawalLevel(Integer channelWithdrawalLevel) {
        this.channelWithdrawalLevel = channelWithdrawalLevel;
    }

    public BigDecimal getChannelWithdrawalFee() {
        return channelWithdrawalFee;
    }

    public void setChannelWithdrawalFee(BigDecimal channelWithdrawalFee) {
        this.channelWithdrawalFee = channelWithdrawalFee;
    }


    public BigDecimal getChannelMchWithdrawalFee() {
        return channelMchWithdrawalFee;
    }

    public void setChannelMchWithdrawalFee(BigDecimal channelMchWithdrawalFee) {
        this.channelMchWithdrawalFee = channelMchWithdrawalFee;
    }


    public String getChannelClassName() {
        return channelClassName;
    }

    public void setChannelClassName(String channelClassName) {
        this.channelClassName = channelClassName;
    }

    public Long getPayKindId() {
        return payKindId;
    }

    public void setPayKindId(Long payKindId) {
        this.payKindId = payKindId;
    }

    public String getPayKindName() {
        return payKindName;
    }

    public void setPayKindName(String payKindName) {
        this.payKindName = payKindName;
    }

    public String getPayKindCode() {
        return payKindCode;
    }

    public void setPayKindCode(String payKindCode) {
        this.payKindCode = payKindCode;
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

    public String getWalletKindCode() {
        return walletKindCode;
    }

    public void setWalletKindCode(String walletKindCode) {
        this.walletKindCode = walletKindCode;
    }


    public String getChannelReqUrl() {
        return channelReqUrl;
    }

    public void setChannelReqUrl(String channelReqUrl) {
        this.channelReqUrl = channelReqUrl;
    }

    public BigDecimal getChannelWithdrawalLimtLeft() {
        return channelWithdrawalLimtLeft;
    }

    public void setChannelWithdrawalLimtLeft(BigDecimal channelWithdrawalLimtLeft) {
        this.channelWithdrawalLimtLeft = channelWithdrawalLimtLeft;
    }

    public BigDecimal getChannelWithdrawalLimtRight() {
        return channelWithdrawalLimtRight;
    }

    public void setChannelWithdrawalLimtRight(BigDecimal channelWithdrawalLimtRight) {
        this.channelWithdrawalLimtRight = channelWithdrawalLimtRight;
    }

    public String getChannelSecretKey() {
        return channelSecretKey;
    }

    public void setChannelSecretKey(String channelSecretKey) {
        this.channelSecretKey = channelSecretKey;
    }

    public Integer getChannelWithdrawalInner() {
        return channelWithdrawalInner;
    }

    public void setChannelWithdrawalInner(Integer channelWithdrawalInner) {
        this.channelWithdrawalInner = channelWithdrawalInner;
    }


    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public BigDecimal getAgentRate() {
        return agentRate;
    }

    public void setAgentRate(BigDecimal agentRate) {
        this.agentRate = agentRate;
    }

    public void setMethodPayLimitLeft(BigDecimal methodPayLimitLeft)
    {
        this.methodPayLimitLeft = methodPayLimitLeft;
    }

    public BigDecimal getMethodPayLimitLeft()
    {
        return methodPayLimitLeft;
    }
    public void setMethodPayLimitRight(BigDecimal methodPayLimitRight)
    {
        this.methodPayLimitRight = methodPayLimitRight;
    }

    public BigDecimal getMethodPayLimitRight()
    {
        return methodPayLimitRight;
    }


    @Override
    public String toString() {
        return "MpMerchantMethod{" +
                "id=" + id +
                ", payRate=" + payRate +
                ", level=" + level +
                ", status=" + status +
                ", merchantId=" + merchantId +
                ", merchantName='" + merchantName + '\'' +
                ", merchantCode='" + merchantCode + '\'' +
                ", merchantStatus=" + merchantStatus +
                ", channelMethodId=" + channelMethodId +
                ", channelMethodStatus=" + channelMethodStatus +
                ", channelMethodPayRate=" + channelMethodPayRate +
                ", channelMethodPayLimitLeft=" + channelMethodPayLimitLeft +
                ", channelMethodPayLimitRight=" + channelMethodPayLimitRight +
                ", channelId=" + channelId +
                ", channelName='" + channelName + '\'' +
                ", channelCode='" + channelCode + '\'' +
                ", channelStatus=" + channelStatus +
                ", channelWithdrawalLevel=" + channelWithdrawalLevel +
                ", channelWithdrawalFee=" + channelWithdrawalFee +
                ", channelMchWithdrawalFee=" + channelMchWithdrawalFee +
                ", channelWithdrawalLimtLeft=" + channelWithdrawalLimtLeft +
                ", channelWithdrawalLimtRight=" + channelWithdrawalLimtRight +
                ", channelClassName='" + channelClassName + '\'' +
                ", channelReqUrl='" + channelReqUrl + '\'' +
                ", channelSecretKey='" + channelSecretKey + '\'' +
                ", channelWithdrawalInner=" + channelWithdrawalInner +
                ", payKindId=" + payKindId +
                ", payKindName='" + payKindName + '\'' +
                ", payKindCode='" + payKindCode + '\'' +
                ", walletKindId=" + walletKindId +
                ", walletKindName='" + walletKindName + '\'' +
                ", walletKindCode='" + walletKindCode + '\'' +
                ", methodPayLimitLeft=" + methodPayLimitLeft +
                ", methodPayLimitRight=" + methodPayLimitRight +
                '}';
    }
}
