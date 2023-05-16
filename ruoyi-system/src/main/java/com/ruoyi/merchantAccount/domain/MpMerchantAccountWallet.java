package com.ruoyi.merchantAccount.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 表 mp_merchant_wallet
 * 
 * @author ruoyi
 * @date 2019-05-06
 */
public class MpMerchantAccountWallet extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 状态 */
	private Integer id;
	/** 商户标识 */
	private Integer merchantId;
	/** 钱包类型标识 */
	@Excel(name = "钱包类型标识")
	private Integer walletKindId;
	/** 余额 */
	@Excel(name = "余额")
	private BigDecimal accountBalance;
	/** 冻结金额 */
	@Excel(name = "冻结金额")
	private BigDecimal frozenBalance;

	private List<Long> merchantIds;

	public List<Long> getMerchantIds() {
		return merchantIds;
	}

	public void setMerchantIds(List<Long> merchantIds) {
		this.merchantIds = merchantIds;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setMerchantId(Integer merchantId) 
	{
		this.merchantId = merchantId;
	}

	public Integer getMerchantId() 
	{
		return merchantId;
	}
	public void setWalletKindId(Integer walletKindId) 
	{
		this.walletKindId = walletKindId;
	}

	public Integer getWalletKindId() 
	{
		return walletKindId;
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

	@Override
	public String toString() {
		return "MpMerchantAccountWallet{" +
				"id=" + id +
				", merchantId=" + merchantId +
				", walletKindId=" + walletKindId +
				", accountBalance=" + accountBalance +
				", frozenBalance=" + frozenBalance +
				'}';
	}
}
