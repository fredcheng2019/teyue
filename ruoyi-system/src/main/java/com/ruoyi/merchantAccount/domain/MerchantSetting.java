package com.ruoyi.merchantAccount.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class MerchantSetting extends BaseEntity {

    @Excel(name = "标识")
    private Long id;

    @Excel(name = "商户名称")
    private String name;

    @Excel(name = "商户编码")
    private String code;

    @Excel(name = "商户密钥")
    private String secretKey;

    @Excel(name = "提款密码")
    private String withdrawalPassword;

    @Excel(name = "谷歌校验密钥")
    private String googleSecret;

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
