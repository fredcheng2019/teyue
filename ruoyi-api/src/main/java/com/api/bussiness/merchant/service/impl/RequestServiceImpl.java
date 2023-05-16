package com.api.bussiness.merchant.service.impl;

import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.merchant.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("RequestService")
public class RequestServiceImpl implements RequestService {
	
	private static Logger log = LoggerFactory.getLogger(RequestService.class);
	
	@Autowired
    MerchantDao merchantDao;

	@Override
	public String getMerchantKeyByCode(String code) {
		log.info("通过订单号获取商户秘钥");
		return merchantDao.getMerchantKeyByCode(code);
	}
	
	
	

}
