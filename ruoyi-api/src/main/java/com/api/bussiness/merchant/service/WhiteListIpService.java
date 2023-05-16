package com.api.bussiness.merchant.service;

import com.api.bussiness.dao.table.finance.FinancialSetting;

public interface WhiteListIpService {
	
	boolean getMerchantWhiteListCountByIp(String ip);
	
	
	boolean getChannelWhiteListCountByIp(String ip);
	
	
	FinancialSetting getFinancialSetting();

}
