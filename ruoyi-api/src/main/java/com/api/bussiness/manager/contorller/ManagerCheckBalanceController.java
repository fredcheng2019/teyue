package com.api.bussiness.manager.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.bussiness.manager.service.ManagerCheckBalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.api.bussiness.base.BaseController;
import com.api.bussiness.manager.bean.ManagerReqCheckBalance;
import com.api.bussiness.manager.bean.ManagerRspCheckBalance;

@RestController
public class ManagerCheckBalanceController extends BaseController {
	
	private static Logger log = LoggerFactory.getLogger(ManagerCheckBalanceController.class);
	
	@Autowired
    ManagerCheckBalanceService managerCheckBalanceService;
	
	@RequestMapping(value = "/manager/checkBalance")
	public void checkBalance(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type", "text/json;charset=UTF-8");
		try {
            String msg = (String)request.getAttribute("msg");
            ManagerReqCheckBalance managerReqCheckBalance = JSON.parseObject(msg, ManagerReqCheckBalance.class);
            ManagerRspCheckBalance managerRspCheckBalance = managerCheckBalanceService.checkBalance(managerReqCheckBalance);
            writeToJson(response, managerRspCheckBalance);
		}catch (Exception e) {
			log.error(e.getMessage());
			ManagerRspCheckBalance managerRspCheckBalance = new ManagerRspCheckBalance();
			managerRspCheckBalance.setStatus(ManagerRspCheckBalance.RSP_STATUS_FAIL);
			managerRspCheckBalance.setMsg("系统异常");
			writeToJson(response, managerRspCheckBalance);
		}
		
	}

}
