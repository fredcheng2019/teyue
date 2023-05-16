package com.api.bussiness.manager.service.impl;

import com.api.bussiness.manager.service.ManagerNotifyWithdrawalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.bussiness.dao.order.PayDao;
import com.api.bussiness.dao.table.withdrawal.Withdrawal;
import com.api.bussiness.dao.withdrawal.WithdrawalDao;
import com.api.bussiness.manager.bean.ManagerReqNotifyWithdrawal;
import com.api.bussiness.manager.bean.ManagerRspNotifyPay;
import com.api.bussiness.manager.bean.ManagerRspNotifyWithdrawal;
import com.api.bussiness.merchant.service.MerchantNotifyService;


@Service("ManagerNotifyWithdrawalService")
public class ManagerNotifyWithdrawalServiceImpl implements ManagerNotifyWithdrawalService {
	
	private static Logger log = LoggerFactory.getLogger(ManagerNotifyWithdrawalServiceImpl.class);
	
	@Autowired
	PayDao payDao;
	
	@Autowired
	WithdrawalDao withdrawalDao;
	
	@Autowired
	MerchantNotifyService merchantNotifyService;

	@Override
	public ManagerRspNotifyWithdrawal notifyMerchantWithdrawal(ManagerReqNotifyWithdrawal managerReqNotifyWithdrawal) {
		ManagerRspNotifyWithdrawal managerRspNotifyWithdrawal = new ManagerRspNotifyWithdrawal();
		Withdrawal withdrawal = withdrawalDao.getWithDrawalByCode(managerReqNotifyWithdrawal.getCode());
		log.info("商户号:{}", withdrawal.getMerchant_code());
		//通知次数加一
		//withdrewDao.updateAddMerchantNotifyTimesByCode(withdrawal.getCode());
		managerRspNotifyWithdrawal.setMsg("通知失败");
		managerRspNotifyWithdrawal.setStatus(ManagerRspNotifyPay.RSP_STATUS_FAIL);
		return managerRspNotifyWithdrawal;
	}
	
	

}
