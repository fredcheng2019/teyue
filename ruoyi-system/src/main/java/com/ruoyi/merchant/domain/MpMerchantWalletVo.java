package com.ruoyi.merchant.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 表 mp_merchant_wallet
 * 
 * @author ruoyi
 * @date 2019-05-06
 */
public class MpMerchantWalletVo extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 状态 */
	private Integer id;
	/** 商户标识 */
	@Excel(name = "商户标识")
	private Integer merchantId;
	/** 商户名称 */
	@Excel(name = "商户名称")
	private String merchantName;
	/** 钱包类型标识 */
	@Excel(name = "钱包类型标识")
	private Integer walletKindId;
	/** 钱包类型名称 */
	@Excel(name = "钱包类型名称")
	private String walletKindName;
	/** 余额 */
	@Excel(name = "余额")
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

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getWalletKindName() {
		return walletKindName;
	}

	public void setWalletKindName(String walletKindName) {
		this.walletKindName = walletKindName;
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
		return "MpMerchantWalletVo{" +
				"id=" + id +
				", merchantId=" + merchantId +
				", merchantName='" + merchantName + '\'' +
				", walletKindId=" + walletKindId +
				", walletKindName='" + walletKindName + '\'' +
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
