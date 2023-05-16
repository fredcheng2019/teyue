package com.api.bussiness.dao.table.citycode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ERTCityCode {
	
	private long id;

	private String city_code;
	
	private String province_code;

	private String provinceNane;

	private String city_name;
}
