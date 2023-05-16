package com.ruoyi.merchant.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 表 mp_merchant
 *
 * @author ruoyi
 * @date 2019-05-06
 */
public class MpMerchant extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 标识
     */
    private Long id;
    /**
     * 名称
     */
    @Excel(name = "名称")
    private String name;
    /**
     * 编码
     */
    @Excel(name = "编码")
    private String code;
    /**
     * 状态
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private Integer status;
    /**
     * 用户标识
     */
    @Excel(name = "用户标识")
    private Long userId;
    /**
     * 联系人名称
     */
    @Excel(name = "联系人名称")
    private String contactName;
    /**
     * 联系人电话
     */
    @Excel(name = "联系人电话")
    private String contactPhone;
    /**
     * 联系人邮箱
     */
    @Excel(name = "联系人邮箱")
    private String contactEmail;
    /**
     * 商户密钥
     */
    @Excel(name = "商户密钥")
    private String secretKey;
    /* 提款密码 */
    private String withdrawalPassword;
    /* 谷歌验证秘钥 */
    private String googleSecret;
    /* 是否为代理 */
    private Integer isAgent = 0;
    /* 代理ID */
    private Long agentId;
    /* 代理 提成 */
    private BigDecimal agentProfit;

    /*代付渠道ID*/
    private Long dfChannelId;

    /** 状态0开户，1关闭 */
    private Integer openAdvance;

    public Long getDfChannelId() {
        return dfChannelId;
    }

    public void setDfChannelId(Long dfChannelId) {
        this.dfChannelId = dfChannelId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretKey() {
        return secretKey;
    }


    public String getWithdrawalPassword() {
        return withdrawalPassword;
    }

    public void setWithdrawalPassword(String withdrawalPassword) {
        this.withdrawalPassword = withdrawalPassword;
    }

    public String getGoogleSecret() {
        return googleSecret;
    }

    public void setGoogleSecret(String googleSecret) {
        this.googleSecret = googleSecret;
    }

    public Integer getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(Integer isAgent) {
        this.isAgent = isAgent;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public BigDecimal getAgentProfit() {
        return agentProfit;
    }

    public void setAgentProfit(BigDecimal agentProfit) {
        this.agentProfit = agentProfit;
    }

    public void setOpenAdvance(Integer openAdvance)
    {
        this.openAdvance = openAdvance;
    }

    public Integer getOpenAdvance()
    {
        return openAdvance;
    }


    @Override
    public String toString() {
        return "MpMerchant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", withdrawalPassword='" + withdrawalPassword + '\'' +
                ", googleSecret='" + googleSecret + '\'' +
                '}';
    }
}
