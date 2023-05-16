package com.api.bussiness.manager.service;

import com.api.bussiness.manager.bean.ManagerReqWithdrawalApplying;
import com.api.bussiness.manager.bean.ManagerRspWithdrawalApplying;

public interface ManagerWithdrawalApplyingService {
	
	public ManagerRspWithdrawalApplying withdrawalPass(ManagerReqWithdrawalApplying managerReqWithdrawalApplying) throws Exception;
	
	public ManagerRspWithdrawalApplying withdrawalRefuse(ManagerReqWithdrawalApplying managerReqWithdrawalApplying) throws Exception;

}
