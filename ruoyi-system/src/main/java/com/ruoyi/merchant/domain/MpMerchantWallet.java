package com.ruoyi.merchant.domain;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 表 mp_merchant_wallet
 * 
 * @author ruoyi
 * @date 2019-05-06
 */
public class MpMerchantWallet extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 状态 */
	private Integer id;
	/** 商户标识 */
	@Excel(name = "商户标识")
	private Integer merchantId;
	/** 钱包类型标识 */
	@Excel(name = "钱包类型标识")
	private Integer walletKindId;
	/** 总金额 */
	@Excel(name = "总金额")
	private BigDecimal accountBalance;
	/** 冻结金额 */
	@Excel(name = "冻结金额")
	private BigDecimal frozenBalance;
	/** 待结算金额 */
	@Excel(name = "待结算金额")
	private BigDecimal waitLiquidatedBalance;
	/* 未结算金额1 */
	@Excel(name = "未结算金额1")
	private BigDecimal unliquidatedBalanceOne;
	/* 未结算金额2 */
	@Excel(name = "未结算金额2")
	private BigDecimal unliquidatedBalanceTwo;
	/* dZero */
	@Excel(name = "dZero")
	private BigDecimal dZero;
	@Excel(name = "预付金额")
	private BigDecimal advanceBalance;

	private String  remark;

	@Override
	public String getRemark() {
		return remark;
	}

	@Override
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/* 是否为代理 */
	private Integer isAgent = 0;

	public Integer getIsAgent() {
		return isAgent;
	}

	public void setIsAgent(Integer isAgent) {
		this.isAgent = isAgent;
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

	public BigDecimal getUnliquidatedBalanceOne() {
		return unliquidatedBalanceOne;
	}

	public void setUnliquidatedBalanceOne(BigDecimal unliquidatedBalanceOne) {
		this.unliquidatedBalanceOne = unliquidatedBalanceOne;
	}

	public BigDecimal getUnliquidatedBalanceTwo() {
		return unliquidatedBalanceTwo;
	}

	public void setUnliquidatedBalanceTwo(BigDecimal unliquidatedBalanceTwo) {
		this.unliquidatedBalanceTwo = unliquidatedBalanceTwo;
	}

	public BigDecimal getdZero() {
		return dZero;
	}

	public void setdZero(BigDecimal dZero) {
		this.dZero = dZero;
	}

	public BigDecimal getWaitLiquidatedBalance() {
		return waitLiquidatedBalance;
	}

	public void setWaitLiquidatedBalance(BigDecimal waitLiquidatedBalance) {
		this.waitLiquidatedBalance = waitLiquidatedBalance;
	}

	public BigDecimal getAdvanceBalance() {
		return advanceBalance;
	}

	public void setAdvanceBalance(BigDecimal advanceBalance) {
		this.advanceBalance = advanceBalance;
	}

	@Override
	public String toString() {
		return "MpMerchantWallet{" +
				"id=" + id +
				", merchantId=" + merchantId +
				", walletKindId=" + walletKindId +
				", accountBalance=" + accountBalance +
				", frozenBalance=" + frozenBalance +
				", waitLiquidatedBalance=" + waitLiquidatedBalance +
				", unliquidatedBalanceOne=" + unliquidatedBalanceOne +
				", unliquidatedBalanceTwo=" + unliquidatedBalanceTwo +
				", dZero=" + dZero +
				", advanceBalance=" + advanceBalance +
				'}';
	}
}
