package com.api.bussiness.merchant.service.impl;

import com.api.bussiness.manager.bean.ManagerRspCheckWithdrawal;
import com.api.bussiness.manager.service.ManagerCheckWithdrawalService;
import com.api.bussiness.merchant.bean.MerchantReqCheckWithdrawal;
import com.api.bussiness.merchant.bean.MerchantRspCheckWithdrawal;
import com.api.bussiness.merchant.service.MerchantCheckWithDrawalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.bussiness.dao.table.withdrawal.Withdrawal;
import com.api.bussiness.manager.bean.ManagerReqCheckWithdrawal;

@Service("MerchantCheckWithDrawalService")
public class MerchantCheckWithDrawalServiceImpl implements MerchantCheckWithDrawalService {
	
	private static Logger log = LoggerFactory.getLogger(MerchantCheckWithDrawalServiceImpl.class);

    @Autowired
    ManagerCheckWithdrawalService managerCheckWithdrawalService;


    public MerchantRspCheckWithdrawal checkWithdrawal(MerchantReqCheckWithdrawal merchantReqCheckWithdrawal) {
    	
    	MerchantRspCheckWithdrawal merchantRspCheckWithdrawal = new MerchantRspCheckWithdrawal();
    	
        ManagerReqCheckWithdrawal managerReqCheckWithdrawal = new ManagerReqCheckWithdrawal();
        managerReqCheckWithdrawal.setMch_id(merchantReqCheckWithdrawal.getMch_id());
        managerReqCheckWithdrawal.setOut_trade_no(merchantReqCheckWithdrawal.getOut_trade_no());
        ManagerRspCheckWithdrawal managerRspCheckWithdrawal = null;
		try {
			managerRspCheckWithdrawal = managerCheckWithdrawalService.checkWithdrawal(managerReqCheckWithdrawal);
		} catch (Exception e) {
			log.error(e.getMessage());
			merchantRspCheckWithdrawal.setCode("error");
            merchantRspCheckWithdrawal.setMsg("系统异常");
            return merchantRspCheckWithdrawal;
		}

        Withdrawal withdrawal = managerRspCheckWithdrawal.getWithdrawal();

        
        if (withdrawal == null) {
            merchantRspCheckWithdrawal.setCode("error");
            merchantRspCheckWithdrawal.setMsg("代付订单无效");
        } else {
            merchantRspCheckWithdrawal.setCode("success");
            merchantRspCheckWithdrawal.setMsg("查询代付订单成功");

            merchantRspCheckWithdrawal.setBank_address(withdrawal.getBank_branch());
            merchantRspCheckWithdrawal.setBank_car_name(withdrawal.getBank_name());
            merchantRspCheckWithdrawal.setBank_car_no(withdrawal.getBank_account_no());
            merchantRspCheckWithdrawal.setBank_city(withdrawal.getBank_city());
            merchantRspCheckWithdrawal.setBank_province(withdrawal.getBank_province());
            merchantRspCheckWithdrawal.setBank_type(withdrawal.getBank_code());
            merchantRspCheckWithdrawal.setBranch(withdrawal.getBank_sub_branch());
            merchantRspCheckWithdrawal.setCreate_time(withdrawal.getBegin().getTime() + "");
            if (null != withdrawal.getEnd()) {
                merchantRspCheckWithdrawal.setFinish_time(withdrawal.getEnd().getTime() + "");
            }
            merchantRspCheckWithdrawal.setMch_id(withdrawal.getMerchant_code());
            merchantRspCheckWithdrawal.setOrder_sn(withdrawal.getCode());
            merchantRspCheckWithdrawal.setOut_trade_no(withdrawal.getMch_code());
            if (withdrawal.getStatus() == Withdrawal.STATUS_WITHDRAWAL_SUCCESS) {//5已代付
                merchantRspCheckWithdrawal.setStatus(2);
            } else if (withdrawal.getStatus() == Withdrawal.STATUS_FROZING || withdrawal.getStatus() == Withdrawal.STATUS_ORDER_TIME_OUT) {//冻结中
                merchantRspCheckWithdrawal.setStatus(1);
            } else {
                merchantRspCheckWithdrawal.setStatus(0);
            }

            merchantRspCheckWithdrawal.setUnionBankNum(withdrawal.getBank_union_no());
            merchantRspCheckWithdrawal.setWallet_type(withdrawal.getWallet_kind_code());
        }
        return merchantRspCheckWithdrawal;
    }

}
