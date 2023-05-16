package com.api.bussiness.merchant.contorller;

import com.api.base.util.IPUtils;
import com.api.bussiness.merchant.bean.MerchantReqPay;
import com.api.bussiness.merchant.bean.MerchantRspErrorResult;
import com.api.bussiness.merchant.bean.MerchantRspPay;
import com.api.bussiness.merchant.service.MerchantPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.bussiness.base.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支付接口
 *
 * @author 首信易支付
 */
@RestController
public class MerchantPayController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(MerchantPayController.class);

    @Autowired
    MerchantPayService merchantPayService;

    @RequestMapping(value = "/pay/order")
    public void pay(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type", "text/json;charset=UTF-8");
        try {
            MerchantReqPay merchantReqPay = (MerchantReqPay) request.getAttribute("msg");
            String clientIP = IPUtils.getIpAddr(request);
            merchantReqPay.setClientIP(clientIP);
            MerchantRspPay merchantRspPay = merchantPayService.pay(merchantReqPay);
            if (merchantRspPay.getCode().equals("success")) {
                writeToJson(response, merchantRspPay);
            } else {
                MerchantRspErrorResult merchantRspErrorResult = new MerchantRspErrorResult();
                merchantRspErrorResult.setCode("error");
                merchantRspErrorResult.setMsg(merchantRspPay.getMsg());
                writeToJson(response, merchantRspErrorResult);
            }
        } catch (Exception e) {
            log.error("支付异常", e);
            MerchantRspErrorResult merchantRspErrorResult = new MerchantRspErrorResult();
            merchantRspErrorResult.setCode("error");
            merchantRspErrorResult.setMsg("支付异常");
            writeToJson(response, merchantRspErrorResult);
        }
    }
}
