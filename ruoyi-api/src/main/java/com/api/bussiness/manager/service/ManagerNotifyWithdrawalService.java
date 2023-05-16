package com.api.bussiness.manager.service;

import com.api.bussiness.manager.bean.ManagerReqNotifyWithdrawal;
import com.api.bussiness.manager.bean.ManagerRspNotifyWithdrawal;

public interface ManagerNotifyWithdrawalService {

	public ManagerRspNotifyWithdrawal notifyMerchantWithdrawal(ManagerReqNotifyWithdrawal managerReqNotifyWithdrawal);

}
