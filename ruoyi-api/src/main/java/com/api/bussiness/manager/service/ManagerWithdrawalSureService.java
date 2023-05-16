package com.api.bussiness.manager.service;

import com.api.bussiness.manager.bean.ManagerReqWithdrawalSure;
import com.api.bussiness.manager.bean.ManagerRspWithdrawalSure;

public interface ManagerWithdrawalSureService {
	
	ManagerRspWithdrawalSure managerWithdrawalSureWithFail(ManagerReqWithdrawalSure managerReqWithdrawalSure) throws Exception;
	
	ManagerRspWithdrawalSure managerWithdrawalSureWithSuccess(ManagerReqWithdrawalSure managerReqWithdrawalSure) throws Exception;

}
