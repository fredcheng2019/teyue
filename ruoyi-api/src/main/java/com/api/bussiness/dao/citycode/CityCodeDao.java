package com.api.bussiness.dao.citycode;

import com.api.bussiness.dao.table.citycode.ERTCityCode;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface CityCodeDao {
	
	@Select("select * from mp_ert_city where city_name = #{cityName} and province_code = "
			+ "(select province_code from mp_ert_province where province_nane = #{provinceNane})")
	public ERTCityCode getERTCityCodeByCityNameAndProvinceName(@Param("cityName")String cityName, @Param("provinceNane")String provinceNane);

}
