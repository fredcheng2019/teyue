package com.api.bussiness.manager.service;

import com.api.bussiness.manager.bean.ManagerReqPay;
import com.api.bussiness.manager.bean.ManagerRspPay;

public interface ManagerPayService {
	
	public ManagerRspPay pay(ManagerReqPay managerReqPay) throws Exception;

}
