package com.api.bussiness.manager.service;

import com.api.bussiness.manager.bean.ManagerReqFinanceWithdrawal;
import com.api.bussiness.manager.bean.ManagerRspFinanceWithdrawal;

public interface ManagerFinanceWithdrawalService {

	public ManagerRspFinanceWithdrawal managerFinanceWithdrawal(ManagerReqFinanceWithdrawal managerReqFinanceWithdrawal) throws Exception;

}
