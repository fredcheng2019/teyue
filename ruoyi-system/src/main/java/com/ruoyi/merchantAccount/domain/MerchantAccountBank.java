package com.ruoyi.merchantAccount.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 表 mp_merchant_bank
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
public class MerchantAccountBank extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 标识 */
	private Long id;
	/** 商户标识 */
	private Long merchantId;
	/** 银行标识 */
	private Long bankId;
	/** 分行 */
	private String bankBranch;
	/** 支行 */
	private String bankSubBranch;
	/** 所在省 */
	private String bankProvince;
	/** 所在市 */
	private String bankCity;
	/** 联行号 */
	private String bankUnionNo;
	/** 持卡人 */
	private String bankAccountOwner;
	/** 卡号 */
	private String bankAccountNo;
	/** 对公 对私 */
	private Integer bankAccountType;

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

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("merchantId", getMerchantId())
            .append("bankId", getBankId())
            .append("bankBranch", getBankBranch())
            .append("bankSubBranch", getBankSubBranch())
            .append("bankProvince", getBankProvince())
            .append("bankCity", getBankCity())
            .append("bankUnionNo", getBankUnionNo())
            .append("bankAccountOwner", getBankAccountOwner())
            .append("bankAccountNo", getBankAccountNo())
            .append("bankAccountType", getBankAccountType())
            .toString();
    }
}
