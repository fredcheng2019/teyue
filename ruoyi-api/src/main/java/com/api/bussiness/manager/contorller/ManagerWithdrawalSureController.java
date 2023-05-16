package com.api.bussiness.manager.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.bussiness.manager.service.ManagerWithdrawalSureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.api.bussiness.base.BaseController;
import com.api.bussiness.manager.bean.ManagerReqWithdrawalSure;
import com.api.bussiness.manager.bean.ManagerRspWithdrawalSure;

@RestController
public class ManagerWithdrawalSureController extends BaseController {
	
	private static Logger log = LoggerFactory.getLogger(ManagerWithdrawalSureController.class);
	
	@Autowired
    ManagerWithdrawalSureService managerWithdrawalSureService;
	
	@RequestMapping(value = "/manager/managerWithdrawalSure")
	public void managerWithdrawalSure(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type", "text/json;charset=UTF-8");
		try {
			 String msg = (String)request.getAttribute("msg");
			 ManagerReqWithdrawalSure managerReqWithdrawalSure = JSON.parseObject(msg, ManagerReqWithdrawalSure.class);
			 ManagerRspWithdrawalSure managerRspWithdrawalSure = null;
			 if(managerReqWithdrawalSure.getType() == ManagerReqWithdrawalSure.MANAGER_REQ_WITHDRAWAL_SURE_FAIL) {
				 managerRspWithdrawalSure  = managerWithdrawalSureService.managerWithdrawalSureWithFail(managerReqWithdrawalSure);
			 }else if(managerReqWithdrawalSure.getType() == ManagerReqWithdrawalSure.MANAGER_REQ_WITHDRAWAL_SURE_SUCCESS) {
				 managerRspWithdrawalSure = managerWithdrawalSureService.managerWithdrawalSureWithSuccess(managerReqWithdrawalSure);
			 }
			 this.writeToJson(response, managerRspWithdrawalSure);
		}catch (Exception e) {
			log.error(e.getMessage());
			ManagerRspWithdrawalSure managerRspWithdrawalSure = new ManagerRspWithdrawalSure();
			managerRspWithdrawalSure.setStatus(ManagerRspWithdrawalSure.MANAGER_RSP_WITHDRAWAL_SURE_FAIL);
			managerRspWithdrawalSure.setMsg("系统异常");
			this.writeToJson(response, managerRspWithdrawalSure);
		}
	}
	
}
