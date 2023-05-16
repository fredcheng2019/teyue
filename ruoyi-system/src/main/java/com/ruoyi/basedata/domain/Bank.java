package com.ruoyi.basedata.domain;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 银行种类表 bank
 * 
 * @author ruoyi
 * @date 2019-05-05
 */
public class Bank extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 标识 */
	private Integer id;
	/** 银行名称 */
	@Excel(name = "银行名称")
	private String name;
	/** 编码 */
	@Excel(name = "编码")
	private String code;


	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
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
		return "Bank{" +
				"id=" + id +
				", name='" + name + '\'' +
				", code='" + code + '\'' +
				'}';
	}
}
