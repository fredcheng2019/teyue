package com.api.bussiness.manager.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.bussiness.manager.service.ManagerNotifyPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.api.bussiness.base.BaseController;
import com.api.bussiness.manager.bean.ManagerReqNotifyPay;
import com.api.bussiness.manager.bean.ManagerRspNotifyPay;

@RestController
public class ManagerNotifyPayController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(ManagerNotifyPayController.class);

    @Autowired
    ManagerNotifyPayService managerNotifyPayService;

    /**
     * 补单通知商户
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/manager/notifyPay")
    public void notifyPay(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type", "text/json;charset=UTF-8");
        try {
            String msg = (String) request.getAttribute("msg");
            ManagerReqNotifyPay managerReqNotifyPay = JSON.parseObject(msg, ManagerReqNotifyPay.class);
            ManagerRspNotifyPay managerRspNotifyPay = managerNotifyPayService.notifyMerchantPay(managerReqNotifyPay);
            writeToJson(response, managerRspNotifyPay);
        } catch (Exception e) {
            log.error(e.getMessage());
            ManagerRspNotifyPay managerRspNotifyPay = new ManagerRspNotifyPay();
            managerRspNotifyPay.setStatus(ManagerRspNotifyPay.RSP_STATUS_FAIL);
            managerRspNotifyPay.setMsg("系统异常");
            writeToJson(response, managerRspNotifyPay);
        }

    }

}
