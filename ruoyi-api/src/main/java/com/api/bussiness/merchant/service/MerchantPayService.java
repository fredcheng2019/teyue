package com.api.bussiness.merchant.service;

import com.api.bussiness.merchant.bean.MerchantReqPay;
import com.api.bussiness.merchant.bean.MerchantRspPay;

public interface MerchantPayService {

	MerchantRspPay pay(MerchantReqPay order) throws Exception;

}
