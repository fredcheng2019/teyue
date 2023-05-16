package com.api.bussiness.dao.ip;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface WhiteListIpDao {
	
	@Select("select count(*) from mp_merchant_ip where ip = #{ip}")
	int getMerchantWhiteListCountByIp(@Param("ip")String ip);
	
	
	@Select("select count(*) from mp_channel_ip where ip = #{ip}")
	int getChannelWhiteListCountByIp(@Param("ip")String ip);

}
