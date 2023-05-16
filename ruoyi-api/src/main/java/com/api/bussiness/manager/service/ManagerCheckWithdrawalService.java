package com.api.bussiness.manager.service;

import com.api.bussiness.manager.bean.ManagerRspCheckWithdrawal;
import com.api.bussiness.manager.bean.ManagerReqCheckWithdrawal;


public interface ManagerCheckWithdrawalService {
	ManagerRspCheckWithdrawal checkWithdrawal(ManagerReqCheckWithdrawal managerReqCheckWithdrawal) throws Exception;
}
