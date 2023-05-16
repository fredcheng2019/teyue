package com.api.bussiness.merchant.service.impl;

import com.api.bussiness.dao.ip.WhiteListIpDao;
import com.api.bussiness.merchant.service.WhiteListIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.bussiness.dao.finance.FinanceDao;
import com.api.bussiness.dao.table.finance.FinancialSetting;


@Service("whiteListIpService")
public class WhiteListIpServiceImpl implements WhiteListIpService {
	
	@Autowired
    WhiteListIpDao whiteListIpDao;
	
	@Autowired
	FinanceDao financeDao;

	@Override
	public boolean getMerchantWhiteListCountByIp(String ip) {
		return whiteListIpDao.getMerchantWhiteListCountByIp(ip)>0;
	}

	@Override
	public boolean getChannelWhiteListCountByIp(String ip) {
		return whiteListIpDao.getChannelWhiteListCountByIp(ip)>0;
	}

	@Override
	public FinancialSetting getFinancialSetting() {
		return financeDao.getFinancialSetting();
	}

}
