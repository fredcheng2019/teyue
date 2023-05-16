package com.ruoyi.basedata.domain;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 表 pay_kind
 * 
 * @author ruoyi
 * @date 2019-05-06
 */
public class PayKind extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 标识 */
	private Long id;
	/** 支付类型名称 */
	@Excel(name = "支付类型名称")
	private String name;
	/** 编码 */
	@Excel(name = "编码")
	private String code;


	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return id;
	}
	public void setName(String name) 
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	public void setCode(String code) 
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}


	@Override
	public String toString() {
		return "PayKind{" +
				"id=" + id +
				", name='" + name + '\'' +
				", code='" + code + '\'' +
				'}';
	}
}
