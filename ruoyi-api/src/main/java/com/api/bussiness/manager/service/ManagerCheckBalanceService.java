package com.api.bussiness.manager.service;

import com.api.bussiness.manager.bean.ManagerReqCheckBalance;
import com.api.bussiness.manager.bean.ManagerRspCheckBalance;

public interface ManagerCheckBalanceService {
	
	public ManagerRspCheckBalance checkBalance(ManagerReqCheckBalance managerReqCheckBalance);

}
