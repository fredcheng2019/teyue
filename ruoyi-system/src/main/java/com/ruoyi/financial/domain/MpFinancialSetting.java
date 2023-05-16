package com.ruoyi.financial.domain;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 表 mp_financial_setting
 * 
 * @author ruoyi
 * @date 2019-05-13
 */
public class MpFinancialSetting extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id;
	/**  */
	@Excel(name = "提款密码")
	private String withdrawalPassword;
	/**  */
	@Excel(name = "谷歌校验密钥")
	private String googleSecret;
	/** 代付规则（1.渠道优先级2.渠道余额大小） */
	@Excel(name = "代付规则",readConverterExp = "1=渠道优先级,2=渠道余额大小")
	private Integer mchWithdrawalRule;
	/* 代扣 */
	@Excel(name = "代扣: 0启用 1停用")
	private Integer withdrawalInner;
	/* 代付审核 */
	@Excel(name = "代付审核: 0启用 1停用")
	private Integer withdrawalAudit;
	/* 渠道ip白名单 */
	@Excel(name = "渠道ip白名单: 0启用 1停用")
	private Integer channelWhite;
	/* 商户ip白名单 */
	@Excel(name = "商户ip白名单: 0启用 1停用")
	private Integer merchantWhite;

	public void setId(Long id) 
	{
		this.id = id;
	}

	public Long getId() 
	{
		return id;
	}
	public void setWithdrawalPassword(String withdrawalPassword) 
	{
		this.withdrawalPassword = withdrawalPassword;
	}

	public String getWithdrawalPassword() 
	{
		return withdrawalPassword;
	}
	public void setGoogleSecret(String googleSecret) 
	{
		this.googleSecret = googleSecret;
	}

	public String getGoogleSecret() 
	{
		return googleSecret;
	}
	public void setMchWithdrawalRule(Integer mchWithdrawalRule) 
	{
		this.mchWithdrawalRule = mchWithdrawalRule;
	}

	public Integer getMchWithdrawalRule() 
	{
		return mchWithdrawalRule;
	}

	public Integer getWithdrawalInner() {
		return withdrawalInner;
	}

	public void setWithdrawalInner(Integer withdrawalInner) {
		this.withdrawalInner = withdrawalInner;
	}

	public Integer getWithdrawalAudit() {
		return withdrawalAudit;
	}

	public void setWithdrawalAudit(Integer withdrawalAudit) {
		this.withdrawalAudit = withdrawalAudit;
	}

	public Integer getChannelWhite() {
		return channelWhite;
	}

	public void setChannelWhite(Integer channelWhite) {
		this.channelWhite = channelWhite;
	}

	public Integer getMerchantWhite() {
		return merchantWhite;
	}

	public void setMerchantWhite(Integer merchantWhite) {
		this.merchantWhite = merchantWhite;
	}

	@Override
	public String toString() {
		return "MpFinancialSetting{" +
				"id=" + id +
				", withdrawalPassword='" + withdrawalPassword + '\'' +
				", googleSecret='" + googleSecret + '\'' +
				", mchWithdrawalRule=" + mchWithdrawalRule +
				", withdrawalInner=" + withdrawalInner +
				", withdrawalAudit=" + withdrawalAudit +
				", channelWhite=" + channelWhite +
				", merchantWhite=" + merchantWhite +
				'}';
	}
}
