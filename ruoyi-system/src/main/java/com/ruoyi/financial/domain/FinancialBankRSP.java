package com.ruoyi.financial.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 表 mp_financial_bank
 * 
 * @author ruoyi
 * @date 2019-05-08
 */
public class FinancialBankRSP extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 标识 */
	@Excel(name = "标识")
	private Long id;
	/** 银行标识 */
	private Long bankId;
	/* 银行类型 */
	@Excel(name = "银行类型")
	private String bankName;
	/** 分行 */
	@Excel(name = "分行")
	private String bankBranch;
	/** 支行 */
	@Excel(name = "支行")
	private String bankSubBranch;
	/** 所在省 */
	@Excel(name = "所在省")
	private String bankProvince;
	/** 所在市 */
	@Excel(name = "所在市")
	private String bankCity;
	/** 联行号 */
	@Excel(name = "联行号")
	private String bankUnionNo;
	/** 户主 */
	@Excel(name = "持卡人")
	private String bankAccountOwner;
	/** 账户号 */
	@Excel(name = "账户号")
	private String bankAccountNo;
	/** 账户类型 */
	@Excel(name = "账户类型")
	private Integer bankAccountType;

	public void setId(Long id) 
	{
		this.id = id;
	}

	public Long getId() 
	{
		return id;
	}
	public void setBankId(Long bankId) 
	{
		this.bankId = bankId;
	}

	public Long getBankId() 
	{
		return bankId;
	}
	public void setBankBranch(String bankBranch) 
	{
		this.bankBranch = bankBranch;
	}

	public String getBankBranch() 
	{
		return bankBranch;
	}
	public void setBankSubBranch(String bankSubBranch) 
	{
		this.bankSubBranch = bankSubBranch;
	}

	public String getBankSubBranch() 
	{
		return bankSubBranch;
	}
	public void setBankProvince(String bankProvince) 
	{
		this.bankProvince = bankProvince;
	}

	public String getBankProvince() 
	{
		return bankProvince;
	}
	public void setBankCity(String bankCity) 
	{
		this.bankCity = bankCity;
	}

	public String getBankCity() 
	{
		return bankCity;
	}
	public void setBankUnionNo(String bankUnionNo) 
	{
		this.bankUnionNo = bankUnionNo;
	}

	public String getBankUnionNo() 
	{
		return bankUnionNo;
	}
	public void setBankAccountOwner(String bankAccountOwner) 
	{
		this.bankAccountOwner = bankAccountOwner;
	}

	public String getBankAccountOwner() 
	{
		return bankAccountOwner;
	}
	public void setBankAccountNo(String bankAccountNo) 
	{
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankAccountNo() 
	{
		return bankAccountNo;
	}
	public void setBankAccountType(Integer bankAccountType) 
	{
		this.bankAccountType = bankAccountType;
	}

	public Integer getBankAccountType() 
	{
		return bankAccountType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
}
