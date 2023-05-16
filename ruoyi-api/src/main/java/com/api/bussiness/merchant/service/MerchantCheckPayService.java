package com.api.bussiness.merchant.service;

import com.api.bussiness.merchant.bean.MerchantReqCheckPay;
import com.api.bussiness.merchant.bean.MerchantRspCheckPay;

public interface MerchantCheckPayService {


	MerchantRspCheckPay checkPay(MerchantReqCheckPay merchantReqCheckPay);

}
