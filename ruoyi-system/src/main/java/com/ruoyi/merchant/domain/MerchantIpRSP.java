package com.ruoyi.merchant.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/11 20:53
 */
public class MerchantIpRSP extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 商户ip白名单标识 */
    @Excel(name = "商户ip白名单标识")
    private Long id;
    /** 商户标识  */
    @Excel(name = "商户标识")
    private Long merchantId;
    /* 商户名称 */
    @Excel(name = "商户名称")
    private String merchantName;
    /** ip */
    @Excel(name = "ip")
    private String ip;

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

    public void setMerchantName(String merchanName) {
        this.merchantName = merchanName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "MerchantIpRSP{" +
                "id=" + id +
                ", merchantId=" + merchantId +
                ", merchanName='" + merchantName + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }

}
