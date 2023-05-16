package com.api.bussiness.merchant.service;

import com.api.bussiness.merchant.bean.MerchantReqCheckWithdrawal;
import com.api.bussiness.merchant.bean.MerchantRspCheckWithdrawal;

public interface MerchantCheckWithDrawalService {

	MerchantRspCheckWithdrawal checkWithdrawal(MerchantReqCheckWithdrawal merchantReqCheckWithdrawal);

}
