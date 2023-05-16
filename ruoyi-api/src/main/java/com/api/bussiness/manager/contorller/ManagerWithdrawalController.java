package com.api.bussiness.manager.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.bussiness.manager.service.ManagerWithdrawalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.api.bussiness.base.BaseController;
import com.api.bussiness.manager.bean.ManagerReqWithdrawal;
import com.api.bussiness.manager.bean.ManagerRspWithdrawal;

@RestController
public class ManagerWithdrawalController extends BaseController {
	
	private static Logger log = LoggerFactory.getLogger(ManagerWithdrawalController.class);
	
	@Autowired
    ManagerWithdrawalService managerWithdrawalService;
	
	/**
	 *     平台支付，不跟渠道
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/manager/withdrawal")
	public void withdrawal(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type", "text/json;charset=UTF-8");
		try {
			String msg = (String)request.getAttribute("msg");
			ManagerReqWithdrawal managerReqWithdrawal = JSON.parseObject(msg, ManagerReqWithdrawal.class);
			ManagerRspWithdrawal managerRspWithdrawal = managerWithdrawalService.withdrawal(managerReqWithdrawal);
			this.writeToJson(response, managerRspWithdrawal);
		}catch (Exception e) {
			log.error(e.getMessage());
			ManagerRspWithdrawal managerRspWithdrawal = new ManagerRspWithdrawal();
			managerRspWithdrawal.setMsg("服务器异常");
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);
			this.writeToJson(response, managerRspWithdrawal);
		}
	}
	

}
