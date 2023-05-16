package com.ruoyi.basedata.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 表 areas
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
public class Areas extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 区划ID */
	private String iD;
	/** 父ID */
	private String parentId;
	/** 级别 */
	private String levelType;
	/** 全称 */
	private String name;
	/** 简称 */
	private String shortName;
	/** ID路径 */
	private String parentPath;
	/** 省份全称 */
	private String province;
	/** 城市全称 */
	private String city;
	/** 县区全称 */
	private String district;
	/** 省份简称 */
	private String provinceShortName;
	/** 城市简称 */
	private String cityShortName;
	/** 县区简称 */
	private String districtShortName;
	/** 省份拼音 */
	private String provincePinyin;
	/** 城市拼音 */
	private String cityPinyin;
	/** 县区拼音 */
	private String districtPinyin;
	/** 区号 */
	private String cityCode;
	/** 邮编 */
	private String zipCode;
	/** 拼音 */
	private String pinyin;
	/** 简拼 */
	private String jianpin;
	/** 首拼 */
	private String firstChar;
	/** 经度 */
	private String lng;
	/** 纬度 */
	private String lat;
	/** 是否行政区 */
	private String remark1;
	/** 类型(县级市|地级市|经济开发区|高新区|新区) */
	private String remark2;

	public void setID(String iD) 
	{
		this.iD = iD;
	}

	public String getID() 
	{
		return iD;
	}
	public void setParentId(String parentId) 
	{
		this.parentId = parentId;
	}

	public String getParentId() 
	{
		return parentId;
	}
	public void setLevelType(String levelType) 
	{
		this.levelType = levelType;
	}

	public String getLevelType() 
	{
		return levelType;
	}
	public void setName(String name) 
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	public void setShortName(String shortName) 
	{
		this.shortName = shortName;
	}

	public String getShortName() 
	{
		return shortName;
	}
	public void setParentPath(String parentPath) 
	{
		this.parentPath = parentPath;
	}

	public String getParentPath() 
	{
		return parentPath;
	}
	public void setProvince(String province) 
	{
		this.province = province;
	}

	public String getProvince() 
	{
		return province;
	}
	public void setCity(String city) 
	{
		this.city = city;
	}

	public String getCity() 
	{
		return city;
	}
	public void setDistrict(String district) 
	{
		this.district = district;
	}

	public String getDistrict() 
	{
		return district;
	}
	public void setProvinceShortName(String provinceShortName) 
	{
		this.provinceShortName = provinceShortName;
	}

	public String getProvinceShortName() 
	{
		return provinceShortName;
	}
	public void setCityShortName(String cityShortName) 
	{
		this.cityShortName = cityShortName;
	}

	public String getCityShortName() 
	{
		return cityShortName;
	}
	public void setDistrictShortName(String districtShortName) 
	{
		this.districtShortName = districtShortName;
	}

	public String getDistrictShortName() 
	{
		return districtShortName;
	}
	public void setProvincePinyin(String provincePinyin) 
	{
		this.provincePinyin = provincePinyin;
	}

	public String getProvincePinyin() 
	{
		return provincePinyin;
	}
	public void setCityPinyin(String cityPinyin) 
	{
		this.cityPinyin = cityPinyin;
	}

	public String getCityPinyin() 
	{
		return cityPinyin;
	}
	public void setDistrictPinyin(String districtPinyin) 
	{
		this.districtPinyin = districtPinyin;
	}

	public String getDistrictPinyin() 
	{
		return districtPinyin;
	}
	public void setCityCode(String cityCode) 
	{
		this.cityCode = cityCode;
	}

	public String getCityCode() 
	{
		return cityCode;
	}
	public void setZipCode(String zipCode) 
	{
		this.zipCode = zipCode;
	}

	public String getZipCode() 
	{
		return zipCode;
	}
	public void setPinyin(String pinyin) 
	{
		this.pinyin = pinyin;
	}

	public String getPinyin() 
	{
		return pinyin;
	}
	public void setJianpin(String jianpin) 
	{
		this.jianpin = jianpin;
	}

	public String getJianpin() 
	{
		return jianpin;
	}
	public void setFirstChar(String firstChar) 
	{
		this.firstChar = firstChar;
	}

	public String getFirstChar() 
	{
		return firstChar;
	}
	public void setLng(String lng) 
	{
		this.lng = lng;
	}

	public String getLng() 
	{
		return lng;
	}
	public void setLat(String lat) 
	{
		this.lat = lat;
	}

	public String getLat() 
	{
		return lat;
	}
	public void setRemark1(String remark1) 
	{
		this.remark1 = remark1;
	}

	public String getRemark1() 
	{
		return remark1;
	}
	public void setRemark2(String remark2) 
	{
		this.remark2 = remark2;
	}

	public String getRemark2() 
	{
		return remark2;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("iD", getID())
            .append("parentId", getParentId())
            .append("levelType", getLevelType())
            .append("name", getName())
            .append("shortName", getShortName())
            .append("parentPath", getParentPath())
            .append("province", getProvince())
            .append("city", getCity())
            .append("district", getDistrict())
            .append("provinceShortName", getProvinceShortName())
            .append("cityShortName", getCityShortName())
            .append("districtShortName", getDistrictShortName())
            .append("provincePinyin", getProvincePinyin())
            .append("cityPinyin", getCityPinyin())
            .append("districtPinyin", getDistrictPinyin())
            .append("cityCode", getCityCode())
            .append("zipCode", getZipCode())
            .append("pinyin", getPinyin())
            .append("jianpin", getJianpin())
            .append("firstChar", getFirstChar())
            .append("lng", getLng())
            .append("lat", getLat())
            .append("remark1", getRemark1())
            .append("remark2", getRemark2())
            .toString();
    }
}
