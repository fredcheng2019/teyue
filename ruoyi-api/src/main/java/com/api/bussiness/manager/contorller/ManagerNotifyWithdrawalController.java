package com.api.bussiness.manager.contorller;

import com.alibaba.fastjson.JSON;
import com.api.bussiness.manager.service.ManagerNotifyWithdrawalService;
import com.api.bussiness.base.BaseController;
import com.api.bussiness.manager.bean.ManagerReqNotifyWithdrawal;
import com.api.bussiness.manager.bean.ManagerRspNotifyPay;
import com.api.bussiness.manager.bean.ManagerRspNotifyWithdrawal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ManagerNotifyWithdrawalController extends BaseController {
	
	private static Logger log = LoggerFactory.getLogger(ManagerNotifyWithdrawalController.class);
	
	@Autowired
    ManagerNotifyWithdrawalService managerNotifyWithdrawalService;
	
	@RequestMapping(value = "/manager/notifyWithdrawal")
	public void notifyWithdrawal(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type", "text/json;charset=UTF-8");
		try {
            String msg = (String)request.getAttribute("msg");
            ManagerReqNotifyWithdrawal managerReqNotifyWithdrawal = JSON.parseObject(msg, ManagerReqNotifyWithdrawal.class);
            ManagerRspNotifyWithdrawal managerRspNotifyWithdrawal = managerNotifyWithdrawalService.notifyMerchantWithdrawal(managerReqNotifyWithdrawal);
            this.writeToJson(response, managerRspNotifyWithdrawal);
		}catch (Exception e) {
			log.error(e.getMessage());
			ManagerRspNotifyPay managerRspNotifyPay = new ManagerRspNotifyPay();
			managerRspNotifyPay.setStatus(ManagerRspNotifyPay.RSP_STATUS_FAIL);
			managerRspNotifyPay.setMsg("系统异常");
			try {
				writeToJson(response, managerRspNotifyPay);
			}catch (Exception e2){

			}
		}
	}

}
