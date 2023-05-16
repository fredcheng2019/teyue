package com.ruoyi.basedata.domain;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 表 mp_ip_black
 * 
 * @author ruoyi
 * @date 2019-05-15
 */
public class MpIpBlack extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 标识 */
	@Excel(name = "标识")
	private Long id;
	/** ip */
	@Excel(name = "ip")
	private String ip;

	public void setId(Long id) 
	{
		this.id = id;
	}

	public Long getId() 
	{
		return id;
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
            .append("ip", getIp())
            .toString();
    }
}
