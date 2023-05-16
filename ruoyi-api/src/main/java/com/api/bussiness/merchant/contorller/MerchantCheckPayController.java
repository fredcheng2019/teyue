package com.api.bussiness.merchant.contorller;

import com.alibaba.fastjson.JSON;
import com.api.base.util.IPUtils;
import com.api.bussiness.merchant.bean.MerchantReqCheckPay;
import com.api.bussiness.merchant.bean.MerchantRspCheckPay;
import com.api.bussiness.merchant.bean.MerchantRspErrorResult;
import com.api.bussiness.merchant.service.MerchantCheckPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.bussiness.base.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class MerchantCheckPayController extends BaseController {


    private static Logger log = LoggerFactory.getLogger(MerchantCheckPayController.class);

    @Autowired
    MerchantCheckPayService merchantCheckPayService;

    @RequestMapping(value = "/pay/orderQuery")
    public void checkPay(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type", "text/json;charset=UTF-8");
        try {
            String reqIp = IPUtils.getIpAddr(request);
            MerchantReqCheckPay merchantReqCheckPay = (MerchantReqCheckPay) request.getAttribute("msg");
            MerchantRspCheckPay merchantRspCheckPay = merchantCheckPayService.checkPay(merchantReqCheckPay);

            if (merchantRspCheckPay.getCode().equals("success")) {
                log.info("访问IP：{},查询响应报文:{}",reqIp, JSON.toJSONString(merchantRspCheckPay));
                writeToJson(response, merchantRspCheckPay);
            } else {
                MerchantRspErrorResult merchantRspErrorResult = new MerchantRspErrorResult();
                merchantRspErrorResult.setCode("error");
                merchantRspErrorResult.setMsg(merchantRspCheckPay.getMsg());
                writeToJson(response, merchantRspErrorResult);
            }
        } catch (Exception e) {
            log.error("查询支付异常", e);
            MerchantRspErrorResult merchantRspErrorResult = new MerchantRspErrorResult();
            merchantRspErrorResult.setCode("error");
            merchantRspErrorResult.setMsg("查询支付异常");
            writeToJson(response, merchantRspErrorResult);
        }
    }
}
