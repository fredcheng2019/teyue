package com.api.bussiness.merchant.service.impl;

import com.api.bussiness.manager.bean.ManagerReqCheckPay;
import com.api.bussiness.manager.bean.ManagerRspCheckPay;
import com.api.bussiness.manager.service.ManagerCheckPayService;
import com.api.bussiness.merchant.bean.MerchantReqCheckPay;
import com.api.bussiness.merchant.bean.MerchantRspCheckPay;
import com.api.bussiness.merchant.service.MerchantCheckPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.bussiness.dao.table.order.MpPay;


@Service("MerchantCheckPayService")
public class MerchantCheckPayServiceImpl implements MerchantCheckPayService {

    private static Logger log = LoggerFactory.getLogger(MerchantCheckPayServiceImpl.class);

    @Autowired
    ManagerCheckPayService managerCheckPayService;

    @Override
    public MerchantRspCheckPay checkPay(MerchantReqCheckPay merchantReqCheckPay) {
        MerchantRspCheckPay merchantRspCheckPay = new MerchantRspCheckPay();
        ManagerReqCheckPay managerReqCheckPay = new ManagerReqCheckPay();
        managerReqCheckPay.setMch_code(merchantReqCheckPay.getOut_trade_no());
        managerReqCheckPay.setMerchant_code(merchantReqCheckPay.getMch_id());

        ManagerRspCheckPay managerRspCheckPay = null;
        try {
            managerRspCheckPay = managerCheckPayService.checkPay(managerReqCheckPay);
        } catch (Exception e) {
            log.error(merchantReqCheckPay.getOut_trade_no()+"查询订单异常："+e.getMessage());
            merchantRspCheckPay.setCode("error");
            merchantRspCheckPay.setMsg("系统异常");
            return merchantRspCheckPay;
        }

        MpPay mpPay = managerRspCheckPay.getMpPay();

        if (mpPay == null) {
            merchantRspCheckPay.setCode("error");
            merchantRspCheckPay.setMsg("支付订单无效");
        } else {
            merchantRspCheckPay.setCode("success");
            merchantRspCheckPay.setMsg("查询支付订单成功");

            merchantRspCheckPay.setChannel_pay_code(mpPay.getPay_kind_code());
            merchantRspCheckPay.setCreate_time(mpPay.getBegin().getTime() + "");
            if (mpPay.getStatus() == MpPay.STATUS_PAY_SUCCESS) {//已支付
                merchantRspCheckPay.setFinish_time(mpPay.getEnd().getTime() + "");
            } else {
                merchantRspCheckPay.setFinish_time(null);
            }
            merchantRspCheckPay.setMch_id(mpPay.getMerchant_code());
            merchantRspCheckPay.setOrder_sn(mpPay.getCode());
            merchantRspCheckPay.setOut_trade_no(mpPay.getMch_code());
            merchantRspCheckPay.setRemark(mpPay.getRemarks());

            if (mpPay.getMerchant_notify_status() == 0) {//判断通知状态,未通知
                if (mpPay.getStatus() == MpPay.STATUS_WAIT_TO_PAY || mpPay.getStatus() == MpPay.STATUS_PAY_FAIL) {//可能是未支付的
                    merchantRspCheckPay.setResponse_status(0);
                } else {
                    merchantRspCheckPay.setResponse_status(1);
                }
            } else if (mpPay.getMerchant_notify_status() == 1) {
                merchantRspCheckPay.setResponse_status(2);
            }

            if (mpPay.getStatus() == MpPay.STATUS_PAY_SUCCESS) {//已支付
                merchantRspCheckPay.setStatus(1);
            } else {
                merchantRspCheckPay.setStatus(0);
            }
            merchantRspCheckPay.setTime_expire(mpPay.getExpires().getTime() + "");
            merchantRspCheckPay.setTime_start(mpPay.getBegin().getTime() + "");
            merchantRspCheckPay.setTotal_fee(mpPay.getSub_fee());
        }

        return merchantRspCheckPay;
    }

}
