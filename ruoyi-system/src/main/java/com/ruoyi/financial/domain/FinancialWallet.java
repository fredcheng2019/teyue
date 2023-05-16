package com.ruoyi.financial.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;

/**
 * 表 mp_financial_wallet
 * 
 * @author ruoyi
 * @date 2019-05-08
 */
public class FinancialWallet extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 状态 */
	private Long id;
	/** 渠道标识 */
	private Long channelId;
	/** 余额 */
	private BigDecimal accountBalance;
	/** 冻结金额 */
	private BigDecimal frozenBalance;

	public void setId(Long id) 
	{
		this.id = id;
	}

	public Long getId() 
	{
		return id;
	}
	public void setChannelId(Long channelId) 
	{
		this.channelId = channelId;
	}

	public Long getChannelId() 
	{
		return channelId;
	}

	public void setAccountBalance(BigDecimal accountBalance) 
	{
		this.accountBalance = accountBalance;
	}

	public BigDecimal getAccountBalance() 
	{
		return accountBalance;
	}
	public void setFrozenBalance(BigDecimal frozenBalance) 
	{
		this.frozenBalance = frozenBalance;
	}

	public BigDecimal getFrozenBalance() 
	{
		return frozenBalance;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("channelId", getChannelId())
            .append("accountBalance", getAccountBalance())
            .append("frozenBalance", getFrozenBalance())
            .toString();
    }
}
