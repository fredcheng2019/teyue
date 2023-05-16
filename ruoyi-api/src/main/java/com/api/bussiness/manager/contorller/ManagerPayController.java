package com.api.bussiness.manager.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.bussiness.manager.service.ManagerPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.api.bussiness.base.BaseController;
import com.api.bussiness.manager.bean.ManagerReqPay;
import com.api.bussiness.manager.bean.ManagerRspPay;


@RestController
public class ManagerPayController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(ManagerPayController.class);

    @Autowired
    ManagerPayService managerPayService;

    /**
     * 平台支付，不跟渠道
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/manager/pay")
    public void pay(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type", "text/json;charset=UTF-8");
        try {
            String msg = (String) request.getAttribute("msg");
            ManagerReqPay managerReqPay = JSON.parseObject(msg, ManagerReqPay.class);
            ManagerRspPay managerRspPay = managerPayService.pay(managerReqPay);
            this.writeToJson(response, managerRspPay);
        } catch (Exception e) {
            log.error(e.getMessage());
            ManagerRspPay managerRspPay = new ManagerRspPay();
            managerRspPay.setMsg("服务器异常");
            managerRspPay.setStatus(ManagerRspPay.MANAGER_RSP_PAY_FAIL);
            this.writeToJson(response, managerRspPay);
        }
    }


}
