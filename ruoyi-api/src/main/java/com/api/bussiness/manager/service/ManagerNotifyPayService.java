package com.api.bussiness.manager.service;

import com.api.bussiness.manager.bean.ManagerReqNotifyPay;
import com.api.bussiness.manager.bean.ManagerRspNotifyPay;

public interface ManagerNotifyPayService {
	
	
	public ManagerRspNotifyPay notifyMerchantPay(ManagerReqNotifyPay managerReqNotifyPay);

}
