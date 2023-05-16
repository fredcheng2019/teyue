package com.api.bussiness.merchant.service;

import com.api.bussiness.merchant.bean.MerchantReqWithdrawal;
import com.api.bussiness.merchant.bean.MerchantRspWithdrawal;

public interface MerchantWithdrawalService {

	MerchantRspWithdrawal withdrawal(MerchantReqWithdrawal drawal) throws Exception;

}
