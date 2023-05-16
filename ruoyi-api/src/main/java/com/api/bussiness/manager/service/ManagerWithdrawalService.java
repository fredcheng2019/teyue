package com.api.bussiness.manager.service;

import com.api.bussiness.manager.bean.ManagerReqWithdrawal;
import com.api.bussiness.manager.bean.ManagerRspWithdrawal;

public interface ManagerWithdrawalService {
	
	public ManagerRspWithdrawal withdrawal(ManagerReqWithdrawal managerReqWithdrawal) throws Exception;

}
