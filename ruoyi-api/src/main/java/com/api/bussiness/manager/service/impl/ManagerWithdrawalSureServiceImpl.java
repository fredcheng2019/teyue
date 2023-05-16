package com.api.bussiness.manager.service.impl;

import java.util.Date;

import com.api.bussiness.manager.service.ManagerWithdrawalSureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.bussiness.dao.finance.FinanceDao;
import com.api.bussiness.dao.order.ChannelDao;
import com.api.bussiness.dao.order.PayDao;
import com.api.bussiness.dao.table.withdrawal.Withdrawal;
import com.api.bussiness.dao.wallet.ChannelWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletDao;
import com.api.bussiness.dao.withdrawal.WithdrawalDao;
import com.api.bussiness.manager.bean.ManagerReqWithdrawalSure;
import com.api.bussiness.manager.bean.ManagerRspWithdrawalSure;


@Service("managerWithdrawalSureService")
@Transactional(rollbackFor = Exception.class)
public class ManagerWithdrawalSureServiceImpl implements ManagerWithdrawalSureService {
	
	@Autowired
    PayDao payDao;

    @Autowired
    WithdrawalDao withdrawalDao;

    @Autowired
    ChannelWalletDao channelWalletDao;

    @Autowired
    MerchantWalletDao merchantWalletDao;

    @Autowired
    FinanceDao financeDao;

    @Autowired
    ChannelDao channelDao;
	
	@Override
	public ManagerRspWithdrawalSure managerWithdrawalSureWithFail(ManagerReqWithdrawalSure managerReqWithdrawalSure) throws Exception{
		
		ManagerRspWithdrawalSure managerRspWithdrawalSure = new ManagerRspWithdrawalSure();
		
		Withdrawal withdrawal = withdrawalDao.getWithDrawalByCode(managerReqWithdrawalSure.getCode());
		if(null == withdrawal) {
			managerRspWithdrawalSure.setMsg("订单不存在");
			managerRspWithdrawalSure.setStatus(ManagerRspWithdrawalSure.MANAGER_RSP_WITHDRAWAL_SURE_FAIL);
			return managerRspWithdrawalSure;
		}
		
		if(withdrawal.getStatus() != Withdrawal.STATUS_ORDER_TIME_OUT && withdrawal.getStatus() != Withdrawal.STATUS_FROZING) {
			managerRspWithdrawalSure.setMsg("订单状态不是超时和待结算");
			managerRspWithdrawalSure.setStatus(ManagerRspWithdrawalSure.MANAGER_RSP_WITHDRAWAL_SURE_FAIL);
			return managerRspWithdrawalSure;
		}
		
		//更新订单状态
        Date date = new Date();
        int result = withdrawalDao.updateWithDrawalStatusByCode(withdrawal.getCode(),"", Withdrawal.STATUS_WITHDRAWAL_FAIL, date);
        if(result != 1) {
        	throw new Exception("确认失败更新代付订单异常：更新结果："+result+";订单号："+withdrawal.getCode());
        }
        withdrawal.setEnd(date);
        withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_FAIL);
        
        //更新商户钱包
        int resultMch = merchantWalletDao.recoveryBalanceByThreeIdWhenFail(withdrawal.getMch_fee(), withdrawal.getMerchant_id(), withdrawal.getWallet_kind_id());
        if(resultMch != 1) {
        	throw new Exception("确认失败更新商户代付商户钱包异常，更新结果："+resultMch+";商户名称："+withdrawal.getMerchant_name()+";钱包名称："+withdrawal.getWallet_kind_name());
        }
        //更新渠道钱包
        int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindIdWhenFail(withdrawal.getCh_fee(), withdrawal.getChannel_id());
        if(resultCh != 1) {
        	throw new Exception("更新商户代付渠道钱包异常，更新结果："+resultCh+";渠道名称："+withdrawal.getChannel_name());
        }
        
        managerRspWithdrawalSure.setMsg("成功");
		managerRspWithdrawalSure.setStatus(ManagerRspWithdrawalSure.MANAGER_RSP_WITHDRAWAL_SURE_SUCCESS);
        
		return managerRspWithdrawalSure;
	}

	@Override
	public ManagerRspWithdrawalSure managerWithdrawalSureWithSuccess(ManagerReqWithdrawalSure managerReqWithdrawalSure) throws Exception{
		
		ManagerRspWithdrawalSure managerRspWithdrawalSure = new ManagerRspWithdrawalSure();
		
		Withdrawal withdrawal = withdrawalDao.getWithDrawalByCode(managerReqWithdrawalSure.getCode());
		if(null == withdrawal) {
			managerRspWithdrawalSure.setMsg("订单不存在");
			managerRspWithdrawalSure.setStatus(ManagerRspWithdrawalSure.MANAGER_RSP_WITHDRAWAL_SURE_FAIL);
			return managerRspWithdrawalSure;
		}
		
		if(withdrawal.getStatus() != Withdrawal.STATUS_ORDER_TIME_OUT && withdrawal.getStatus() != Withdrawal.STATUS_FROZING) {
			managerRspWithdrawalSure.setMsg("订单状态不是超时和待结算");
			managerRspWithdrawalSure.setStatus(ManagerRspWithdrawalSure.MANAGER_RSP_WITHDRAWAL_SURE_FAIL);
			return managerRspWithdrawalSure;
		}
		
		//更新订单状态
        Date date = new Date();
        int result = withdrawalDao.updateWithDrawalStatusByCode(withdrawal.getCode(),"", Withdrawal.STATUS_WITHDRAWAL_SUCCESS, date);
        if(result != 1) {
        	throw new Exception("确认成功更新代付订单异常：更新结果："+result+";订单号："+withdrawal.getCode());
        }
        withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_SUCCESS);
        withdrawal.setEnd(date);
        
        //更新商户钱包
        int resultMch = merchantWalletDao.recoveryBalanceByMchIdAndWalletKindId(withdrawal.getMch_fee(), withdrawal.getMerchant_id(), withdrawal.getWallet_kind_id());
        if(resultMch != 1) {
        	throw new Exception("确认成功更新商户代付商户钱包异常，更新结果："+resultMch+";商户名称："+withdrawal.getMerchant_name()+";钱包名称："+withdrawal.getWallet_kind_name());
        }
        //更新渠道钱包
        int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindId(withdrawal.getCh_fee(), withdrawal.getChannel_id());
        if(resultCh != 1) {
        	throw new Exception("确认成功更新商户代付渠道钱包异常，更新结果："+resultCh+";渠道名称："+withdrawal.getChannel_name());
        }
        //平台盈利增加
        int resultFin = financeDao.updateFinance(withdrawal.getFin_fee(), withdrawal.getChannel_id());
        if(resultFin != 1) {
        	throw new Exception("确认成功更新商户代付平台钱包异常，更新结果："+resultFin+";渠道名称："+withdrawal.getChannel_name());
        }
        managerRspWithdrawalSure.setMsg("成功");
		managerRspWithdrawalSure.setStatus(ManagerRspWithdrawalSure.MANAGER_RSP_WITHDRAWAL_SURE_SUCCESS);
        
		return managerRspWithdrawalSure;
	}

}
