package com.api.bussiness.manager.service;

import com.api.bussiness.manager.bean.ManagerReqCheckPay;
import com.api.bussiness.manager.bean.ManagerRspCheckPay;


public interface ManagerCheckPayService {
	
	ManagerRspCheckPay checkPay(ManagerReqCheckPay managerReqCheckPay) throws Exception;
	
}
