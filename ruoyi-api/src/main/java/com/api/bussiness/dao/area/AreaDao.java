package com.api.bussiness.dao.area;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Repository
public interface AreaDao {
	
	/**
	 * 根据区域级别（0国家 、1省、2市、3区县）和区域名称获得区域码
	 * @param levelType
	 * @param name
	 * @return
	 */
	@Select("select CityCode from areas where LevelType = #{levelType} and name = #{name}")
	public String getCityCode(@Param("levelType")String levelType,@Param("name")String name);

}
