package com.ruoyi.merchant.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 表 mp_merchant_ip
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
public class MerchantIp extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 商户ip白名单标识 */
	private Long id;
	/** 商户标识 */
	private Long merchantId;
	/** ip */
	private String ip;

	public void setId(Long id) 
	{
		this.id = id;
	}

	public Long getId() 
	{
		return id;
	}
	public void setMerchantId(Long merchantId) 
	{
		this.merchantId = merchantId;
	}

	public Long getMerchantId() 
	{
		return merchantId;
	}
	public void setIp(String ip) 
	{
		this.ip = ip;
	}

	public String getIp() 
	{
		return ip;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("merchantId", getMerchantId())
            .append("ip", getIp())
            .toString();
    }
}
